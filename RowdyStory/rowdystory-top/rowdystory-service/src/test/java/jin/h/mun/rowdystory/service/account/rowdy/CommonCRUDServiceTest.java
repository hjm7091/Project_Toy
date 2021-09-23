package jin.h.mun.rowdystory.service.account.rowdy;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.api.RegisterRequest;
import jin.h.mun.rowdystory.dto.account.api.UpdateRequest;
import jin.h.mun.rowdystory.exception.account.AccountPreviousPasswordUnmatchedException;
import jin.h.mun.rowdystory.exception.account.SocialAccountUnmodifiableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith( SpringExtension.class )
class CommonCRUDServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CommonCRUDService commonCRUDService;

    @Test
    @DisplayName( "모든 계정 조회 테스트" )
    public void getAll() throws Exception {
        //given
        User user1 = User.builder().email( "user1@test.com" ).userName( "user1" ).build();
        User user2 = User.builder().email( "user2@test.com" ).userName( "user2" ).build();

        //when
        when( userRepository.findAll() ).thenReturn( Arrays.asList( user1, user2 ) );
        List<UserDTO> userDTOs = commonCRUDService.getAll();

        //then
        assertThat( userDTOs )
                .hasSize( 2 )
                .extracting( UserDTO::getEmail )
                .containsExactlyInAnyOrder( user2.getEmail(), user1.getEmail() );
    }

    @Test
    @DisplayName( "아이디로 계정 조회 테스트" )
    public void get() throws Exception {
        //given
        User user = User.builder().id( 1L ).email( "user@test.com" ).build();

        //when
        when( userRepository.findById( 1L ) ).thenReturn( Optional.of( user ) );
        Optional<UserDTO> userOpt1L = commonCRUDService.getById( 1L );
        Optional<UserDTO> userOpt2L = commonCRUDService.getById( 2L );

        //then
        assertThat( userOpt1L.isPresent() ).isTrue();
        assertThat( userOpt2L.isPresent() ).isFalse();
    }

    @Test
    @DisplayName( "계정 등록 테스트" )
    public void register() throws Exception {
        //given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email( "user@test.com" )
                .password( "1234" )
                .userName( "user" ).build();

        //when
        when( userRepository.save( any( User.class ) ) ).then( returnsFirstArg() );
        when( passwordEncoder.encode( anyString() ) ).thenReturn( "encrypted" );
        UserDTO userDTO = commonCRUDService.register( registerRequest );

        //then
        assertThat( userDTO.getEmail() ).isEqualTo( registerRequest.getEmail() );
        assertThat( userDTO.getUserName() ).isEqualTo( registerRequest.getUserName() );
    }

    @Test
    @DisplayName( "id 또는 email 에 따라 계정 업데이트 테스트" )
    public void update() throws Exception {
        //given
        User user = User.builder()
                .id( 1L )
                .email( "user@test.com" )
                .userName( "user" )
                .picture( "picture" )
                .roleType( RoleType.GUEST ).build();

        UpdateRequest updateRequest = UpdateRequest.builder()
                .password( "updatePassword" )
                .userName( "updateUserName" )
                .picture( "updatePicture" )
                .roleType( "user" )
                .build();

        //when
        when( userRepository.findById( 1L ) ).thenReturn( Optional.of( user ) );
        when( userRepository.findByEmail( "user@test.com" ) ).thenReturn( Optional.of( user ) );
        when( passwordEncoder.encode( anyString() ) ).thenReturn( "encrypted" );
        Optional<UserDTO> userOpt1L = commonCRUDService.updateById( 1L, updateRequest );
        Optional<UserDTO> userOpt2L = commonCRUDService.updateById( 2L, updateRequest );
        Optional<UserDTO> userOptTest = commonCRUDService.updateByEmail( "user@test.com", updateRequest );
        Optional<UserDTO> userOptInvalid = commonCRUDService.updateByEmail( "invalid", updateRequest );

        //then
        assertThat( userOpt1L.isPresent() ).isTrue();
        assertThat( userOpt2L.isPresent() ).isFalse();
        assertThat( userOptTest.isPresent() ).isTrue();
        assertThat( userOptInvalid.isPresent() ).isFalse();
    }

    @Test
    @DisplayName( "계정 삭제 테스트" )
    public void delete() throws Exception {
        //given
        User user = User.builder()
                .id( 1L )
                .email( "user@test.com" )
                .userName( "user" )
                .picture( "picture" )
                .roleType( RoleType.GUEST ).build();

        //when
        when( userRepository.findById( 1L ) ).thenReturn( Optional.of( user ) );

        //then
        assertThat( commonCRUDService.deleteById( 1L ) ).isTrue();
        assertThat( commonCRUDService.deleteById( 2L ) ).isFalse();
    }

    @Test
    @DisplayName( "이메일 중복 테스트" )
    public void checkDuplicate() {
        //given
        String email = "jin@test.com";
        User user = User.builder().email( email ).build();

        //when
        when( userRepository.findByEmail( email ) ).thenReturn( Optional.of( user ) );

        //then
        assertThat( commonCRUDService.checkDuplicate( email ) ).isTrue();
        assertThat( commonCRUDService.checkDuplicate( "jin111@test.com" ) ).isFalse();
    }

    @Test
    @DisplayName( "이메일 변경 테스트 (소셜 유저가 아닌 경우 이메일 변경됨)" )
    public void changeNonSocialUser() {
        //given
        User jin = User.builder().email( "jin@test.com" ).build();
        String hakEmail = "hak@test.com";

        //when
        when( userRepository.findByEmail( jin.getEmail() ) ).thenReturn( Optional.of( jin ) );
        UserDTO changedDTO = commonCRUDService.updateEmail( jin.getEmail(), hakEmail );

        //then
        assertThat( jin.getEmail() ).isEqualTo( hakEmail );
    }

    @Test
    @DisplayName( "이메일 변경 테스트 (소셜 유저인 경우 예외 발생)" )
    public void changeSocialUser() {
        //given
        User jin = User.builder().email( "jin@test.com" ).socialType( SocialType.GOOGLE ).build();
        String hakEmail = "hak@test.com";

        //when
        when( userRepository.findByEmail( jin.getEmail() ) ).thenReturn( Optional.of( jin ) );
        assertThrows( SocialAccountUnmodifiableException.class, () -> commonCRUDService.updateEmail( jin.getEmail(), hakEmail ) );
        assertThrows( SocialAccountUnmodifiableException.class, () -> commonCRUDService.updatePassword( jin.getEmail(), "1111", "2222" ) );
    }

    @Test
    @DisplayName( "비밀번호 변경 테스트 (이전 비밀번호가 일치하지 않는 경우 예외 발생)" )
    public void changeWithInvalidPreviousPassword() {
        //given
        User jin = User.builder().email( "jin@test.com" ).password( "1234" ).build();
        String from = "1111", to = "2222";

        //when
        when( userRepository.findByEmail( jin.getEmail() ) ).thenReturn( Optional.of( jin ) );
        when( passwordEncoder.matches( from, jin.getPassword() ) ).thenReturn( false );
        assertThrows( AccountPreviousPasswordUnmatchedException.class, () -> commonCRUDService.updatePassword( jin.getEmail(), from, to ) );
    }

    @Test
    @DisplayName( "비밀번호 변경 테스트 (이전 비밀번호가 일치하는 경우 비밀번호 변경됨)" )
    public void changeWithValidPreviousPassword() {
        //given
        User jin = User.builder().email( "jin@test.com" ).password( "1234" ).build();
        String from = "1234", to = "2222";

        //when
        when( userRepository.findByEmail( jin.getEmail() ) ).thenReturn( Optional.of( jin ) );
        when( passwordEncoder.matches( from, jin.getPassword() ) ).thenReturn( true );
        commonCRUDService.updatePassword( jin.getEmail(), from, to );

        //then
        assertThat( jin.getPassword() ).isEqualTo( to );
    }

    @Test
    @DisplayName( "테스트 커버리지를 위해 작성" )
    public void forCoverage() {
        //given
        String hakEmail = "hak@test.com";

        //when
        assertThrows( IllegalStateException.class, () -> commonCRUDService.updateEmail( "test", hakEmail ) );
        assertThrows( IllegalStateException.class, () -> commonCRUDService.updatePassword( "test", "before", "after" ) );
    }
}
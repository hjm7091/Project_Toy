package jin.h.mun.rowdystory.service.account.rowdy;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.dto.account.UserUpdateRequest;
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
class AccountServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName( "모든 계정 조회 테스트" )
    public void getAll() throws Exception {
        //given
        User user1 = User.builder().email( "user1@test.com" ).userName( "user1" ).build();
        User user2 = User.builder().email( "user2@test.com" ).userName( "user2" ).build();

        //when
        when( userRepository.findAll() ).thenReturn( Arrays.asList( user1, user2 ) );
        List<UserDTO> userDTOs = accountService.getAll();

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
        Optional<UserDTO> userOpt1L = accountService.get( 1L );
        Optional<UserDTO> userOpt2L = accountService.get( 2L );

        //then
        assertThat( userOpt1L.isPresent() ).isTrue();
        assertThat( userOpt2L.isPresent() ).isFalse();
    }

    @Test
    @DisplayName( "계정 등록 테스트" )
    public void register() throws Exception {
        //given
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email( "user@test.com" )
                .password( "1234" )
                .userName( "user" ).build();

        //when
        when( userRepository.save( any( User.class ) ) ).then( returnsFirstArg() );
        when( passwordEncoder.encode( anyString() ) ).thenReturn( "encrypted" );
        UserDTO userDTO = accountService.register( userRegisterRequest );

        //then
        assertThat( userDTO.getEmail() ).isEqualTo( userRegisterRequest.getEmail() );
        assertThat( userDTO.getUserName() ).isEqualTo( userRegisterRequest.getUserName() );
    }

    @Test
    @DisplayName( "계정 업데이트 테스트" )
    public void update() throws Exception {
        //given
        User user = User.builder()
                .id( 1L )
                .email( "user@test.com" )
                .userName( "user" )
                .picture( "picture" )
                .roleType( RoleType.GUEST ).build();

        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .password( "updatePassword" )
                .userName( "updateUserName" )
                .picture( "updatePicture" )
                .roleType( "user" )
                .build();

        //when
        when( userRepository.findById( 1L ) ).thenReturn( Optional.of( user ) );
        when( passwordEncoder.encode( anyString() ) ).thenReturn( "encrypted" );
        Optional<UserDTO> userOpt1L = accountService.update( 1L, userUpdateRequest );
        Optional<UserDTO> userOpt2L = accountService.update( 2L, userUpdateRequest );

        //then
        assertThat( userOpt1L.isPresent() ).isTrue();
        assertThat( userOpt2L.isPresent() ).isFalse();
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
        assertThat( accountService.delete( 1L ) ).isTrue();
        assertThat( accountService.delete( 2L ) ).isFalse();
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
        assertThat( accountService.checkDuplicate( email ) ).isTrue();
        assertThat( accountService.checkDuplicate( "jin111@test.com" ) ).isFalse();
    }

    @Test
    @DisplayName( "이메일 변경 테스트" )
    public void changeEmail() {
        //given
        User jin = User.builder().email( "jin@test.com" ).build();
        String hakEmail = "hak@test.com";

        //when
        when( userRepository.findByEmail( jin.getEmail() ) ).thenReturn( Optional.of( jin ) );
        UserDTO changedDTO = accountService.changeEmail( jin.getEmail(), hakEmail );

        //then
        assertThat( jin.getEmail() ).isEqualTo( hakEmail );
    }

    @Test
    @DisplayName( "이메일 변경 테스트 (이런 경우는 없어야함), 테스트 커버리지를 위해 작성" )
    public void changeEmailForTestCoverage() {
        //given
        String hakEmail = "hak@test.com";

        //when
        assertThrows( IllegalStateException.class, () -> accountService.changeEmail( "test", hakEmail ) );
    }
}
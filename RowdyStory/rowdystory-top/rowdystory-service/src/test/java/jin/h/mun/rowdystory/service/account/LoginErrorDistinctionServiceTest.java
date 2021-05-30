package jin.h.mun.rowdystory.service.account;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.dto.account.UserLoginRequest;
import jin.h.mun.rowdystory.service.account.LoginErrorDistinctionService.DistinctionResult;
import jin.h.mun.rowdystory.service.account.LoginErrorDistinctionService.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith( SpringExtension.class )
class LoginErrorDistinctionServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginErrorDistinctionService loginErrorDistinctionService;

    @Test
    @DisplayName( "mocking 이 정상적으로 되었는지 확인" )
    public void contextsLoad() {
        assertThat( userRepository ).isNotNull();
        assertThat( loginErrorDistinctionService ).isNotNull();
    }

    @Test
    @DisplayName( "이메일이 다른 경우 로그인 email error 발생" )
    public void loginWithDifferentEmail() {
        //given
        User jin = User.builder().email( "jin@test.com" ).password( "1234" ).build();
        when( userRepository.findByEmail( jin.getEmail() ) ).thenReturn( Optional.of( jin ) );
        UserLoginRequest requestWithDifferentEmail = UserLoginRequest.builder().email( "test" ).password( jin.getPassword() ).build();

        //when
        DistinctionResult distinctionResult = loginErrorDistinctionService.distinguish( requestWithDifferentEmail );

        //then
        assertThat( distinctionResult.getFieldError().getDefaultMessage() ).isEqualTo( ErrorMessage.email );
    }

    @Test
    @DisplayName( "비밀번호가 다른 경우 password error 발생" )
    public void loginWithDifferentPassword() {
        //given
        User jin = User.builder().email( "jin@test.com" ).password( "1234" ).build();
        when( userRepository.findByEmail( jin.getEmail() ) ).thenReturn( Optional.of( jin ) );
        UserLoginRequest requestWithDifferentPassword = UserLoginRequest.builder().email( jin.getEmail() ).password( "1111" ).build();

        //when
        DistinctionResult distinctionResult = loginErrorDistinctionService.distinguish( requestWithDifferentPassword );

        //then
        assertThat( distinctionResult.getFieldError().getDefaultMessage() ).isEqualTo( ErrorMessage.password );
    }

    @Test
    @DisplayName( "소셜 계정으로 로그인 시도하는 경우 social error 발생" )
    public void loginWithSameEmailAndPassword() {
        //given
        User jin = User.builder().email( "jin@test.com" ).socialType( SocialType.GOOGLE ).build();
        when( userRepository.findByEmail( jin.getEmail() ) ).thenReturn( Optional.of( jin ) );
        UserLoginRequest requestWithSameEmailAndPassword = UserLoginRequest.builder().email( jin.getEmail() ).password( "1234" ).build();

        //when
        DistinctionResult distinctionResult = loginErrorDistinctionService.distinguish( requestWithSameEmailAndPassword );

        //then
        assertThat( distinctionResult.getFieldError().getDefaultMessage() ).isEqualTo( ErrorMessage.social );
    }

}
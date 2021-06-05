package jin.h.mun.rowdystory.service.account.rowdy;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.exception.account.AlreadyRegisteredSocialException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith( SpringExtension.class )
class FormLoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FormLoginService formLoginService;

    @Test
    @DisplayName( "이메일이 일치하는 경우 UserDetails 반환" )
    public void loadUserWithMatchedEmail() {
        //given
        User jin = User.builder().email( "jin@test.com" ).password( "1234" ).roleType( RoleType.USER ).build();
        when( userRepository.findByEmail( jin.getEmail() ) ).thenReturn( Optional.of( jin ) );

        //when
        UserDetails userDetails = formLoginService.loadUserByUsername( jin.getEmail() );

        //then
        assertThat( userDetails.getUsername() ).isEqualTo( jin.getEmail() );
        assertThat( userDetails.getAuthorities() )
                .anyMatch( authority -> authority.getAuthority().equals( RoleType.USER.getRoleName() ) );
        assertThat( userDetails.getPassword() ).isEqualTo( jin.getPassword() );
    }

    @Test
    @DisplayName( "이메일이 다른 경우 UsernameNotFoundException 발생" )
    public void loadUserWithUnMatchedEmail() {
        //given
        User jin = User.builder().email( "jin@test.com" ).password( "1234" ).roleType( RoleType.USER ).build();
        when( userRepository.findByEmail( jin.getEmail() ) ).thenReturn( Optional.of( jin ) );

        //when
        assertThrows( UsernameNotFoundException.class, () -> formLoginService.loadUserByUsername( "other@test.com" ) );
    }

    @Test
    @DisplayName( "이메일이 일치하지만 해당 유저가 소셜로 로그인 했던 경우에는 Form 으로 로그인 시도시 AlreadyRegisteredSocialException 발생" )
    public void loadUserWithMatchedEmailButUserAlreadyHasBeenToLoginSocial() {
        //given
        User jin = User.builder().email( "jin@test.com" ).password( "1234" ).roleType( RoleType.USER ).socialType( SocialType.GOOGLE ).build();
        when( userRepository.findByEmail( jin.getEmail() ) ).thenReturn( Optional.of( jin ) );

        //when
        assertThrows( AlreadyRegisteredSocialException.class, () -> formLoginService.loadUserByUsername( jin.getEmail() ) );
    }
}
package jin.h.mun.rowdystory.service.account.social;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.exception.account.DuplicatedEmailException;
import jin.h.mun.rowdystory.service.account.social.factory.OAuth2UserFactory;
import jin.h.mun.rowdystory.service.account.social.factory.OAuth2UserRequestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith( SpringExtension.class )
class OAuth2LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpSession httpSession;

    @Mock
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> delegator;

    @InjectMocks
    private OAuth2LoginService oAuth2LoginService;

    @Test
    @DisplayName( "mocking 이 정상적으로 되었는지 확인" )
    public void contextLoads() {
        assertThat( oAuth2LoginService.getUserRepository() ).isNotNull();
        assertThat( oAuth2LoginService.getHttpSession() ).isNotNull();
        assertThat( oAuth2LoginService.getDelegator() ).isNotNull();
    }

    @Test
    @DisplayName( "DB에 중복되는 이메일을 가지고 있는 다른 유저가 없는 경우에는 소셜 로그인 성공" )
    public void oauth2LoginWithUniqueEmail() {
        //given
        User userWhoTriedSocialLogin = User.builder().email( "jin@test.com" ).roleType( RoleType.USER ).socialType( SocialType.GOOGLE ).build();
        when( userRepository.findByEmail( anyString() ) ).thenReturn( Optional.empty() );
        when( userRepository.save( any( User.class ) ) ).thenReturn( userWhoTriedSocialLogin );

        OAuth2UserRequest googleOAuth2UserRequest = OAuth2UserRequestFactory.ofGoogle();
        DefaultOAuth2User googleOAuth2User = OAuth2UserFactory.ofGoogle()
                .addAttribute( "email", userWhoTriedSocialLogin.getEmail() ).build();
        when( delegator.loadUser( googleOAuth2UserRequest ) ).thenReturn( googleOAuth2User );

        //when
        OAuth2User oAuth2User = oAuth2LoginService.loadUser( googleOAuth2UserRequest );

        //then
        assertThat( oAuth2User ).isNotNull();
    }

    @Test
    @DisplayName( "DB에 중복되는 이메일을 가지고 있는 일반 유저가 있는 경우에는 소셜 로그인 실패" )
    public void oauth2LoginDuplicatedEmailForm() {
        //given
        User userJoinedByForm = User.builder().id( 1L ).email( "form@test.com" ).roleType( RoleType.USER ).build();
        User userWhoTriedSocialLogin = User.builder().email( "jin@test.com" ).roleType( RoleType.USER ).socialType( SocialType.GOOGLE ).build();
        when( userRepository.findByEmail( anyString() ) ).thenReturn( Optional.of( userJoinedByForm ) );
        when( userRepository.save( any( User.class ) ) ).thenReturn( userWhoTriedSocialLogin );

        OAuth2UserRequest googleOAuth2UserRequest = OAuth2UserRequestFactory.ofGoogle();
        DefaultOAuth2User googleOAuth2User = OAuth2UserFactory.ofGoogle()
                .addAttribute( "email", userWhoTriedSocialLogin.getEmail() ).build();
        when( delegator.loadUser( googleOAuth2UserRequest ) ).thenReturn( googleOAuth2User );

        //when
        assertThrows( DuplicatedEmailException.class, () -> oAuth2LoginService.loadUser( googleOAuth2UserRequest ) );
    }

    @Test
    @DisplayName( "DB에 중복되는 이메일을 가지고 있는 다른 타입의 소셜 유저가 있는 경우에는 소셜 로그인 실패" )
    public void oauth2LoginDuplicatedEmailSocial() {
        //given
        User userJoinedBySocial = User.builder().id( 2L ).email( "social@test.com" ).roleType( RoleType.USER ).socialType( SocialType.FACEBOOK ).build();
        User userWhoTriedSocialLogin = User.builder().email( "jin@test.com" ).roleType( RoleType.USER ).socialType( SocialType.GOOGLE ).build();
        when( userRepository.findByEmail( anyString() ) ).thenReturn( Optional.of( userJoinedBySocial ) );
        when( userRepository.save( any( User.class ) ) ).thenReturn( userWhoTriedSocialLogin );

        OAuth2UserRequest googleOAuth2UserRequest = OAuth2UserRequestFactory.ofGoogle();
        DefaultOAuth2User googleOAuth2User = OAuth2UserFactory.ofGoogle()
                .addAttribute( "email", userWhoTriedSocialLogin.getEmail() ).build();
        when( delegator.loadUser( googleOAuth2UserRequest ) ).thenReturn( googleOAuth2User );

        //when
        assertThrows( DuplicatedEmailException.class, () -> oAuth2LoginService.loadUser( googleOAuth2UserRequest ) );
    }

    @Test
    @DisplayName(
        "동일한 계정으로 소셜 로그인 재시도시 필드가 없데이트 된다. " +
        "NullPointerException 이 발생한다면 User 의 change 메소드가 호출된 것으로 생각할 수 있다. " +
        "따라서 DummyOAuth2User 에 변경될 필드의 값을 비워둔다."
    )
    public void retryOauth2Login() {
        //given
        User userWhoTriedSocialLogin = User.builder().email( "jin@test.com" ).roleType( RoleType.USER ).socialType( SocialType.GOOGLE ).build();
        when( userRepository.findByEmail( anyString() ) ).thenReturn( Optional.of( userWhoTriedSocialLogin ) );
        when( userRepository.save( any( User.class ) ) ).thenReturn( userWhoTriedSocialLogin );

        OAuth2UserRequest googleOAuth2UserRequest = OAuth2UserRequestFactory.ofGoogle();
        DefaultOAuth2User googleOAuth2User = OAuth2UserFactory.ofGoogle()
                .addAttribute( "email", userWhoTriedSocialLogin.getEmail() ).build();
        when( delegator.loadUser( googleOAuth2UserRequest ) ).thenReturn( googleOAuth2User );

        //when
        assertThrows( NullPointerException.class, () -> oAuth2LoginService.loadUser( googleOAuth2UserRequest ) );
    }

}
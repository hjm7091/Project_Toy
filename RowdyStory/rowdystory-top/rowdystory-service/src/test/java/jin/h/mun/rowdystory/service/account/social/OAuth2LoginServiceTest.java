package jin.h.mun.rowdystory.service.account.social;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.exception.account.DuplicatedEmailException;
import jin.h.mun.rowdystory.service.account.social.factory.OAuth2UserFactory;
import jin.h.mun.rowdystory.service.account.social.factory.OAuth2UserRequestFactory;
import jin.h.mun.rowdystory.service.account.social.user.RowdyOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith( SpringExtension.class )
class OAuth2LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> delegator;

    @InjectMocks
    private OAuth2LoginService oAuth2LoginService;

    @Test
    @DisplayName( "defaultOAuth2UserService 메서드를 호출하면 DefaultOAuth2UserService 가 반환 되어야함." )
    public void defaultOAuth2UserService() {
        //given
        DefaultOAuth2UserService defaultOAuth2UserService = oAuth2LoginService.defaultOAuth2UserService();

        //then
        assertThat( defaultOAuth2UserService ).isNotNull();
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
        assertThat( oAuth2User.getClass() ).isEqualTo( RowdyOAuth2User.class );
    }

    @Test
    @DisplayName( "DB에 중복되는 이메일을 가지고 있는 일반 유저가 있는 경우에는 소셜 로그인 실패" )
    public void oauth2LoginDuplicatedEmailByForm() {
        //given
        User userJoinedByForm = User.builder().id( 1L ).email( "form@test.com" ).roleType( RoleType.USER ).build();
        User userWhoTriedSocialLogin = User.builder().email( "jin@test.com" ).roleType( RoleType.USER ).socialType( SocialType.GOOGLE ).build();
        when( userRepository.findByEmail( anyString() ) ).thenReturn( Optional.of( userJoinedByForm ) );

        OAuth2UserRequest googleOAuth2UserRequest = OAuth2UserRequestFactory.ofGoogle();
        DefaultOAuth2User googleOAuth2User = OAuth2UserFactory.ofGoogle()
                .addAttribute( "email", userWhoTriedSocialLogin.getEmail() ).build();
        when( delegator.loadUser( googleOAuth2UserRequest ) ).thenReturn( googleOAuth2User );

        //when
        assertThrows( DuplicatedEmailException.class, () -> oAuth2LoginService.loadUser( googleOAuth2UserRequest ) );
    }

    @Test
    @DisplayName( "DB에 중복되는 이메일을 가지고 있는 다른 타입의 소셜 유저가 있는 경우에는 소셜 로그인 실패" )
    public void oauth2LoginDuplicatedEmailBySocial() {
        //given
        User userJoinedByFacebook = User.builder().id( 2L ).email( "social@test.com" ).roleType( RoleType.USER ).socialType( SocialType.FACEBOOK ).build();
        User userWhoTriedSocialLogin = User.builder().email( "jin@test.com" ).roleType( RoleType.USER ).socialType( SocialType.GOOGLE ).build();
        when( userRepository.findByEmail( anyString() ) ).thenReturn( Optional.of( userJoinedByFacebook ) );

        OAuth2UserRequest googleOAuth2UserRequest = OAuth2UserRequestFactory.ofGoogle();
        DefaultOAuth2User googleOAuth2User = OAuth2UserFactory.ofGoogle()
                .addAttribute( "email", userWhoTriedSocialLogin.getEmail() ).build();
        when( delegator.loadUser( googleOAuth2UserRequest ) ).thenReturn( googleOAuth2User );

        //when
        assertThrows( DuplicatedEmailException.class, () -> oAuth2LoginService.loadUser( googleOAuth2UserRequest ) );
    }

    @Test
    @DisplayName( "동일한 계정으로 소셜 로그인 재시도시 필드가 없데이트 된다. " )
    public void retryOauth2LoginWithSameSocialAndSameEmail() {
        //given
        String userNameBefore = "jin", pictureBefore = "picture1";
        User userWhoTriedSocialLogin = User.builder().email( "jin@test.com" ).userName( userNameBefore ).picture( pictureBefore )
                .roleType( RoleType.USER ).socialType( SocialType.GOOGLE ).build();
        when( userRepository.findByEmail( anyString() ) ).thenReturn( Optional.of( userWhoTriedSocialLogin ) );

        String userNameAfter = "hak", pictureAfter = "picture2";
        OAuth2UserRequest googleOAuth2UserRequest = OAuth2UserRequestFactory.ofGoogle();
        DefaultOAuth2User googleOAuth2User = OAuth2UserFactory.ofGoogle()
                .addAttribute( "email", userWhoTriedSocialLogin.getEmail() )
                .addAttribute( "name", userNameAfter )
                .addAttribute( "picture", pictureAfter ).build();
        when( delegator.loadUser( googleOAuth2UserRequest ) ).thenReturn( googleOAuth2User );

        //when
        OAuth2User oAuth2User = oAuth2LoginService.loadUser( googleOAuth2UserRequest );

        //then
        assertThat( userWhoTriedSocialLogin.getUserName() ).isEqualTo( userNameAfter );
        assertThat( userWhoTriedSocialLogin.getPicture() ).isEqualTo( pictureAfter );
    }

}
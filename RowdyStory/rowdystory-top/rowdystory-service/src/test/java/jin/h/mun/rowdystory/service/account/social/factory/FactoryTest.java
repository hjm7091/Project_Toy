package jin.h.mun.rowdystory.service.account.social.factory;

import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FactoryTest {

    @Test
    @DisplayName( "테스트에 사용할 oAuth2UserRequest 의 값이 정상적인지 확인" )
    public void dummyOauth2UserRequest() {
        //given
        OAuth2UserRequest googleOAuth2UserRequest = OAuth2UserRequestFactory.ofGoogle();
        OAuth2UserRequest facebookOAuth2UserRequest = OAuth2UserRequestFactory.ofFacebook();
        OAuth2UserRequest kakaoOAuth2UserRequest = OAuth2UserRequestFactory.ofKakao();
        OAuth2UserRequest naverOAuth2UserRequest = OAuth2UserRequestFactory.ofNaver();

        //when
        String googleRegistrationId = googleOAuth2UserRequest.getClientRegistration().getRegistrationId();
        String googleUserNameAttributeName = googleOAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        String facebookRegistrationId = facebookOAuth2UserRequest.getClientRegistration().getRegistrationId();
        String facebookUserNameAttributeName = facebookOAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        String kakaoRegistrationId = kakaoOAuth2UserRequest.getClientRegistration().getRegistrationId();
        String kakaoUserNameAttributeName = kakaoOAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        String naverRegistrationId = naverOAuth2UserRequest.getClientRegistration().getRegistrationId();
        String naverUserNameAttributeName = naverOAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        //then
        assertThat( googleRegistrationId ).isEqualTo( SocialType.GOOGLE.getName() );
        assertThat( googleUserNameAttributeName ).isEqualTo( "sub" );
        assertThat( facebookRegistrationId ).isEqualTo( SocialType.FACEBOOK.getName() );
        assertThat( facebookUserNameAttributeName ).isEqualTo( "id" );
        assertThat( kakaoRegistrationId ).isEqualTo( SocialType.KAKAO.getName() );
        assertThat( kakaoUserNameAttributeName ).isEqualTo( "id" );
        assertThat( naverRegistrationId ).isEqualTo( SocialType.NAVER.getName() );
        assertThat( naverUserNameAttributeName ).isEqualTo( "response" );
    }

    @Test
    @DisplayName( "테스트에 사용할 oAuth2User 의 값이 정상적인지 확인" )
    public void oAuth2User() {
        //given
        DefaultOAuth2User googleOAuth2User = OAuth2UserFactory.ofGoogle()
                .addAttribute( "email", "test@test.com" ).build();
        DefaultOAuth2User facebookOAuth2User = OAuth2UserFactory.ofFacebook()
                .addAttribute( "email", "test@test.com" ).build();
        DefaultOAuth2User kakaoOAuth2User = OAuth2UserFactory.ofKakao()
                .addAttribute( "email", "test@test.com" ).build();
        DefaultOAuth2User naverOAuth2User = OAuth2UserFactory.ofNaver()
                .addAttribute( "email", "test@test.com" ).build();

        //when
        Map<String, Object> googleAttributes = googleOAuth2User.getAttributes();
        Map<String, Object> facebookAttributes = facebookOAuth2User.getAttributes();
        Map<String, Object> kakaoAttributes = kakaoOAuth2User.getAttributes();
        Map<String, Object> naverAttributes = naverOAuth2User.getAttributes();

        //then
        assertThat( googleAttributes.size() ).isEqualTo( 2 );
        assertThat( googleAttributes.containsKey( "sub" ) ).isTrue();
        assertThat( facebookAttributes.size() ).isEqualTo( 2 );
        assertThat( facebookAttributes.containsKey( "id" ) ).isTrue();
        assertThat( kakaoAttributes.size() ).isEqualTo( 2 );
        assertThat( kakaoAttributes.containsKey( "id" ) ).isTrue();
        assertThat( naverAttributes.size() ).isEqualTo( 2 );
        assertThat( naverAttributes.containsKey( "response" ) ).isTrue();
    }

}

package jin.h.mun.rowdystory.service.account.social;

import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.service.account.social.dummy.DummyOAuth2User;
import jin.h.mun.rowdystory.service.account.social.dummy.DummyOAuth2UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DummyTest {

    @Test
    @DisplayName( "테스트에 사용할 DummyOAuth2UserRequest 의 값이 정상적인지 확인" )
    public void dummyOauth2UserRequest() {
        //given
        OAuth2UserRequest googleOAuth2UserRequest = DummyOAuth2UserRequest.ofGoogle();
        OAuth2UserRequest facebookOAuth2UserRequest = DummyOAuth2UserRequest.ofFacebook();
        OAuth2UserRequest kakaoOAuth2UserRequest = DummyOAuth2UserRequest.ofKakao();
        OAuth2UserRequest naverOAuth2UserRequest = DummyOAuth2UserRequest.ofNaver();

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
    @DisplayName( "테스트에 사용할 DummyOAuth2User 의 값이 정상적인지 확인" )
    public void dummyOAuth2User() {
        //given
        DummyOAuth2User googleOAuth2User = DummyOAuth2User.ofGoogleWithEmail( "test@test.com" );
        DummyOAuth2User facebookOAuth2User = DummyOAuth2User.ofFacebookWithEmail( "test@test.com" );
        DummyOAuth2User kakaoOAuth2User = DummyOAuth2User.ofKakaoWithEmail( "test@test.com" );
        DummyOAuth2User naverOAuth2User = DummyOAuth2User.ofNaverWithEmail( "test@test.com" );

        //when
        Map<String, Object> googleAttributes = googleOAuth2User.getAttributes();
        Map<String, Object> facebookAttributes = facebookOAuth2User.getAttributes();
        Map<String, Object> kakaoAttributes = kakaoOAuth2User.getAttributes();
        Map<String, Object> naverAttributes = naverOAuth2User.getAttributes();

        System.out.println( googleAttributes );

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

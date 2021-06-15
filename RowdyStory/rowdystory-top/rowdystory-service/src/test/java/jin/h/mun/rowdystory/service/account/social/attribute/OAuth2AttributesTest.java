package jin.h.mun.rowdystory.service.account.social.attribute;

import jin.h.mun.rowdystory.service.account.social.attribute.OAuth2Attributes.InvalidAttributesException;
import jin.h.mun.rowdystory.service.account.social.factory.OAuth2UserRequestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OAuth2AttributesTest {

    @Test
    @DisplayName( "Naver OAuth2UserRequest -> OAuth2Attributes 변환 테스트, 비정상 attributes" )
    public void ofNaverAbnormalAttributes() {
        //given
        OAuth2UserRequest oAuth2UserRequest = OAuth2UserRequestFactory.ofNaver();
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = Collections.emptyMap();

        //when
        assertThrows( InvalidAttributesException.class, () -> OAuth2Attributes.of( registrationId, userNameAttributeName, attributes ) );
    }

    @Test
    @DisplayName( "Naver OAuth2UserRequest -> OAuth2Attributes 변환 테스트, 정상 attributes" )
    public void ofNaverNormalAttributes() {
        //given
        OAuth2UserRequest oAuth2UserRequest = OAuth2UserRequestFactory.ofNaver();
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put( userNameAttributeName, Collections.emptyMap() );

        //when
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of( registrationId, userNameAttributeName, attributes );

        //then
        assertThat( oAuth2Attributes.getRegistrationId() ).isEqualTo( registrationId );
        assertThat( oAuth2Attributes.getNameAttributeKey() ).isEqualTo( userNameAttributeName );
    }

    @Test
    @DisplayName( "Kakao OAuth2UserRequest -> OAuth2Attributes 변환 테스트, 비정상 attributes" )
    public void ofKakaoAbnormalAttributes() {
        //given
        OAuth2UserRequest oAuth2UserRequest = OAuth2UserRequestFactory.ofKakao();
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = Collections.emptyMap();

        //when
        assertThrows( InvalidAttributesException.class, () -> OAuth2Attributes.of( registrationId, userNameAttributeName, attributes ) );
    }

    @Test
    @DisplayName( "Kakao OAuth2UserRequest -> OAuth2Attributes 변환 테스트, 정상 attributes" )
    public void ofKakaoNormalAttributes() {
        //given
        OAuth2UserRequest oAuth2UserRequest = OAuth2UserRequestFactory.ofKakao();
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = new HashMap<>();
        Map<String, Object> kakaoAccount = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        kakaoAccount.put( "profile", Collections.emptyMap() );
        attributes.put( "kakao_account", kakaoAccount );
        attributes.put( "properties", properties );

        //when
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of( registrationId, userNameAttributeName, attributes );

        //then
        assertThat( oAuth2Attributes.getRegistrationId() ).isEqualTo( registrationId );
        assertThat( oAuth2Attributes.getNameAttributeKey() ).isEqualTo( userNameAttributeName );
    }
}
package jin.h.mun.rowdystory.service.account.social.dummy;

import jin.h.mun.rowdystory.service.account.social.provider.CustomOAuth2Provider;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.time.Instant;

public class DummyOAuth2UserRequest extends OAuth2UserRequest {

    private static final OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken( OAuth2AccessToken.TokenType.BEARER, "token", Instant.MIN, Instant.MAX );

    private DummyOAuth2UserRequest(ClientRegistration clientRegistration, OAuth2AccessToken accessToken ) {
        super( clientRegistration, accessToken );
    }

    public static OAuth2UserRequest ofGoogle() {
        ClientRegistration clientRegistration = CommonOAuth2Provider.GOOGLE.getBuilder( "google" )
                .clientId( "google.oauth2.client-id" )
                .clientSecret( "google.oauth2.client-secret" )
                .scope( "email", "profile" )
                .build();

        return new DummyOAuth2UserRequest( clientRegistration, oAuth2AccessToken );
    }

    public static OAuth2UserRequest ofFacebook() {
        ClientRegistration clientRegistration = CommonOAuth2Provider.FACEBOOK.getBuilder( "facebook" )
                .clientId( "facebook.oauth2.client-id" )
                .clientSecret( "facebook.oauth2.client-secret" )
                .scope( "email", "public_profile" )
                .build();

        return new DummyOAuth2UserRequest( clientRegistration, oAuth2AccessToken );
    }

    public static OAuth2UserRequest ofKakao() {
        ClientRegistration clientRegistration = CustomOAuth2Provider.KAKAO.getBuilder( "kakao" )
                .clientId( "kakao.oauth2.client-id" )
                .clientSecret( "kakao" )
                .jwkSetUri( "kakao" )
                .build();

        return new OAuth2UserRequest( clientRegistration, oAuth2AccessToken );
    }

    public static OAuth2UserRequest ofNaver() {
        ClientRegistration clientRegistration = CustomOAuth2Provider.NAVER.getBuilder( "naver" )
                .clientId( "naver.oauth2.client-id" )
                .clientSecret( "naver" )
                .jwkSetUri( "naver" )
                .build();

        return new OAuth2UserRequest( clientRegistration, oAuth2AccessToken );
    }
}

package jin.h.mun.knutalk.social.config;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jin.h.mun.knutalk.social.provider.CustomOAuth2Provider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SocialConfig {
	
	@Value( "${kakao.oauth2.client-id}" )
	private String kakaoClientId;
	
	@Value( "${naver.oauth2.client-id}" )
	private String naverClientId;

	@Value( "${naver.oauth2.client-secret}" )
	private String naverclientSecret;
	
	@Bean
	public ClientRegistrationRepository clientRegistrationRepository( final OAuth2ClientProperties oAuth2ClientProperties ) {
		
        List<ClientRegistration> clientRegistrations = oAuth2ClientProperties.getRegistration().keySet().stream()
	        .map( client -> getRegistration( oAuth2ClientProperties, client ) )
	        .filter( Objects::nonNull )
	        .collect( Collectors.toList() );
	
        // kakao client
    	ClientRegistration kakaoClientRegistration = CustomOAuth2Provider.KAKAO.getBuilder( "kakao" )
    			.clientId( kakaoClientId )
    			.clientSecret( "kakao" ) //필요없는 값이지만 null이면 실행이 안되도록 설정되어 있음
    			.jwkSetUri( "kakao" ) //필요없는 값이지만 null이면 실행이 안되도록 설정되어 있음
    			.build();
    	
    	clientRegistrations.add( kakaoClientRegistration );
    	
    	// naver client
    	ClientRegistration naverClientRegistration = CustomOAuth2Provider.NAVER.getBuilder( "naver" )
    			.clientId( naverClientId )
    			.clientSecret( naverclientSecret )
    			.jwkSetUri( "naver" )
    			.build();
    	
    	clientRegistrations.add( naverClientRegistration );
    	
		log.info( "clientRegistrations size : {}", clientRegistrations.size() );
	
		return new InMemoryClientRegistrationRepository( clientRegistrations );
	}

	private ClientRegistration getRegistration( final OAuth2ClientProperties clientProperties, final String client ) {
        if ( client.equals( "google" ) ) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get( "google" );
            return CommonOAuth2Provider.GOOGLE.getBuilder( client )
                    .clientId( registration.getClientId() )
                    .clientSecret( registration.getClientSecret() )
                    .scope( "email", "profile" )
                    .build();
        } else if ( client.equals( "facebook" ) ) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get( "facebook" );
            return CommonOAuth2Provider.FACEBOOK.getBuilder( client )
                    .clientId( registration.getClientId() )
                    .clientSecret( registration.getClientSecret() )
                    .userInfoUri( "https://graph.facebook.com/me?fields=id,name,email,link" )
                    .scope( "email", "public_profile" )
                    .build();
        }
        
        throw new IllegalArgumentException( "invalid parameter : " + client );
    }
	
}

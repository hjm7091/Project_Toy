package jin.h.mun.rowdystory.service.account.social.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith( SpringExtension.class )
class SocialConfigTest {

    @Mock
    private OAuth2ClientProperties oAuth2ClientProperties;

    @InjectMocks
    private SocialConfig socialConfig;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField( socialConfig, "kakaoClientId", "kakao.oauth2.client-id" );
        ReflectionTestUtils.setField( socialConfig, "naverClientId", "naver.oauth2.client-id" );
        ReflectionTestUtils.setField( socialConfig, "naverClientSecret", "naver.oauth2.client-secret" );
    }

    @Test
    @DisplayName( "clientRegistrationRepository() 메서드 테스트, clientRegistrations size 가 4인지 확인" )
    public void clientRegistrationRepository() {
        //given
        OAuth2ClientProperties.Registration googleRegistration = new OAuth2ClientProperties.Registration();
        googleRegistration.setClientId( "google.oauth2.client-id" );
        googleRegistration.setClientSecret( "google.oauth2.client-secret" );
        OAuth2ClientProperties.Registration facebookRegistration = new OAuth2ClientProperties.Registration();
        facebookRegistration.setClientId( "facebook.oauth2.client-id" );
        facebookRegistration.setClientSecret( "facebook.oauth2.client-secret" );

        Map<String, OAuth2ClientProperties.Registration> registrationMap = new HashMap<>();
        registrationMap.put( "google", googleRegistration );
        registrationMap.put( "facebook", facebookRegistration );
        when( oAuth2ClientProperties.getRegistration() ).thenReturn( registrationMap );

        //when
        ClientRegistrationRepository clientRegistrationRepository = socialConfig.clientRegistrationRepository();
        ClientRegistration googleClientRegistration = clientRegistrationRepository.findByRegistrationId( "google" );
        ClientRegistration facebookClientRegistration = clientRegistrationRepository.findByRegistrationId( "facebook" );
        ClientRegistration kakaoClientRegistration = clientRegistrationRepository.findByRegistrationId( "kakao" );
        ClientRegistration naverClientRegistration = clientRegistrationRepository.findByRegistrationId( "naver" );

        //then
        assertThat( clientRegistrationRepository.getClass() ).isEqualTo( InMemoryClientRegistrationRepository.class );
        assertThat( googleClientRegistration ).isNotNull();
        assertThat( facebookClientRegistration ).isNotNull();
        assertThat( kakaoClientRegistration ).isNotNull();
        assertThat( naverClientRegistration ).isNotNull();
    }

    @Test
    @DisplayName( "registrationMap 에 'google', 'facebook' 외의 다른 키 값 존재시 에러 발생" )
    public void clientRegistrationRepositoryWithInvalidKey() {
        //given
        Map<String, OAuth2ClientProperties.Registration> registrationMap = new HashMap<>();
        registrationMap.put( "github", new OAuth2ClientProperties.Registration() );
        when( oAuth2ClientProperties.getRegistration() ).thenReturn( registrationMap );

        //when
        assertThrows( InvalidParameterException.class, () -> socialConfig.clientRegistrationRepository() );
    }

}
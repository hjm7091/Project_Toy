package jin.h.mun.knutalk.social.service;

import jin.h.mun.knutalk.data.repository.user.UserRepository;
import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.domain.account.enums.SocialType;
import jin.h.mun.knutalk.social.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser( final OAuth2UserRequest userRequest ) throws OAuth2AuthenticationException {

        log.info( "[CustomOAuth2UserService] accessToken : {}, clientRegistration : {}", userRequest.getAccessToken(), userRequest.getClientRegistration() );

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser( userRequest );

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes oAuthAttributes = OAuthAttributes.of( registrationId, userNameAttributeName, oAuth2User.getAttributes() );

        User user = saveOrUpdate( oAuthAttributes );
        httpSession.setAttribute( "user", new SessionUser( user ) );

        return new DefaultOAuth2User(
                Collections.singleton( new SimpleGrantedAuthority( user.getRoleType().getName() ) ),
                oAuthAttributes.getAttributes(),
                oAuthAttributes.getNameAttributeKey() );
    }

    private User saveOrUpdate( final OAuthAttributes attributes ) {

        String email = attributes.getEmail();
        SocialType socialType = SocialType.getSocialType( attributes.getRegistrationId() );

        User user = userRepository.findByEmailAndSocialType( email, socialType )
                .map( entity -> entity.changeUserName( attributes.getName() ).changePicture( attributes.getPicture() ) )
                .orElse( attributes.toUserEntity() );

        return userRepository.save( user );
    }

}

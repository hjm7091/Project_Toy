package jin.h.mun.rowdystory.service.account.social;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.exception.account.DuplicatedEmailException;
import jin.h.mun.rowdystory.exception.account.ErrorMessage;
import jin.h.mun.rowdystory.service.account.social.attribute.OAuth2Attributes;
import jin.h.mun.rowdystory.service.account.social.user.RowdyOAuth2User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
public class OAuth2LoginService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier( "defaultOAuth2UserService" )
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegator;

    @Bean
    public DefaultOAuth2UserService defaultOAuth2UserService() {
        return new DefaultOAuth2UserService();
    }

    @Override
    public OAuth2User loadUser( final OAuth2UserRequest userRequest ) throws OAuth2AuthenticationException {

        log.info( "[CustomOAuth2UserService] accessToken : {}, clientRegistration : {}", userRequest.getAccessToken(), userRequest.getClientRegistration() );

        OAuth2User oAuth2User = delegator.loadUser( userRequest );

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of( registrationId, userNameAttributeName, oAuth2User.getAttributes() );

        User user = saveOrUpdate( oAuth2Attributes );

        return new RowdyOAuth2User(
                Collections.singleton( new SimpleGrantedAuthority( user.getRoleType().getRoleName() ) ),
                oAuth2Attributes );
    }

    private User saveOrUpdate( final OAuth2Attributes attributes ) {

        String email = attributes.getEmail();
        SocialType socialType = SocialType.getSocialTypeFrom( attributes.getRegistrationId() );

        Optional<User> userOpt = userRepository.findByEmail( email );

        if ( userOpt.isPresent() ) {
            User user = userOpt.get();

            throwExceptionByUserCondition( user, socialType );

            return user.changeUserName( attributes.getName() ).changePicture( attributes.getPicture() );
        }

        return userRepository.save( attributes.toUserEntity() );
    }

    private void throwExceptionByUserCondition( User findUser, SocialType acquiredSocialType ) {
        if ( !findUser.isSocialUser() || findUser.getSocialType() != acquiredSocialType ) {
            String message = ErrorMessage.EMAIL_ALREADY_EXIST.getMessage();
            throw new DuplicatedEmailException( message );
        }
    }

}

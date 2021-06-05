package jin.h.mun.rowdystory.service.account.social.dummy;

import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DummyOAuth2User extends DefaultOAuth2User {

    private static final Collection<? extends GrantedAuthority> authorities = Collections.singleton( new SimpleGrantedAuthority( RoleType.USER.getRoleName() ) );

    private DummyOAuth2User( Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey ) {
        super( authorities, attributes, nameAttributeKey );
    }

    public static DummyOAuth2User ofGoogleWithEmail( @NonNull String email ) {

        Map<String, Object> attributes = new HashMap<>();

        String userNameAttributeName = DummyOAuth2UserRequest.ofGoogle().getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        attributes.put( userNameAttributeName, userNameAttributeName );
        attributes.put( "email", email );

        return new DummyOAuth2User( authorities, attributes, userNameAttributeName );
    }

    public static DummyOAuth2User ofFacebookWithEmail( @NonNull String email ) {

        Map<String, Object> attributes = new HashMap<>();

        String userNameAttributeName = DummyOAuth2UserRequest.ofFacebook().getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        attributes.put( userNameAttributeName, userNameAttributeName );
        attributes.put( "email", email );

        return new DummyOAuth2User( authorities, attributes, userNameAttributeName );
    }

    public static DummyOAuth2User ofKakaoWithEmail( @NonNull String email ) {

        Map<String, Object> attributes = new HashMap<>();

        String userNameAttributeName = DummyOAuth2UserRequest.ofKakao().getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        attributes.put( userNameAttributeName, userNameAttributeName );
        attributes.put( "email", email );

        return new DummyOAuth2User( authorities, attributes, userNameAttributeName );
    }

    public static DummyOAuth2User ofNaverWithEmail( @NonNull String email ) {

        Map<String, Object> attributes = new HashMap<>();

        String userNameAttributeName = DummyOAuth2UserRequest.ofNaver().getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        attributes.put( userNameAttributeName, userNameAttributeName );
        attributes.put( "email", email );

        return new DummyOAuth2User( authorities, attributes, userNameAttributeName );
    }
}

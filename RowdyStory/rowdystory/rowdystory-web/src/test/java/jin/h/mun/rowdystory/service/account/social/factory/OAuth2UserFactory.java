package jin.h.mun.rowdystory.service.account.social.factory;

import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OAuth2UserFactory {

    private static final Collection<? extends GrantedAuthority> authorities = Collections.singleton( new SimpleGrantedAuthority( RoleType.USER.getRoleName() ) );

    public static GoogleBuilder ofGoogle() {
        return new GoogleBuilder();
    }

    public static FacebookBuilder ofFacebook() {
        return new FacebookBuilder();
    }

    public static KakaoBuilder ofKakao() {
        return new KakaoBuilder();
    }

    public static NaverBuilder ofNaver() {
        return new NaverBuilder();
    }

    public static class GoogleBuilder extends Builder {

        @Override
        public String getUserNameAttributeName() {
            return OAuth2UserRequestFactory.ofGoogle().getClientRegistration()
                    .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        }

    }

    public static class FacebookBuilder extends Builder {

        @Override
        public String getUserNameAttributeName() {
            return OAuth2UserRequestFactory.ofFacebook().getClientRegistration()
                    .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        }

    }

    public static class KakaoBuilder extends Builder {

        @Override
        public String getUserNameAttributeName() {
            return OAuth2UserRequestFactory.ofKakao().getClientRegistration()
                    .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        }

    }

    public static class NaverBuilder extends Builder {

        @Override
        public String getUserNameAttributeName() {
            return OAuth2UserRequestFactory.ofNaver().getClientRegistration()
                    .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        }

    }

    public static abstract class Builder {

        protected final Map<String, Object> attributes = new HashMap<>();

        public Builder addAttribute( String key, Object value ) {
            this.attributes.put( key, value );
            return this;
        }

        public DefaultOAuth2User build() {
            String userNameAttributeName = getUserNameAttributeName();

            this.attributes.put( userNameAttributeName, userNameAttributeName );

            return new DefaultOAuth2User( authorities, this.attributes, userNameAttributeName );
        }

        public abstract String getUserNameAttributeName();
    }

}

package jin.h.mun.rowdystory.service.account.social.user;

import jin.h.mun.rowdystory.service.account.social.attribute.OAuth2Attributes;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;

@Getter
public class RowdyOAuth2User extends DefaultOAuth2User {

    private final OAuth2Attributes oAuth2Attributes;

    public RowdyOAuth2User( Collection<? extends GrantedAuthority> authorities, OAuth2Attributes oAuth2Attributes ) {
        super( authorities, oAuth2Attributes.getAttributes(), oAuth2Attributes.getNameAttributeKey() );
        this.oAuth2Attributes = oAuth2Attributes;
    }
}

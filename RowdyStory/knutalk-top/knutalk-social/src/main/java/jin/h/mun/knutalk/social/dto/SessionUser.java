package jin.h.mun.knutalk.social.dto;

import jin.h.mun.knutalk.domain.account.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
    private String email;
    private String picture;

    public SessionUser( final User user ) {
    	if ( user != null ) {
            this.name = user.getUserName();
            this.email = user.getEmail();
            this.picture = user.getPicture();
    	}
    }
}

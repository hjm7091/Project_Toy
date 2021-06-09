package jin.h.mun.rowdystory.exception.account;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AlreadyRegisteredSocialException extends UsernameNotFoundException {

    public AlreadyRegisteredSocialException( String msg, Throwable t ) { super( msg, t ); }

    public AlreadyRegisteredSocialException( String msg ) { super( msg ); }
}

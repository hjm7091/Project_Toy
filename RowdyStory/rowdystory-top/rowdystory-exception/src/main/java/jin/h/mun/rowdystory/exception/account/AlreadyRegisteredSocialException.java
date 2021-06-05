package jin.h.mun.rowdystory.exception.account;

import org.springframework.security.core.AuthenticationException;

public class AlreadyRegisteredSocialException extends AuthenticationException {

    public AlreadyRegisteredSocialException( String msg, Throwable t ) { super( msg, t ); }

    public AlreadyRegisteredSocialException( String msg ) { super( msg ); }
}

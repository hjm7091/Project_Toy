package jin.h.mun.rowdystory.exception.account;

import org.springframework.security.core.AuthenticationException;

public class DuplicatedEmailException extends AuthenticationException {

    public DuplicatedEmailException( String message ) { super( message ); }

    public DuplicatedEmailException( String message, Throwable cause ) { super( message, cause ); }
}

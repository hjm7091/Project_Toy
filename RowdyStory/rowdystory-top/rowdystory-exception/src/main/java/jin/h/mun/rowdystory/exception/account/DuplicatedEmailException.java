package jin.h.mun.rowdystory.exception.account;

public class DuplicatedEmailException extends RuntimeException {

    public DuplicatedEmailException( String message ) { super( message ); }

    public DuplicatedEmailException( String message, Throwable cause ) { super( message, cause ); }
}

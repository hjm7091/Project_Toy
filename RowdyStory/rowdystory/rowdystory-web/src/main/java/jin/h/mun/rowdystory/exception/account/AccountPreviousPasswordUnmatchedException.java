package jin.h.mun.rowdystory.exception.account;

public class AccountPreviousPasswordUnmatchedException extends RuntimeException {

    public AccountPreviousPasswordUnmatchedException() {
        super();
    }

    public AccountPreviousPasswordUnmatchedException( String message ) {
        super( message );
    }

    public AccountPreviousPasswordUnmatchedException( String message, Throwable cause ) {
        super( message, cause );
    }

    public AccountPreviousPasswordUnmatchedException( Throwable cause ) {
        super( cause );
    }

    protected AccountPreviousPasswordUnmatchedException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}

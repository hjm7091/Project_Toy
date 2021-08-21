package jin.h.mun.rowdystory.exception.account;

public class SocialAccountUnmodifiableException extends RuntimeException {

    public SocialAccountUnmodifiableException() {
        super();
    }

    public SocialAccountUnmodifiableException(String message ) {
        super( message );
    }

    public SocialAccountUnmodifiableException(String message, Throwable cause ) {
        super( message, cause );
    }

    public SocialAccountUnmodifiableException(Throwable cause ) {
        super( cause );
    }

    protected SocialAccountUnmodifiableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}

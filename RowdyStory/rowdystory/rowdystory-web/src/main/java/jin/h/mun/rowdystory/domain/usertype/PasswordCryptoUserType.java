package jin.h.mun.rowdystory.domain.usertype;

import lombok.Getter;

public class PasswordCryptoUserType extends AbstractCryptoUserType {

    @Getter
    private static final PasswordCryptoUserType INSTANCE = new PasswordCryptoUserType();

    private static final String key = "password";

    public PasswordCryptoUserType() {
        super( PasswordCryptoUserType.key );
    }
}

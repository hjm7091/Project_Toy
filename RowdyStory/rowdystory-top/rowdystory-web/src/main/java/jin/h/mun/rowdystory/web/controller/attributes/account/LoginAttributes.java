package jin.h.mun.rowdystory.web.controller.attributes.account;

import jin.h.mun.rowdystory.dto.account.UserLoginRequest;
import jin.h.mun.rowdystory.web.controller.attributes.common.HeaderAttributes;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

public class LoginAttributes extends HeaderAttributes {

    public static final String LOGIN_REQUEST_OBJECT = "loginRequest";
    public static final String LOGIN_URI = "login";
    public static final String LOGIN_ERROR_STRING = "loginError";

    @ModelAttribute( LOGIN_REQUEST_OBJECT )
    public final UserLoginRequest loginRequest() {
        return new UserLoginRequest();
    }

    @ModelAttribute( PasswordAttributes.PASSWORD_URI )
    public final String passwordUri() { return AccountMapping.PASSWORD; }

}

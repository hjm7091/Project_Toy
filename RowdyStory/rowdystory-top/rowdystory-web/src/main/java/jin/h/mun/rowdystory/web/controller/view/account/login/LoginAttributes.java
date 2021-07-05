package jin.h.mun.rowdystory.web.controller.view.account.login;

import jin.h.mun.rowdystory.dto.account.UserLoginRequest;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.register.RegisterAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

public class LoginAttributes {

    public static final String LOGIN_REQUEST_OBJECT = "loginRequest";
    public static final String LOGIN_URI = "login";
    public static final String LOGIN_ERROR_STRING = "loginError";
    public static final String FIND_PASSWORD_URI = "findPassword";

    @ModelAttribute( LOGIN_REQUEST_OBJECT )
    public final UserLoginRequest loginRequest() {
        return new UserLoginRequest();
    }

    @ModelAttribute( LOGIN_URI )
    public final String loginUri() {
        return AccountMapping.LOGIN;
    }

    @ModelAttribute( FIND_PASSWORD_URI )
    public final String findPasswordUri() { return AccountMapping.FIND_PASSWORD; }

    @ModelAttribute( RegisterAttributes.REGISTER_URI )
    public final String registerUri() { return AccountMapping.REGISTER; }
}

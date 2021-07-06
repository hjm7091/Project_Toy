package jin.h.mun.rowdystory.web.controller.attributes.account;

import jin.h.mun.rowdystory.dto.account.UserPasswordRequest;
import jin.h.mun.rowdystory.web.controller.attributes.common.HeaderAttributes;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

public class PasswordAttributes extends HeaderAttributes {

    public static final String PASSWORD_REQUEST_OBJECT = "passwordRequest";
    public static final String PASSWORD_URI = "password";

    @ModelAttribute( PASSWORD_REQUEST_OBJECT )
    public UserPasswordRequest passwordRequest() {
        return new UserPasswordRequest();
    }

    @ModelAttribute( PASSWORD_URI )
    public String passwordUri() {
        return AccountMapping.PASSWORD;
    }
}

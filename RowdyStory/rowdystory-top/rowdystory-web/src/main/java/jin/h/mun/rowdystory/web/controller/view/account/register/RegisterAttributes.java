package jin.h.mun.rowdystory.web.controller.view.account.register;

import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

public class RegisterAttributes {

    public static final String REGISTER_REQUEST_OBJECT = "registerRequest";
    public static final String REGISTER_URI = "register";

    @ModelAttribute( REGISTER_REQUEST_OBJECT )
    public UserRegisterRequest registerRequest() {
        return new UserRegisterRequest();
    }

    @ModelAttribute( REGISTER_URI )
    public String registerUri() {
        return AccountMapping.REGISTER;
    }
}

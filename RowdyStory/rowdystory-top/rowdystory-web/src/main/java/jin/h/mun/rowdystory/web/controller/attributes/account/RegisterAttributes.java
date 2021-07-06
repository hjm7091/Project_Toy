package jin.h.mun.rowdystory.web.controller.attributes.account;

import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.web.controller.attributes.common.HeaderAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

public class RegisterAttributes extends HeaderAttributes {

    public static final String REGISTER_REQUEST_OBJECT = "registerRequest";
    public static final String REGISTER_URI = "register";

    @ModelAttribute( REGISTER_REQUEST_OBJECT )
    public UserRegisterRequest registerRequest() {
        return new UserRegisterRequest();
    }
}

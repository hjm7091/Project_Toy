package jin.h.mun.rowdystory.web.controller.attributes.common;

import jin.h.mun.rowdystory.web.controller.attributes.account.LoginAttributes;
import jin.h.mun.rowdystory.web.controller.attributes.account.RegisterAttributes;
import jin.h.mun.rowdystory.web.controller.attributes.home.HomeAttributes;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.home.HomeResolver.HomeMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

public class HeaderAttributes {

    @ModelAttribute( LoginAttributes.LOGIN_URI )
    public String loginUri() {
        return AccountMapping.LOGIN;
    }

    @ModelAttribute( RegisterAttributes.REGISTER_URI )
    public String registerUri() {
        return AccountMapping.REGISTER;
    }

    @ModelAttribute( HomeAttributes.HOME_URI )
    public String homeUri() {
        return HomeMapping.HOME;
    }

}

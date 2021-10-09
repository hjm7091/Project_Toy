package jin.h.mun.rowdystory.web.controller.view.account.info;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountView;
import jin.h.mun.rowdystory.web.resolver.session.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    @GetMapping( AccountMapping.INFO )
    public String info( @SessionUser UserDTO userDTO ) {

        if ( userDTO == null )
            return AccountView.LOGIN;

        return AccountView.INFO;
    }

}

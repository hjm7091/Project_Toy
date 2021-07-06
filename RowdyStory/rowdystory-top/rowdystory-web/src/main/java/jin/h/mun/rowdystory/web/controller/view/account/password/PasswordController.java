package jin.h.mun.rowdystory.web.controller.view.account.password;

import jin.h.mun.rowdystory.web.controller.attributes.account.PasswordAttributes;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class PasswordController extends PasswordAttributes {

    @GetMapping( AccountMapping.PASSWORD )
    public String password() {
        return AccountView.PASSWORD;
    }

}

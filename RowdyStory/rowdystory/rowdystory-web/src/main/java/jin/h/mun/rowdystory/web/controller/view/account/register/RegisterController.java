package jin.h.mun.rowdystory.web.controller.view.account.register;

import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class RegisterController {

    @GetMapping( AccountMapping.REGISTER )
    public String register() {
        return AccountView.REGISTER;
    }
}

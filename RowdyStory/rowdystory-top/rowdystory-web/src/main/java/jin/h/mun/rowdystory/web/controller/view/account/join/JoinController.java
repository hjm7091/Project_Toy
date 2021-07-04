package jin.h.mun.rowdystory.web.controller.view.account.join;

import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class JoinController {

    @GetMapping( AccountMapping.JOIN )
    public String join() {
        return AccountView.JOIN;
    }
}

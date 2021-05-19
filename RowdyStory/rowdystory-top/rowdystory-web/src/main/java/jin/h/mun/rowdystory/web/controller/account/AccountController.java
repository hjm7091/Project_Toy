package jin.h.mun.rowdystory.web.controller.account;

import jin.h.mun.rowdystory.social.annotation.SocialUser;
import jin.h.mun.rowdystory.social.dto.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccountController {

	@RequestMapping( "/account/login" )
	public String loginForm( @SocialUser SessionUser user ) {
		return "account/login";
	}
}

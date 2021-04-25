package jin.h.mun.knutalk.web.controller;

import jin.h.mun.knutalk.social.annotation.SocialUser;
import jin.h.mun.knutalk.social.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class AccountController {

	@RequestMapping( "/login" )
	public String loginForm( @SocialUser SessionUser user ) {
		return "LoginForm";
	}
}

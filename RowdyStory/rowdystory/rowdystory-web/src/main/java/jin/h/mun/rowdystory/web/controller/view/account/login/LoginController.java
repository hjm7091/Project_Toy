package jin.h.mun.rowdystory.web.controller.view.account.login;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountView;
import jin.h.mun.rowdystory.web.controller.view.home.Home.HomeView;
import jin.h.mun.rowdystory.web.resolver.session.SessionUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static jin.h.mun.rowdystory.web.controller.view.attribute.AttributeKeyAndValueForView.*;

@Slf4j
@Controller
public class LoginController {

	@GetMapping( AccountMapping.LOGIN )
	public String login( @SessionUser UserDTO userDTO ) {

		log.info( "userDTO : {}", userDTO );

		if ( userDTO != null )
			return HomeView.HOME;

		return AccountView.LOGIN;
	}

	@RequestMapping( value = AccountMapping.LOGIN_FAIL, method = { RequestMethod.GET, RequestMethod.POST } )
	public String loginFailure( Model model, String message ) {

		log.info( "login failed. reason : {}", message );

		model.addAttribute( LOGIN_ERROR_MESSAGE.key, message );

		return AccountView.LOGIN;
	}

}

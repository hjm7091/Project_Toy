package jin.h.mun.rowdystory.web.controller.view.account.login;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountView;
import jin.h.mun.rowdystory.web.controller.view.home.HomeResolver.HomeView;
import jin.h.mun.rowdystory.web.session.SessionUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
public class LoginController extends LoginAttributes {

	@GetMapping( AccountMapping.LOGIN )
	public String login( Model model, @SessionUser UserDTO userDTO ) {

		log.info( "userDTO : {}", userDTO );

		if ( userDTO != null )
			return HomeView.HOME;

		return AccountView.LOGIN;
	}

	@RequestMapping( value = AccountMapping.LOGIN_FAIL, method = { RequestMethod.GET, RequestMethod.POST } )
	public String loginFailure( Model model, String message ) {

		log.info( "login failed. reason : {}", message );

		model.addAttribute( LoginAttributes.LOGIN_ERROR_STRING, message );

		return AccountView.LOGIN;
	}

}

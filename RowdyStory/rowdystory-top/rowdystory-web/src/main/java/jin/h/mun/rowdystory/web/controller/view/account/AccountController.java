package jin.h.mun.rowdystory.web.controller.view.account;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.UserLoginRequest;
import jin.h.mun.rowdystory.web.controller.view.home.HomeView;
import jin.h.mun.rowdystory.web.session.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AccountController {

	@GetMapping( AccountView.LOGIN )
	public String login( UserLoginRequest userLoginRequest, @SessionUser UserDTO userDTO ) {

		log.info( "userDTO : {}", userDTO );

		if ( userDTO != null )
			return HomeView.HOME;

		return AccountView.LOGIN;
	}

	@PostMapping( AccountView.LOGIN_FAIL )
	public String formLoginFailure( String message, UserLoginRequest userLoginRequest, BindingResult bindingResult ) {

		log.info( "form login failed. reason : {}", message );

		FieldError fieldError = new FieldError( "message", "email", message );
		bindingResult.addError( fieldError );

		return AccountView.LOGIN;
	}

	@GetMapping( AccountView.LOGIN_FAIL )
	public String oauth2LoginFailure( String message, UserLoginRequest userLoginRequest, BindingResult bindingResult ) {

		log.info( "oauth2 login failed. reason : {}", message );

		FieldError fieldError = new FieldError( "message", "email", message );
		bindingResult.addError( fieldError );

		return AccountView.LOGIN;
	}
}

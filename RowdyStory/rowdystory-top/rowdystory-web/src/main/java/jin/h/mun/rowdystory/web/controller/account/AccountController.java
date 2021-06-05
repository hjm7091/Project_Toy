package jin.h.mun.rowdystory.web.controller.account;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.UserLoginRequest;
import jin.h.mun.rowdystory.service.account.LoginErrorDistinctionService;
import jin.h.mun.rowdystory.web.controller.account.session.SessionUser;
import jin.h.mun.rowdystory.web.controller.home.HomeURL;
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

	private final LoginErrorDistinctionService loginErrorDistinctionService;

	@GetMapping( AccountURL.LOGIN )
	public String login( UserLoginRequest userLoginRequest, @SessionUser UserDTO userDTO ) {

		log.info( "userDTO : {}", userDTO );

		if ( userDTO != null )
			return HomeURL.HOME;

		return AccountURL.LOGIN;
	}

	/*
	 * 이 메소드는 CustomLoginFailureHandler 에 의해서만 호출됨. 따라서 identificationResult 는 반드시 에러가 있어야함.
	 */
	@PostMapping( AccountURL.LOGIN )
	public String loginFailure( UserLoginRequest userLoginRequest, BindingResult bindingResult ) {

		log.info( "userLoginRequest : {}", userLoginRequest );

		FieldError fieldError = loginErrorDistinctionService.distinguish( userLoginRequest );
		bindingResult.addError( fieldError );

		return AccountURL.LOGIN;
	}
}

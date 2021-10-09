package jin.h.mun.rowdystory.web.controller.view.account.login.handler;

import jin.h.mun.rowdystory.exception.account.ErrorMessage;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure( HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                         AuthenticationException e ) throws IOException, ServletException {
        log.info( "exception message : {}, exception class : {}", e.getMessage(), e.getClass().getSimpleName() );

        String message = e.getMessage();

        if ( e instanceof BadCredentialsException )
            message = ErrorMessage.PASSWORD_NOT_MATCH.getMessage();

        String dispatchURL = AccountMapping.LOGIN_FAIL + "?message=" + message;

        httpServletRequest.getRequestDispatcher( dispatchURL ).forward( httpServletRequest, httpServletResponse );
    }
}

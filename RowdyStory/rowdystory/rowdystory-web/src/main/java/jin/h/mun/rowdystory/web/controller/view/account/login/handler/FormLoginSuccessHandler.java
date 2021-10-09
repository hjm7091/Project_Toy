package jin.h.mun.rowdystory.web.controller.view.account.login.handler;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.web.resolver.session.SessionDefine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component
public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess( HttpServletRequest request, HttpServletResponse response, Authentication authentication ) throws IOException, ServletException {

        log.info( "email : {}", authentication.getName() );

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute( SessionDefine.USER.getName(), UserDTO.builder().email( authentication.getName() ).build() );

        super.onAuthenticationSuccess( request, response, authentication );
    }

}

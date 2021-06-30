package jin.h.mun.rowdystory.web.controller.view.account.handler;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.service.account.social.user.RowdyOAuth2User;
import jin.h.mun.rowdystory.web.session.SessionDefine;
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
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess( HttpServletRequest request, HttpServletResponse response, Authentication authentication ) throws IOException, ServletException {

        RowdyOAuth2User rowdyOAuth2User = ( RowdyOAuth2User ) authentication.getPrincipal();

        log.info( "email : {}", rowdyOAuth2User.getOAuth2Attributes().getEmail() );

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute( SessionDefine.USER.getName(), UserDTO.builder().email( rowdyOAuth2User.getOAuth2Attributes().getEmail() ).build() );

        super.onAuthenticationSuccess( request, response, authentication );
    }

}

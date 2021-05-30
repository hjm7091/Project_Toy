package jin.h.mun.rowdystory.web.controller.account.handler;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess( HttpServletRequest request, HttpServletResponse response, Authentication authentication ) throws IOException, ServletException {

        User userDetails = ( User ) authentication.getPrincipal();

        log.info( "userDetails : {}", userDetails );

        Optional<jin.h.mun.rowdystory.domain.account.User> userOpt = userRepository.findByEmail( userDetails.getUsername() );

        jin.h.mun.rowdystory.domain.account.User user =
                userOpt.orElseThrow( () -> new RuntimeException( "해당 이메일에 대한 정보가 없습니다. 이메일 주소를 확인해주세요." ) );

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute( "user", user.toDTO() );

        super.onAuthenticationSuccess( request, response, authentication );
    }
}

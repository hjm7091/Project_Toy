package jin.h.mun.rowdystory.web.controller.account.session;

import javax.servlet.http.HttpSession;

import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.account.UserDTO;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SessionUserArgumentResolver implements HandlerMethodArgumentResolver {

	private final HttpSession httpSession;
	
	@Override
	public boolean supportsParameter( final MethodParameter parameter ) {
		boolean isSessionUserAnnotation = parameter.getParameterAnnotation( SessionUser.class ) != null;
		boolean isUserDTOClass = UserDTO.class.equals( parameter.getParameterType() );
		return isSessionUserAnnotation && isUserDTOClass;
	}

	@Override
	public Object resolveArgument( final MethodParameter parameter, final ModelAndViewContainer mavContainer,
			final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory ) throws Exception {
		return httpSession.getAttribute( "user" );
	}

}

package jin.h.mun.knutalk.social.resolver;

import javax.servlet.http.HttpSession;

import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.social.annotation.SocialUser;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SocialUserArgumentResolver implements HandlerMethodArgumentResolver {

	private final HttpSession httpSession;
	
	@Override
	public boolean supportsParameter( final MethodParameter parameter ) {
		boolean isSocialUserAnnotation = parameter.getParameterAnnotation( SocialUser.class ) != null;
		boolean isUserClass = User.class.equals( parameter.getParameterType() );
		return isSocialUserAnnotation && isUserClass;
	}

	@Override
	public Object resolveArgument( final MethodParameter parameter, final ModelAndViewContainer mavContainer,
			final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory ) throws Exception {
		return httpSession.getAttribute( "user" );
	}

}

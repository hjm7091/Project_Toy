package jin.h.mun.rowdystory.web.session;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class SessionArgumentResolver implements HandlerMethodArgumentResolver {

	private final HttpSession httpSession;
	
	@Override
	public boolean supportsParameter( final MethodParameter parameter ) {

		Optional<Session> sessionOpt = getSession( parameter.getParameterAnnotations() );

		if ( !sessionOpt.isPresent() ) return false;

		SessionDefine sessionDefine = sessionOpt.get().sessionDefine();

		Class<?> sessionType = sessionDefine.getType();

		Class<?> parameterType = parameter.getParameterType();

		return sessionType.equals( parameterType );
	}

	@Override
	public Object resolveArgument( final MethodParameter parameter, final ModelAndViewContainer mavContainer,
			final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory ) throws Exception {

		Optional<Session> sessionOpt = getSession( parameter.getParameterAnnotations() );

//		SessionDefine sessionDefine = sessionOpt.orElseThrow( () -> {
//			return new IllegalStateException( "Session should exist. Because we already checked that exist in method supportsParameter()" );
//		} ).sessionDefine();
		SessionDefine sessionDefine = sessionOpt.get().sessionDefine();

		return httpSession.getAttribute( sessionDefine.getName() );
	}

	private Optional<Session> getSession( Annotation[] annotations ) {
		return Arrays.stream( annotations )
				.map( annotation -> annotation.annotationType().getAnnotation( Session.class ) )
				.filter( Objects::nonNull )
				.findFirst();
	}

}

package jin.h.mun.rowdystory.web.config;

import jin.h.mun.rowdystory.web.interceptor.ModelAttributeAddingHandlerInterceptor;
import jin.h.mun.rowdystory.web.resolver.session.SessionArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

	private final SessionArgumentResolver sessionArgumentResolver;

	private final ModelAttributeAddingHandlerInterceptor modelAttributeAddingHandlerInterceptor;
	
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
			"classpath:/META-INF/resources/", "classpath:/resources/",
			"classpath:/static/", "classpath:/public/" };
	
	@Override
	protected void addResourceHandlers( final ResourceHandlerRegistry registry ) {
        registry
	        .addResourceHandler( "/**" )
	        .addResourceLocations( CLASSPATH_RESOURCE_LOCATIONS )
	        .setCacheControl( CacheControl.noCache() );
//	        .resourceChain( true )
//	        .addResolver( new PathResourceResolver() );
	}

	@Override
	protected void addArgumentResolvers( final List<HandlerMethodArgumentResolver> argumentResolvers ) {
		argumentResolvers.add( sessionArgumentResolver );
	}

	@Override
	protected void addInterceptors( final InterceptorRegistry registry ) {
		registry.addInterceptor( modelAttributeAddingHandlerInterceptor );
	}
}

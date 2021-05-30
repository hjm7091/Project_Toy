package jin.h.mun.rowdystory.web.config;

import jin.h.mun.rowdystory.web.controller.account.session.SessionUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

	private final SessionUserArgumentResolver sessionUserArgumentResolver;
	
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
		argumentResolvers.add( sessionUserArgumentResolver );
	}
}

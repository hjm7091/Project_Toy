package jin.h.mun.knutalk.web.config;

import java.util.List;

import jin.h.mun.knutalk.social.resolver.SocialUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.resource.PathResourceResolver;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

	private final SocialUserArgumentResolver socialUserArgumentResolver;
	
	private static final String[] RESOURCE_LOCATIONS = { "classpath:/static/" };
	
	@Override
	protected void addResourceHandlers( final ResourceHandlerRegistry registry ) {
        registry
	        .addResourceHandler( "/**" )
	        .addResourceLocations( RESOURCE_LOCATIONS )
	        .setCachePeriod( 3600 )
	        .resourceChain( true )
	        .addResolver( new PathResourceResolver() );
	}
	
	@Override
	protected void addArgumentResolvers( final List<HandlerMethodArgumentResolver> argumentResolvers ) {
		argumentResolvers.add( socialUserArgumentResolver );
	}
}

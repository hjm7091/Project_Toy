package jin.h.mun.rowdystory.web.config;

import jin.h.mun.rowdystory.social.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure( HttpSecurity http ) throws Exception {
        http
            .csrf().disable() //h2-console을 위한 설정
            .headers().frameOptions().disable() //h2-console을 위한 설정
            .and()
                .authorizeRequests()
                    .antMatchers( "/", "/home", "/account/**" ).permitAll()
                    .antMatchers( "/custom/**", "/images/**", "/bootstrap/**", "/bootstrap-4.5.3/**", "/jquery/**" ).permitAll()
                    .antMatchers( "/h2-console/**", "/profile" ).permitAll()
                    .anyRequest().authenticated()
            .and()
                .logout()
                .logoutUrl( "/logout" )
                .logoutSuccessUrl( "/home" )
            .and()
                .oauth2Login()
                    .loginPage( "/account/login" )
                    .authorizationEndpoint().baseUri( "/login/oauth2/authorization/" )
                    .and()
                        .defaultSuccessUrl( "/home" )
                        .userInfoEndpoint()
                        .userService( customOAuth2UserService );

    }

    /*
     * 아래 빈 없으면 IllegalStateException 발생함.
     * No thread-bound request found:
     * Are you referring to request attributes outside of an actual web request, or processing a request outside of the originally receiving thread?
     * If you are actually operating within a web request and still receive this message, your code is probably running outside of DispatcherServlet: In this case, use RequestContextListener or RequestContextFilter to expose the current request.
     * @link { https://stackoverflow.com/questions/24025924/java-lang-illegalstateexception-no-thread-bound-request-found-exception-in-asp }
     */
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

}

package jin.h.mun.rowdystory.web.config;

import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.service.account.rowdy.FormLoginService;
import jin.h.mun.rowdystory.web.controller.account.handler.FormLoginFailureHandler;
import jin.h.mun.rowdystory.web.controller.account.handler.FormLoginSuccessHandler;
import jin.h.mun.rowdystory.service.account.social.OAuth2LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2LoginService OAuth2LoginService;

    private final FormLoginService formLoginService;

    private final FormLoginSuccessHandler formLoginSuccessHandler;

    private final FormLoginFailureHandler formLoginFailureHandler;

    @Override
    protected void configure( HttpSecurity http ) throws Exception {
        http
            .csrf().disable() //h2-console을 위한 설정
            .headers().frameOptions().disable() //h2-console을 위한 설정
            .and()
                .authorizeRequests()
                    .antMatchers( "/", "/home" ).permitAll()
            .and()
                .authorizeRequests()
                    .antMatchers( "/h2-console/**", "/profile" ).hasAuthority( RoleType.ADMIN.getRoleName() )
                    .antMatchers( "/api/**" ).hasAnyAuthority( RoleType.ADMIN.getRoleName(), RoleType.USER.getRoleName() )
            .and()
                .formLogin()
                    .loginPage( "/account/login" )
                    .usernameParameter( "email" )
                    .successHandler( formLoginSuccessHandler )
                    .failureHandler( formLoginFailureHandler )
                    .permitAll()
            .and()
                .logout()
                .logoutUrl( "/logout" )
                .logoutSuccessUrl( "/home" )
                .invalidateHttpSession( true ) //로그아웃 시 세션정보를 제거할 지 여부를 지정한다. 기본값은 TRUE 이고 세션정보를 제거한다.
            .and()
                .oauth2Login()
                    .loginPage( "/account/login" )
                    .authorizationEndpoint().baseUri( "/login/oauth2/authorization/" )
                    .and()
                        .defaultSuccessUrl( "/home" )
                        .userInfoEndpoint()
                        .userService( OAuth2LoginService );
    }

    @Override
    public void configure( WebSecurity web ) throws Exception {
        web.ignoring()
            .antMatchers( "/custom/**", "/images/**", "/bootstrap/**", "/bootstrap-4.5.3/**", "/jquery/**" )
            .antMatchers( "/favicon.ico" );
    }

    @Override
    protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( formLoginService ).passwordEncoder( passwordEncoder() );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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

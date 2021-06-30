package jin.h.mun.rowdystory.web.config;

import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.service.account.rowdy.FormLoginService;
import jin.h.mun.rowdystory.service.account.social.OAuth2LoginService;
import jin.h.mun.rowdystory.web.controller.api.account.AccountAPI;
import jin.h.mun.rowdystory.web.controller.view.account.AccountView;
import jin.h.mun.rowdystory.web.controller.view.account.handler.LoginFailureHandler;
import jin.h.mun.rowdystory.web.controller.view.account.handler.FormLoginSuccessHandler;
import jin.h.mun.rowdystory.web.controller.view.account.handler.OAuth2LoginSuccessHandler;
import jin.h.mun.rowdystory.web.controller.view.home.HomeView;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2LoginService OAuth2LoginService;

    private final FormLoginService formLoginService;

    private final FormLoginSuccessHandler formLoginSuccessHandler;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    private final LoginFailureHandler loginFailureHandler;

    @Override
    protected void configure( HttpSecurity http ) throws Exception {
        http
            .csrf().disable() //h2-console을 위한 설정
            .headers().frameOptions().disable() //h2-console을 위한 설정
            .and()
                .authorizeRequests()
                    .antMatchers( HomeView.ROOT, HomeView.ROOT_HOME ).permitAll()
                    .antMatchers( AccountAPI.BASE + "/**" ).permitAll()
            .and()
                .authorizeRequests()
                    .antMatchers( "/h2-console/**", "/profile" ).hasAuthority( RoleType.ADMIN.getRoleName() )
                    .antMatchers( "/api/**" ).hasAnyAuthority( RoleType.ADMIN.getRoleName(), RoleType.USER.getRoleName() )
            .and()
                .formLogin()
                    .loginPage( AccountView.ROOT_LOGIN )
                    .usernameParameter( "email" )
                    .passwordParameter( "password" )
                    .successHandler( formLoginSuccessHandler )
                    .failureHandler( loginFailureHandler )
                    .permitAll()
            .and()
                .logout()
                .logoutUrl( AccountView.ROOT_LOGOUT )
                .logoutSuccessUrl( HomeView.ROOT_HOME )
                .invalidateHttpSession( true ) // 로그아웃 시 세션정보를 제거할 지 여부를 지정한다. 기본값은 TRUE 이고 세션정보를 제거한다.
            .and()
                .oauth2Login()
                    .loginPage( AccountView.ROOT_LOGIN )
                    .authorizationEndpoint().baseUri( "/login/oauth2/authorization/" )
                    .and()
                        .defaultSuccessUrl( HomeView.ROOT_HOME )
                        .userInfoEndpoint()
                        .userService( OAuth2LoginService )
                    .and()
                        .successHandler( oAuth2LoginSuccessHandler )
                        .failureHandler( loginFailureHandler );
    }

    @Override
    public void configure( WebSecurity web ) throws Exception {
        web.ignoring()
            .antMatchers( "/custom/**", "/images/**", "/bootstrap/**", "/bootstrap-4.5.3/**", "/jquery/**" )
            .antMatchers( "/favicon.ico" );
    }

    @Override
    protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
        auth.authenticationProvider( daoAuthenticationProvider() );
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService( formLoginService );
        daoAuthenticationProvider.setPasswordEncoder( passwordEncoder() );
        daoAuthenticationProvider.setHideUserNotFoundExceptions( false ); // true 로 설정하면 UsernameNotFoundException -> BadCredentialsException 으로 숨겨짐, 보안상 true 가 더 안전함.
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
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

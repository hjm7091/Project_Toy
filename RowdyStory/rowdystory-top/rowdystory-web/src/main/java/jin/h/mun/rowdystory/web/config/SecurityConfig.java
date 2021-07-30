package jin.h.mun.rowdystory.web.config;

import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.service.account.rowdy.FormLoginService;
import jin.h.mun.rowdystory.service.account.social.OAuth2LoginService;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.login.handler.FormLoginSuccessHandler;
import jin.h.mun.rowdystory.web.controller.view.account.login.handler.LoginFailureHandler;
import jin.h.mun.rowdystory.web.controller.view.account.login.handler.OAuth2LoginSuccessHandler;
import jin.h.mun.rowdystory.web.controller.view.home.Home.HomeMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2LoginService OAuth2LoginService;

    private final FormLoginService formLoginService;

    private final FormLoginSuccessHandler formLoginSuccessHandler;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    private final LoginFailureHandler loginFailureHandler;

    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure( HttpSecurity http ) throws Exception {
        http
            .csrf().disable() //h2-console 을 위한 설정
            .headers().frameOptions().disable() //h2-console 을 위한 설정
            .and()
                .authorizeRequests()
                    .antMatchers( "/h2-console/**", "/profile" ).hasAuthority( RoleType.ADMIN.getRoleName() )
//                    .antMatchers( "/api/**" ).hasAnyAuthority( RoleType.ADMIN.getRoleName(), RoleType.USER.getRoleName() )
            .and()
                .formLogin()
                    .loginPage( AccountMapping.LOGIN )
                    .usernameParameter( "email" )
                    .passwordParameter( "password" )
                    .successHandler( formLoginSuccessHandler )
                    .failureHandler( loginFailureHandler )
                    .permitAll()
            .and()
                .logout()
                .logoutUrl( AccountMapping.LOGOUT )
                .logoutSuccessUrl( HomeMapping.HOME )
                .invalidateHttpSession( true ) // 로그아웃 시 세션정보를 제거할 지 여부를 지정한다. 기본값은 TRUE 이고 세션정보를 제거한다.
            .and()
                .oauth2Login()
                    .loginPage( AccountMapping.LOGIN )
                    .authorizationEndpoint().baseUri( "/login/oauth2/authorization/" )
                    .and()
                        .defaultSuccessUrl( HomeMapping.HOME )
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
        daoAuthenticationProvider.setPasswordEncoder( passwordEncoder );
        daoAuthenticationProvider.setHideUserNotFoundExceptions( false ); // true 로 설정하면 UsernameNotFoundException -> BadCredentialsException 으로 숨겨짐, 보안상 true 가 더 안전함.
        return daoAuthenticationProvider;
    }

}

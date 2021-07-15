package jin.h.mun.rowdystory.web.controller.view.account.login;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.exception.account.ErrorMessage;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountView;
import jin.h.mun.rowdystory.web.controller.view.attribute.ModelAttribute;
import jin.h.mun.rowdystory.web.controller.view.home.Home.HomeMapping;
import jin.h.mun.rowdystory.web.controller.view.home.Home.HomeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static jin.h.mun.rowdystory.web.controller.view.attribute.AttributeKeyAndValueForView.LOGIN_ERROR_MESSAGE;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LoginControllerTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder;

    private User formUser, socialUser;

    @BeforeEach
    public void setUp() {
        requestBuilder = formLogin()
                .userParameter( "email" )
                .passwordParam( "password" )
                .loginProcessingUrl( AccountMapping.LOGIN );

        initUserData();
    }

    private void initUserData() {
        formUser = User.builder()
                .email( "jin@test.com" )
                .userName( "jin" )
                .password( passwordEncoder.encode( "123456" ) )
                .roleType( RoleType.USER )
                .build();

        socialUser = User.builder()
                .email( "jin@naver.com" )
                .userName( "jin" )
                .roleType( RoleType.USER )
                .socialType( SocialType.GOOGLE )
                .build();

        userRepository.save( formUser );
        userRepository.save( socialUser );
    }

    @Test
    @DisplayName( "세션 정보 없는 경우 로그인 페이지 호출시 login.html 리턴" )
    public void getLoginPageWithNoSession() throws Exception {
        //given
        String[] attributes = ModelAttribute.of( AccountView.LOGIN ).keySet().toArray( new String[0] );

        //when
        mockMvc.perform( get( AccountMapping.LOGIN ) )
//                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( view().name( AccountView.LOGIN ) )
                .andExpect( model().attributeExists( attributes ) );
    }

    @Test
    @DisplayName( "세션 정보 있는 경우 로그인 페이지 호출시 home.html 리턴" )
    public void getLoginPageWithSession() throws Exception {
        //given
        HashMap<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put( "user", UserDTO.builder().email( "test@test.com" ).build() );

        //when
        mockMvc.perform( get( AccountMapping.LOGIN ).sessionAttrs( sessionAttrs ) )
//                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( view().name( HomeView.HOME ) );
    }

    @Test
    @DisplayName( "존재하지 않는 이메일로 form 로그인 시도" )
    public void doFormLoginWithNonExistEmail() throws Exception {
        //given
        requestBuilder.user( "test@test.com" ).password( "123456" );
        String expectedUrl = AccountMapping.LOGIN_FAIL + "?message=" + ErrorMessage.EMAIL_NOT_EXIST.getMessage();

        //when
        mockMvc.perform( requestBuilder )
//                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( forwardedUrl( expectedUrl ) );
    }

    @Test
    @DisplayName( "소셜 계정으로 form 로그인 시도" )
    public void doFormLoginWithSocialAccount() throws Exception {
        //given
        requestBuilder.user( socialUser.getEmail() ).password( socialUser.getPassword() );
        String expectedUrl = AccountMapping.LOGIN_FAIL + "?message=" + ErrorMessage.EMAIL_AlREADY_REGISTERED_SOCIAL_ACCOUNT.getMessage();

        //when
        mockMvc.perform( requestBuilder )
//                .andDo( print() );
                .andExpect( status().isOk() )
                .andExpect( forwardedUrl( expectedUrl ) );
    }

    @Test
    @DisplayName( "일치하지 않는 비밀번호로 form 로그인 시도" )
    public void doFormLoginWithUnMatchedPassword() throws Exception {
        //given
        requestBuilder.user( formUser.getEmail() ).password( "1111111" );
        String expectedUrl = AccountMapping.LOGIN_FAIL + "?message=" + ErrorMessage.PASSWORD_NOT_MATCH.getMessage();

        //when
        mockMvc.perform( requestBuilder )
//                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( forwardedUrl( expectedUrl ) );
    }

    @Test
    @DisplayName( "유효한 이메일, 패스워드로 form 로그인 시도" )
    public void doFormLoginWithValidInfo() throws Exception {
        //given
        requestBuilder.user( formUser.getEmail() ).password( "123456" );

        //when
        mockMvc.perform( requestBuilder )
//                .andDo( print() )
                .andExpect( status().is3xxRedirection() )
                .andExpect( redirectedUrl( HomeMapping.ROOT ) );
    }

    @Test
    @DisplayName( "form, oauth2 로그인 실패 후 redirectUrl 매핑 테스트" )
    public void loginFailure() throws Exception {
        //given
        String errorMessage = "에러 메시지";
        String failUrl = AccountMapping.LOGIN_FAIL + "?message=" + errorMessage;
        String[] attributes = ModelAttribute.of( AccountView.LOGIN ).keySet().toArray( new String[0] );

        //when
        mockMvc.perform( post( failUrl ) )
//                .andDo( print() )
                .andExpect( view().name( AccountView.LOGIN ) )
                .andExpect( model().attributeExists( attributes ) )
                .andExpect( model().attributeExists( LOGIN_ERROR_MESSAGE.key ) )
                .andExpect( content().string( containsString( errorMessage ) ) );

        mockMvc.perform( get( failUrl ) )
//                .andDo( print() )
                .andExpect( view().name( AccountView.LOGIN ) )
                .andExpect( model().attributeExists( attributes ) )
                .andExpect( model().attributeExists( LOGIN_ERROR_MESSAGE.key ) )
                .andExpect( content().string( containsString( errorMessage ) ) );
    }

}
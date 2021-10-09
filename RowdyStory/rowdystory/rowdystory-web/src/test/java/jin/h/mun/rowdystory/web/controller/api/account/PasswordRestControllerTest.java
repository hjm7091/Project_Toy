package jin.h.mun.rowdystory.web.controller.api.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.dto.account.api.PasswordUpdateRequest;
import jin.h.mun.rowdystory.exception.account.AccountPreviousPasswordUnmatchedException;
import jin.h.mun.rowdystory.exception.account.SocialAccountUnmodifiableException;
import jin.h.mun.rowdystory.web.resolver.session.SessionDefine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PasswordRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName( "마이페이지에서 비밀번호 변경 (소셜 계정인 경우 에러 발생)" )
    public void socialUserChangePassword() throws Exception {
        //given
        User jin = User.builder()
                .email( "jin@naver.com" )
                .userName( "jin" )
                .password( passwordEncoder.encode( "1234" ) )
                .roleType( RoleType.USER )
                .socialType( SocialType.GOOGLE ).build();
        userRepository.saveAndFlush( jin );

        HashMap<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put( SessionDefine.USER.getName(), jin.toDTO() );
        PasswordUpdateRequest passwordUpdateRequest = PasswordUpdateRequest.builder()
                .before( "1234" ).after( "2345" ).build();

        //when
        MvcResult mvcResult = mockMvc.perform( put( AccountAPI.PASSWORD )
                        .accept( MediaTypes.HAL_JSON_VALUE )
                        .contentType( MediaTypes.HAL_JSON_VALUE )
                        .content( objectMapper.writeValueAsString(passwordUpdateRequest) )
                        .sessionAttrs( sessionAttrs ) )
                        .andExpect( status().is5xxServerError() )
                        .andReturn();

        //then
        String errorMessage = mvcResult.getResponse().getContentAsString();
        assertThat( errorMessage ).contains( SocialAccountUnmodifiableException.class.getSimpleName() );
    }

    @Test
    @DisplayName( "마이페이지에서 비밀번호 변경 (이전 비밀번호가 일치하지 않는 경우 에러 발생)" )
    public void changePasswordWithInvalidPreviousPassword() throws Exception {
        //given
        User jin = User.builder()
                .email( "jin@naver.com" )
                .userName( "jin" )
                .password( passwordEncoder.encode( "1234" ) )
                .roleType( RoleType.USER ).build();
        userRepository.saveAndFlush( jin );

        HashMap<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put( SessionDefine.USER.getName(), jin.toDTO() );
        PasswordUpdateRequest passwordUpdateRequest = PasswordUpdateRequest.builder()
                .before( "1111" ).after( "2345" ).build();

        //when
        MvcResult mvcResult = mockMvc.perform( put( AccountAPI.PASSWORD )
                        .accept( MediaTypes.HAL_JSON_VALUE )
                        .contentType( MediaTypes.HAL_JSON_VALUE )
                        .content( objectMapper.writeValueAsString(passwordUpdateRequest) )
                        .sessionAttrs( sessionAttrs ) )
                        .andExpect( status().is5xxServerError() )
                        .andReturn();

        //then
        String errorMessage = mvcResult.getResponse().getContentAsString();
        assertThat( errorMessage ).contains( AccountPreviousPasswordUnmatchedException.class.getSimpleName() );
    }

    @Test
    @DisplayName( "마이페이지에서 비밀번호 변경 (이전 비밀번호가 일치하는 경우 비밀번호 변경됨)" )
    public void changePasswordWithValidPreviousPassword() throws Exception {
        //given
        User jin = User.builder()
                .email( "jin@naver.com" )
                .userName( "jin" )
                .password( passwordEncoder.encode( "1234" ) )
                .roleType( RoleType.USER ).build();
        userRepository.saveAndFlush( jin );

        HashMap<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put( SessionDefine.USER.getName(), jin.toDTO() );
        PasswordUpdateRequest passwordUpdateRequest = PasswordUpdateRequest.builder()
                .before( "1234" ).after( "2345" ).build();

        //when
        mockMvc.perform( put( AccountAPI.PASSWORD )
                        .accept( MediaTypes.HAL_JSON_VALUE )
                        .contentType( MediaTypes.HAL_JSON_VALUE )
                        .content( objectMapper.writeValueAsString(passwordUpdateRequest) )
                        .sessionAttrs( sessionAttrs ) )
                .andExpect( status().isOk() );

        //then
        assertThat( passwordEncoder.matches( "2345", jin.getPassword() ) ).isTrue();
    }

}
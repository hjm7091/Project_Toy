package jin.h.mun.rowdystory.web.controller.api.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.api.UpdateRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmailRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName( "존재하는 이메일로 중복 확인 요청시 결과는 true" )
    public void checkWithDuplicatedEmail() throws Exception {
        //given
        User jin = User.builder()
                .email( "jin@naver.com" )
                .userName( "jin" ).build();
        userRepository.save( jin );

        //when
        MvcResult mvcResult = mockMvc.perform( get( AccountAPI.EMAIL )
                .param( "email", jin.getEmail() )
                .accept( MediaTypes.HAL_JSON_VALUE )
                .contentType( MediaTypes.HAL_JSON_VALUE ) )
                .andExpect( status().isOk() )
                .andReturn();

        //then
        assertThat( mvcResult.getResponse().getContentAsString() ).isEqualTo( "true" );
    }

    @Test
    @DisplayName( "존재하지 않는 이메일로 중복 확인 요청시 결과는 false" )
    public void checkWithNonDuplicatedEmail() throws Exception {
        //given
        String email = "test@test.com";

        //when
        MvcResult mvcResult = mockMvc.perform( get( AccountAPI.EMAIL )
                .param( "email", email )
                .accept( MediaTypes.HAL_JSON_VALUE )
                .contentType( MediaTypes.HAL_JSON_VALUE ) )
                .andExpect( status().isOk() )
                .andReturn();

        //then
        assertThat( mvcResult.getResponse().getContentAsString() ).isEqualTo( "false" );
    }

    @Test
    @DisplayName( "마이페이지에서 이메일 변경 (소셜 계정이 아닌 경우 이메일, 세션 정보 변경됨)" )
    public void nonSocialUserChangeEmail() throws Exception {
        //given
        User jin = User.builder()
            .email( "jin@naver.com" )
            .userName( "jin" )
            .password( passwordEncoder.encode( "1234" ) )
            .roleType( RoleType.USER ).build();
        userRepository.save( jin );

        HashMap<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put( SessionDefine.USER.getName(), jin.toDTO() );
        UpdateRequest hak = UpdateRequest.builder()
                .email( "hak@naver.com" ).build();

        //when
        MvcResult mvcResult = mockMvc.perform( put( AccountAPI.EMAIL )
                        .accept( MediaTypes.HAL_JSON_VALUE )
                        .contentType( MediaTypes.HAL_JSON_VALUE )
                        .content( objectMapper.writeValueAsString( hak ) )
                        .sessionAttrs( sessionAttrs ) )
                        .andExpect( status().isOk() )
                        .andReturn();

        //then
        UserDTO changedUserDTO = objectMapper.readValue( mvcResult.getResponse().getContentAsString(), UserDTO.class );
        assertThat( changedUserDTO.getEmail() ).isEqualTo( hak.getEmail() );
        assertThat( jin.getEmail() ).isEqualTo( hak.getEmail() );
    }

    @Test
    @DisplayName( "마이페이지에서 이메일 변경 (소셜 계정인 경우 에러 발생)" )
    public void socialUserChangeEmail() throws Exception {
        //given
        User jin = User.builder()
                .email( "jin@naver.com" )
                .userName( "jin" )
                .password( passwordEncoder.encode( "1234" ) )
                .roleType( RoleType.USER )
                .socialType( SocialType.GOOGLE ).build();
        userRepository.save( jin );

        HashMap<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put( SessionDefine.USER.getName(), jin.toDTO() );
        UpdateRequest hak = UpdateRequest.builder()
                .email( "hak@naver.com" ).build();

        //when
        MvcResult mvcResult = mockMvc.perform( put( AccountAPI.EMAIL )
                        .accept( MediaTypes.HAL_JSON_VALUE )
                        .contentType( MediaTypes.HAL_JSON_VALUE )
                        .content( objectMapper.writeValueAsString( hak ) )
                        .sessionAttrs( sessionAttrs ) )
                        .andExpect( status().is5xxServerError() )
                        .andReturn();

        //then
        String errorMessage = mvcResult.getResponse().getContentAsString();
        assertThat( errorMessage ).contains( SocialAccountUnmodifiableException.class.getSimpleName() );
    }

}
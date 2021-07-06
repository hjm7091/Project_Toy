package jin.h.mun.rowdystory.web.controller.view.account.register;

import com.fasterxml.jackson.databind.ObjectMapper;
import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.web.controller.attributes.account.LoginAttributes;
import jin.h.mun.rowdystory.web.controller.attributes.account.RegisterAttributes;
import jin.h.mun.rowdystory.web.controller.attributes.home.HomeAttributes;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName( "회원 가입 페이지 호출시 register.html 리턴" )
    public void getRegisterPage() throws Exception {
        //given
        String[] attributes = {
            RegisterAttributes.REGISTER_REQUEST_OBJECT, RegisterAttributes.REGISTER_URI,
            LoginAttributes.LOGIN_URI, HomeAttributes.HOME_URI
        };

        //when
        mockMvc.perform( get( AccountMapping.REGISTER ) )
                .andExpect( status().isOk() )
                .andExpect( view().name( AccountView.REGISTER ) )
                .andExpect( model().attributeExists( attributes ) );
    }

    @Test
    @DisplayName( "회원 가입 (중복되지 않는 이메일)" )
    public void registerWithUniqueEmail() throws Exception {
        //given
        UserRegisterRequest jin = UserRegisterRequest.builder()
                .email( "jin@test.com" )
                .password( "1234" )
                .userName( "jin" ).build();

        //when
        mockMvc.perform( post( AccountMapping.REGISTER )
                .accept( MediaTypes.HAL_JSON_VALUE )
                .contentType( MediaTypes.HAL_JSON_VALUE )
                .content( objectMapper.writeValueAsString( jin ) ) )
                .andExpect( status().isOk() );
    }

    @Test
    @DisplayName( "회원 가입 (중복된 이메일)" )
    public void registerWithNonUniqueEmail() throws Exception {
        //given
        userRepository.save( User.builder().email( "jin@test.com" ).build() );
        UserRegisterRequest admin = UserRegisterRequest.builder()
                .email( "jin@test.com" )
                .password( "1234" )
                .userName( "admin" ).build();

        //when
        mockMvc.perform( post( AccountMapping.REGISTER )
                .accept( MediaTypes.HAL_JSON_VALUE )
                .contentType( MediaTypes.HAL_JSON_VALUE )
                .content( objectMapper.writeValueAsString( admin ) ) )
                .andExpect( status().is5xxServerError() );
    }

}
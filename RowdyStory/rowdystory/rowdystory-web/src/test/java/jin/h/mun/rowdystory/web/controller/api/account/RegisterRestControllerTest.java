package jin.h.mun.rowdystory.web.controller.api.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.api.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RegisterRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName( "회원 가입 (중복되지 않는 이메일)" )
    public void registerWithUniqueEmail() throws Exception {
        //given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email( "test@naver.com" )
                .password( "1234" )
                .userName( "test" )
                .build();

        //when
        MvcResult mvcResult = mockMvc.perform( post( AccountAPI.REGISTER )
                        .accept( MediaTypes.HAL_JSON_VALUE )
                        .contentType( MediaTypes.HAL_JSON_VALUE )
                        .content( objectMapper.writeValueAsString( registerRequest ) ) )
//                        .andDo( print() )
                        .andExpect( status().isCreated() )
                        .andReturn();

        //then
        UserDTO user = objectMapper.readValue( mvcResult.getResponse().getContentAsString(), User.class ).toDTO();
        assertThat( user.getEmail() ).isEqualTo( registerRequest.getEmail() );
        assertThat( user.getUserName() ).isEqualTo( registerRequest.getUserName() );
    }

    @Test
    @DisplayName( "회원 가입 (중복된 이메일)" )
    public void registerWithNonUniqueEmail() throws Exception {
        //given
        userRepository.save( User.builder().email( "jin@test.com" ).build() );
        RegisterRequest jin = RegisterRequest.builder()
                .email( "jin@test.com" )
                .password( "1234" )
                .userName( "admin" ).build();

        //when
        mockMvc.perform( post( AccountAPI.REGISTER )
                        .accept( MediaTypes.HAL_JSON_VALUE )
                        .contentType( MediaTypes.HAL_JSON_VALUE )
                        .content( objectMapper.writeValueAsString( jin ) ) )
//                .andDo( print() )
                        .andExpect( status().is5xxServerError() );
    }

}
package jin.h.mun.rowdystory.web.controller.api.account;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmailDuplicateCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName( "존재하는 이메일로 요청시 결과는 true" )
    public void checkWithDuplicateEmail() throws Exception {
        //given
        User jin = User.builder()
                .email( "jin@naver.com" )
                .userName( "jin" ).build();
        userRepository.save( jin );

        //when
        MvcResult mvcResult = mockMvc.perform( get( AccountAPI.BASE )
                .param( "email", jin.getEmail() )
                .accept( MediaTypes.HAL_JSON_VALUE )
                .contentType( MediaTypes.HAL_JSON_VALUE ) )
                .andExpect( status().isOk() )
                .andReturn();

        //then
        assertThat( mvcResult.getResponse().getContentAsString() ).isEqualTo( "true" );
    }

    @Test
    @DisplayName( "존재하지 않는 이메일로 요청시 결과는 false" )
    public void checkWithNonDuplicateEmail() throws Exception {
        //given
        String email = "test@test.com";

        //when
        MvcResult mvcResult = mockMvc.perform( get( AccountAPI.BASE )
                .param( "email", email )
                .accept( MediaTypes.HAL_JSON_VALUE )
                .contentType( MediaTypes.HAL_JSON_VALUE ) )
                .andExpect( status().isOk() )
                .andReturn();

        //then
        assertThat( mvcResult.getResponse().getContentAsString() ).isEqualTo( "false" );
    }

}
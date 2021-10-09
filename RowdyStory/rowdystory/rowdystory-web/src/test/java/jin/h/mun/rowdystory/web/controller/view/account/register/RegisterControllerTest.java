package jin.h.mun.rowdystory.web.controller.view.account.register;

import com.fasterxml.jackson.databind.ObjectMapper;
import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountView;
import jin.h.mun.rowdystory.web.controller.view.attribute.ModelAttribute;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        String[] attributes = ModelAttribute.of( AccountView.REGISTER ).keySet().toArray( new String[0] );

        //when
        mockMvc.perform( get( AccountMapping.REGISTER ) )
                .andExpect( status().isOk() )
                .andExpect( view().name( AccountView.REGISTER ) )
                .andExpect( model().attributeExists( attributes ) );
    }

}
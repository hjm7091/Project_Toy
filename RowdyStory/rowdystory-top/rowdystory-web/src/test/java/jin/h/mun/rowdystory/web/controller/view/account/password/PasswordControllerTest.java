package jin.h.mun.rowdystory.web.controller.view.account.password;

import jin.h.mun.rowdystory.web.controller.attributes.account.LoginAttributes;
import jin.h.mun.rowdystory.web.controller.attributes.account.PasswordAttributes;
import jin.h.mun.rowdystory.web.controller.attributes.account.RegisterAttributes;
import jin.h.mun.rowdystory.web.controller.attributes.home.HomeAttributes;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountView;
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
class PasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName( "비밀 번호 찾기 페이지 호출시 password.html 리턴" )
    public void getPasswordPage() throws Exception {
        //given
        String[] attributes = {
            PasswordAttributes.PASSWORD_REQUEST_OBJECT, PasswordAttributes.PASSWORD_URI,
            LoginAttributes.LOGIN_URI, RegisterAttributes.REGISTER_URI, HomeAttributes.HOME_URI
        };

        //when
        mockMvc.perform( get( AccountMapping.PASSWORD ) )
                .andExpect( status().isOk() )
                .andExpect( view().name( AccountView.PASSWORD ) )
                .andExpect( model().attributeExists( attributes ) );
    }

}
package jin.h.mun.rowdystory.web.controller.view.home;

import jin.h.mun.rowdystory.web.controller.attributes.account.LoginAttributes;
import jin.h.mun.rowdystory.web.controller.attributes.account.RegisterAttributes;
import jin.h.mun.rowdystory.web.controller.attributes.home.HomeAttributes;
import jin.h.mun.rowdystory.web.controller.view.home.HomeResolver.HomeMapping;
import jin.h.mun.rowdystory.web.controller.view.home.HomeResolver.HomeView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void home() throws Exception {
        //given
        String[] attributes = {
            HomeAttributes.HOME_URI, LoginAttributes.LOGIN_URI,
            RegisterAttributes.REGISTER_URI
        };

        //when
        mockMvc.perform( get( HomeMapping.ROOT ) )
                .andExpect( status().isOk() )
                .andExpect( view().name( HomeView.HOME ) )
                .andExpect( model().attributeExists( attributes ) );

        mockMvc.perform( get( HomeMapping.HOME ) )
                .andExpect( status().isOk() )
                .andExpect( view().name( HomeView.HOME ) )
                .andExpect( model().attributeExists( attributes ) );
    }

}
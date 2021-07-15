package jin.h.mun.rowdystory.web.controller.view.home;

import jin.h.mun.rowdystory.web.controller.view.home.Home.HomeMapping;
import jin.h.mun.rowdystory.web.controller.view.home.Home.HomeView;
import jin.h.mun.rowdystory.web.controller.view.attribute.ModelAttribute;
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
        String[] attributeKeys = ModelAttribute.of( HomeView.HOME ).keySet().toArray( new String[0] );

        //when
        mockMvc.perform( get( HomeMapping.ROOT ) )
                .andExpect( status().isOk() )
                .andExpect( view().name( HomeView.HOME ) )
                .andExpect( model().attributeExists( attributeKeys ) );

        mockMvc.perform( get( HomeMapping.HOME ) )
                .andExpect( status().isOk() )
                .andExpect( view().name( HomeView.HOME ) )
                .andExpect( model().attributeExists( attributeKeys ) );
    }

}
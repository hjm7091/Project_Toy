package jin.h.mun.rowdystory.web.controller.view.home;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void home() throws Exception {
        mockMvc.perform( get( HomeView.ROOT ) )
                .andExpect( status().isOk() )
                .andExpect( view().name( HomeView.HOME ) );

        mockMvc.perform( get( HomeView.ROOT_HOME ) )
                .andExpect( status().isOk() )
                .andExpect( view().name( HomeView.HOME ) );
    }

}
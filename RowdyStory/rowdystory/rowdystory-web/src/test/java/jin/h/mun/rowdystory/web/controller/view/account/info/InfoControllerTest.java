package jin.h.mun.rowdystory.web.controller.view.account.info;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountView;
import jin.h.mun.rowdystory.web.controller.view.attribute.ModelAttribute;
import jin.h.mun.rowdystory.web.resolver.session.SessionDefine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName( "세션 정보 없는 경우 개인 정보 페이지 호출시 login.html 리턴" )
    public void getInfoPageWithoutSession() throws Exception {
        //given
        String[] attributes = ModelAttribute.of( AccountView.LOGIN ).keySet().toArray( new String[0] );

        //when
        mockMvc.perform( get( AccountMapping.INFO ) )
            .andExpect( status().isOk() )
            .andExpect( model().attributeExists( attributes ) )
            .andExpect( view().name( AccountView.LOGIN ) );
    }

    @Test
    @DisplayName( "세션 정보 있는 경우 개인 정보 페이지 호출시 info.html 리턴" )
    public void getInfoPageWithSession() throws Exception {
        //given
        String[] attributes = ModelAttribute.of( AccountView.INFO ).keySet().toArray( new String[0] );
        HashMap<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put( SessionDefine.USER.getName(), UserDTO.builder().email( "test@test.com" ).build() );

        //when
        mockMvc.perform( get( AccountMapping.INFO ).sessionAttrs( sessionAttrs ) )
                .andExpect( status().isOk() )
                .andExpect( model().attributeExists( attributes ) )
                .andExpect( view().name( AccountView.INFO ) );
    }
}
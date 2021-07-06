package jin.h.mun.rowdystory.web.controller.api.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.dto.account.UserUpdateRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountCURDControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName( "회원 가입" )
    public void register() throws Exception {
        //given
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email( "test@naver.com" )
                .password( "1234" )
                .userName( "test" )
                .picture( "picture1" )
                .build();

        //when
        MvcResult mvcResult = mockMvc.perform( post( AccountAPI.BASE )
                .accept( MediaTypes.HAL_JSON_VALUE )
                .contentType( MediaTypes.HAL_JSON_VALUE )
                .content( objectMapper.writeValueAsString( userRegisterRequest ) ) )
                .andDo( print() )
                .andExpect( status().isCreated() )
                .andReturn();

        //then
        UserDTO user = objectMapper.readValue( mvcResult.getResponse().getContentAsString(), User.class ).toDTO();
        assertThat( user.getEmail() ).isEqualTo( userRegisterRequest.getEmail() );
        assertThat( user.getUserName() ).isEqualTo( userRegisterRequest.getUserName() );
        assertThat( user.getPicture() ).isEqualTo( userRegisterRequest.getPicture() );
    }

    @Test
    @DisplayName( "회원 조회 (전체)" )
    public void getAll() throws Exception {
        mockMvc.perform( get( AccountAPI.BASE )
                .accept( MediaTypes.HAL_JSON_VALUE ))
                .andDo( print() )
                .andExpect( status().isOk() );
    }

    @Test
    @DisplayName( "회원 조회 (유효한 id)" )
    public void getByValidId() throws Exception {
        mockMvc.perform( get( AccountAPI.BASE + "/{id}", 1L )
                .accept( MediaTypes.HAL_JSON_VALUE ))
                .andDo( print() )
                .andExpect( status().isOk() );
    }

    @Test
    @DisplayName( "회원 조회 (유효하지 않은 id)" )
    public void getByInValidId() throws Exception {
        mockMvc.perform( get( AccountAPI.BASE + "/{id}", -1L )
                .accept( MediaTypes.HAL_JSON_VALUE ))
                .andDo( print() )
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName( "회원 수정 (유효한 id)" )
    public void updateByValidId() throws Exception {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .password( "updatePassword" )
                .userName( "updateUserName" )
                .picture( "updatePicture" )
                .roleType( "user" )
                .build();

        //when
        mockMvc.perform( put( AccountAPI.BASE + "/{id}", 1L )
                .accept( MediaTypes.HAL_JSON_VALUE )
                .contentType( MediaTypes.HAL_JSON_VALUE )
                .content( objectMapper.writeValueAsString( userUpdateRequest ) ) )
                .andDo( print() )
                .andExpect( status().isOk() );
    }

    @Test
    @DisplayName( "회원 수정 (유효하지 않은 id)" )
    public void updateByInValidId() throws Exception {
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .password( "updatePassword" )
                .userName( "updateUserName" )
                .picture( "updatePicture" )
                .roleType( "user" )
                .build();

        mockMvc.perform( put( AccountAPI.BASE + "/{id}", -1L )
                .accept( MediaTypes.HAL_JSON_VALUE )
                .contentType( MediaTypes.HAL_JSON_VALUE )
                .content( objectMapper.writeValueAsString( userUpdateRequest ) ) )
                .andDo( print() )
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName( "회원 삭제 (유효한 id)" )
    public void deleteByValidId() throws Exception {
        mockMvc.perform( delete( AccountAPI.BASE + "/{id}", 1L )
                .accept( MediaTypes.HAL_JSON_VALUE ))
                .andDo( print() )
                .andExpect( status().isOk() );
    }

    @Test
    @DisplayName( "회원 삭제 (유효하지 않은 id)" )
    public void deleteByInValidId() throws Exception {
        mockMvc.perform( delete( AccountAPI.BASE + "/{id}", -1L )
                .accept( MediaTypes.HAL_JSON_VALUE ))
                .andDo( print() )
                .andExpect( status().isNotFound() );
    }
}
package jin.h.mun.rowdystory.web.controller.api.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.api.UpdateRequest;
import jin.h.mun.rowdystory.web.resolver.session.SessionDefine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CRUDForAdminRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName( "회원 조회 (전체)" )
    public void getAll() throws Exception {
        mockMvc.perform( get( AccountAPI.BASE )
                .accept( MediaTypes.HAL_JSON_VALUE ))
//                .andDo( print() )
                .andExpect( status().isOk() );
    }

    @Test
    @DisplayName( "회원 조회 (유효한 id)" )
    public void getByValidId() throws Exception {
        mockMvc.perform( get( AccountAPI.BASE + "/{id}", 1L )
                .accept( MediaTypes.HAL_JSON_VALUE ))
//                .andDo( print() )
                .andExpect( status().isOk() );
    }

    @Test
    @DisplayName( "회원 조회 (유효하지 않은 id)" )
    public void getByInValidId() throws Exception {
        mockMvc.perform( get( AccountAPI.BASE + "/{id}", -1L )
                .accept( MediaTypes.HAL_JSON_VALUE ))
//                .andDo( print() )
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName( "회원 수정 (유효한 id)" )
    public void updateByValidId() throws Exception {
        //given
        UpdateRequest updateRequest = UpdateRequest.builder()
                .password( "updatePassword" )
                .userName( "updateUserName" )
                .picture( "updatePicture" )
                .roleType( "user" )
                .build();

        //when
        mockMvc.perform( put( AccountAPI.BASE + "/{id}", 1L )
                .accept( MediaTypes.HAL_JSON_VALUE )
                .contentType( MediaTypes.HAL_JSON_VALUE )
                .content( objectMapper.writeValueAsString( updateRequest ) ) )
//                .andDo( print() )
                .andExpect( status().isOk() );
    }

    @Test
    @DisplayName( "회원 수정 (유효하지 않은 id)" )
    public void updateByInValidId() throws Exception {
        UpdateRequest updateRequest = UpdateRequest.builder()
                .password( "updatePassword" )
                .userName( "updateUserName" )
                .picture( "updatePicture" )
                .roleType( "user" )
                .build();

        mockMvc.perform( put( AccountAPI.BASE + "/{id}", -1L )
                .accept( MediaTypes.HAL_JSON_VALUE )
                .contentType( MediaTypes.HAL_JSON_VALUE )
                .content( objectMapper.writeValueAsString( updateRequest ) ) )
//                .andDo( print() )
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName( "회원 수정 (session 에 있는 이메일 정보가 DB에 존재할 경우)" )
    public void updateWithValidSession() throws Exception {
        //given
        User jin = User.builder()
                .email( "jin@naver.com" )
                .userName( "jin" )
                .password( passwordEncoder.encode( "1234" ) )
                .roleType( RoleType.USER ).build();
        userRepository.save( jin );

        HashMap<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put( SessionDefine.USER.getName(), jin.toDTO() );
        UpdateRequest hak = UpdateRequest.builder()
                .userName( "hak" ).build();

        //when
        mockMvc.perform( put( AccountAPI.BASE )
                .accept( MediaTypes.HAL_JSON_VALUE )
                .contentType( MediaTypes.HAL_JSON_VALUE )
                .content( objectMapper.writeValueAsString( hak ) )
                .sessionAttrs( sessionAttrs ) )
                .andExpect( status().isOk() );

        //then
        Optional<User> findOpt = userRepository.findByEmail( jin.getEmail() );
        assertThat( findOpt.isPresent() ).isTrue();
        assertThat( findOpt.get().getUserName() ).isEqualTo( "hak" );
    }

    @Test
    @DisplayName( "회원 수정 (session 에 있는 이메일 정보가 DB에 존재하지 않을 경우, 이 경우는 실제로 발생할 수는 없음)" )
    public void updateWithInValidSession() throws Exception {
        //given
        User jin = User.builder()
                .email( "jin@naver.com" )
                .userName( "jin" )
                .password( passwordEncoder.encode( "1234" ) )
                .roleType( RoleType.USER ).build();
        userRepository.save( jin );

        HashMap<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put( SessionDefine.USER.getName(), UserDTO.builder().email( "hak@naver.com" ).build() );
        UpdateRequest hak = UpdateRequest.builder()
                .userName( "hak" ).build();

        //when
        mockMvc.perform( put( AccountAPI.BASE )
                        .accept( MediaTypes.HAL_JSON_VALUE )
                        .contentType( MediaTypes.HAL_JSON_VALUE )
                        .content( objectMapper.writeValueAsString( hak ) )
                        .sessionAttrs( sessionAttrs ) )
                        .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName( "회원 삭제 (유효한 id)" )
    public void deleteByValidId() throws Exception {
        //given
        User savedUser = userRepository.save( User.builder().email( "jin@test.com" ).build() );

        //when
        MvcResult mvcResult = mockMvc.perform( delete( AccountAPI.BASE + "/{id}", savedUser.getId() )
                .accept( MediaTypes.HAL_JSON_VALUE ) )
//                .andDo( print() )
                .andExpect( status().isOk() )
                .andReturn();
        boolean result = Boolean.parseBoolean( mvcResult.getResponse().getContentAsString() );

        assertThat( result ).isTrue();
    }

    @Test
    @DisplayName( "회원 삭제 (유효하지 않은 id)" )
    public void deleteByInValidId() throws Exception {
        //given
        Long invalidId = -1L;

        //when
        MvcResult mvcResult = mockMvc.perform( delete( AccountAPI.BASE + "/{id}", invalidId )
                .accept( MediaTypes.HAL_JSON_VALUE ))
//                .andDo( print() )
                .andExpect( status().isOk() )
                .andReturn();
        boolean result = Boolean.parseBoolean( mvcResult.getResponse().getContentAsString() );

        //then
        assertThat( result ).isFalse();
    }
}
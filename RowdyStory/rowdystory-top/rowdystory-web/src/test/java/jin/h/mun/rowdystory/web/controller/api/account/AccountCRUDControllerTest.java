package jin.h.mun.rowdystory.web.controller.api.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.dto.account.UserUpdateRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountCRUDControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName( "회원 가입 (중복되지 않는 이메일)" )
    public void registerWithUniqueEmail() throws Exception {
        //given
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email( "test@naver.com" )
                .password( "1234" )
                .userName( "test" )
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
    }

    @Test
    @DisplayName( "회원 가입 (중복된 이메일)" )
    public void registerWithNonUniqueEmail() throws Exception {
        //given
        userRepository.save( User.builder().email( "jin@test.com" ).build() );
        UserRegisterRequest jin = UserRegisterRequest.builder()
                .email( "jin@test.com" )
                .password( "1234" )
                .userName( "admin" ).build();

        //when
        mockMvc.perform( post( AccountAPI.BASE )
                .accept( MediaTypes.HAL_JSON_VALUE )
                .contentType( MediaTypes.HAL_JSON_VALUE )
                .content( objectMapper.writeValueAsString( jin ) ) )
//                .andDo( print() )
                .andExpect( status().is5xxServerError() );
    }

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
//                .andDo( print() )
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
        UserUpdateRequest hak = UserUpdateRequest.builder()
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
        UserUpdateRequest hak = UserUpdateRequest.builder()
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
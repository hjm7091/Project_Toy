package jin.h.mun.rowdystory.web.controller.api.advice;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.api.PasswordUpdateRequest;
import jin.h.mun.rowdystory.dto.annotation.Encoding;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class RequestBodyEncodeAdviceTest {

    @Test
    @DisplayName( "필드에 @Encoding 어노테이션이 없는 경우 아무일도 일어나지 않음" )
    public void shouldNotBeEncrypted() {
        //given
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        RequestBodyEncodeAdvice requestBodyEncodeAdvice = new RequestBodyEncodeAdvice( passwordEncoder );
        String email = "test.com";
        UserDTO userDTO = UserDTO.builder().email( email ).build();

        //when
        requestBodyEncodeAdvice.afterBodyRead( userDTO, null, null, null, null );

        //then
        assertThat( userDTO.getEmail() ).isEqualTo( email );
    }

    @Test
    @DisplayName( "필드에 @Encoding 어노테이션이 있는 경우 암호화됨" )
    public void shouldBeEncrypted() {
        //given
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        RequestBodyEncodeAdvice requestBodyEncodeAdvice = new RequestBodyEncodeAdvice( passwordEncoder );
        String password = "1234";
        PasswordUpdateRequest passwordUpdateRequest = PasswordUpdateRequest
                .builder().before( password ).after( password ).build();

        //when
        requestBodyEncodeAdvice.afterBodyRead( passwordUpdateRequest, null, null, null, null );

        //then
        assertThat( passwordUpdateRequest.getAfter() ).isNotEqualTo( password );
    }

    @Test
    @DisplayName( "필드에 @Encoding 어노테이션이 있지만 String 타입이 아닌 경우 아무일도 일어나지 않음" )
    public void nonStringShouldNotBeEncrypted() {
        //given
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        RequestBodyEncodeAdvice requestBodyEncodeAdvice = new RequestBodyEncodeAdvice( passwordEncoder );
        @Getter @Setter
        class Test { @Encoding private int age = 10; }
        Test test = new Test();

        //when
        requestBodyEncodeAdvice.afterBodyRead( test, null, null, null, null );

        //then
        assertThat( test.getAge() ).isEqualTo( 10 );
    }
}
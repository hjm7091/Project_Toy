package jin.h.mun.rowdystory.service.util;

import jin.h.mun.rowdystory.dto.account.UserUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReflectionUtilTest {

    @Test
    @DisplayName( "객체에 필드가 존재할 경우 true, 존재하지 않을 경우 false 반환" )
    public void checkFieldExistence() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().build();

        //when
        boolean passwordExistence = ReflectionUtil.checkFieldExistence( "password", userUpdateRequest );
        boolean ageExistence = ReflectionUtil.checkFieldExistence( "age", userUpdateRequest );

        //then
        assertThat( passwordExistence ).isTrue();
        assertThat( ageExistence ).isFalse();
    }

    @Test
    @DisplayName( "객체에 필드가 존재할 경우 타입 반환, 존재하지 않을 경우 에러 발생" )
    public void getFieldType() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().build();

        //when
        Class<?> passwordType = ReflectionUtil.getFieldType( "password", userUpdateRequest );
        String message = assertThrows( RuntimeException.class, () -> ReflectionUtil.getFieldType( "age", userUpdateRequest ) ).getMessage();

        //then
        assertThat( passwordType ).isEqualTo( String.class );
        assertThat( message ).isEqualTo( "age" );
    }

    @Test
    @DisplayName( "객체에 존재하는 필드 업데이트, 타입 일치 => 값 업데이트 됨" )
    public void changeExistFieldWithMatchedType() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().build();

        //when
        ReflectionUtil.changeField( "password", userUpdateRequest, "4567" );

        //then
        assertThat( userUpdateRequest.getPassword() ).isEqualTo( "4567" );
    }

    @Test
    @DisplayName( "객체에 존재하는 필드 업데이트, 타입 불일치 => 에러 발생" )
    public void changeExistFieldWithNonMatchedType() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().build();

        //when
        String message = assertThrows( RuntimeException.class, () -> ReflectionUtil.changeField( "password", userUpdateRequest, 4567 ) ).getMessage();

        //then
        assertThat( message ).contains( "Can not set" );
    }

    @Test
    @DisplayName( "객체에 존재하지 않는 필드 업데이트 => 에러 발생" )
    public void changeNonExistField() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().build();
        String fieldName = "testField";

        //when
        String message = assertThrows( RuntimeException.class, () -> ReflectionUtil.changeField( fieldName, userUpdateRequest, "4567" ) ).getMessage();

        //then
        assertThat( message ).isEqualTo( fieldName );
    }

    @Test
    @DisplayName( "객체의 field 에 자바 네이밍 규칙에 맞는 getter 메서드 존재 O => 값 반환됨" )
    public void invokeExistGetterMethod() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .password( "1234" ).build();

        //when
        Object password = ReflectionUtil.invokeGetterMethod( "password", userUpdateRequest );

        //then
        assertThat( password ).isEqualTo( "1234" );
    }

    @Test
    @DisplayName( "객체의 field 에 자바 네이밍 규칙에 맞는 getter 메서드 존재 X => 에러 발생" )
    public void invokeNonExistGetterMethod() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().build();
        String fieldName = "test";

        //when
        String message = assertThrows( IllegalStateException.class, () -> ReflectionUtil.invokeGetterMethod( fieldName, userUpdateRequest ) ).getMessage();

        //then
        assertThat( message ).contains( fieldName );
    }

}
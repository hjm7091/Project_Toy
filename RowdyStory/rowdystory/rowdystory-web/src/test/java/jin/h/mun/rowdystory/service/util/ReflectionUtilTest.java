package jin.h.mun.rowdystory.service.util;

import jin.h.mun.rowdystory.dto.account.api.UpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReflectionUtilTest {

    @Test
    @DisplayName( "객체에 필드가 존재할 경우 true, 존재하지 않을 경우 false 반환" )
    public void checkFieldExistence() {
        //given
        UpdateRequest updateRequest = UpdateRequest.builder().build();

        //when
        boolean passwordExistence = ReflectionUtil.checkFieldExistence( "password", updateRequest);
        boolean ageExistence = ReflectionUtil.checkFieldExistence( "age", updateRequest);

        //then
        assertThat( passwordExistence ).isTrue();
        assertThat( ageExistence ).isFalse();
    }

    @Test
    @DisplayName( "객체에 필드가 존재할 경우 타입 반환, 존재하지 않을 경우 에러 발생" )
    public void getFieldType() {
        //given
        UpdateRequest updateRequest = UpdateRequest.builder().build();

        //when
        Class<?> passwordType = ReflectionUtil.getFieldType( "password", updateRequest);
        String message = assertThrows( RuntimeException.class, () -> ReflectionUtil.getFieldType( "age", updateRequest) ).getMessage();

        //then
        assertThat( passwordType ).isEqualTo( String.class );
        assertThat( message ).isEqualTo( "age" );
    }

    @Test
    @DisplayName( "set(1.private field : 에러 발생, 2.타입 불일치 : 에러 발생, 3.타입 일치 : 값 업데이트 됨)" )
    public void setValueOfField() throws NoSuchFieldException {
        //given
        class Test {
            private String field1;
            public String field2;
        }
        Test test = new Test();
        Field field1 = test.getClass().getDeclaredField( "field1" );
        Field field2 = test.getClass().getDeclaredField( "field2" );

        //when
        String message1 = assertThrows( RuntimeException.class, () -> ReflectionUtil.setValueOfField( field1, test, "4567" ) ).getMessage().toLowerCase();
        String message2 = assertThrows( RuntimeException.class, () -> ReflectionUtil.setValueOfField( field2, test, 4567 ) ).getMessage().toLowerCase();
        ReflectionUtil.setValueOfField( field2, test, "4567" );

        //then
        assertThat( message1 ).contains( "can not access" );
        assertThat( message2 ).contains( "can not set" );
        assertThat( test.field2 ).isEqualTo( "4567" );
    }

    @Test
    @DisplayName( "get(1.private field : 에러 발생, 2.public field : 값 반환)" )
    public void getValueOfField() throws NoSuchFieldException {
        //given
        class Test {
            private String field1 = "1234";
            public String field2 = "1234";
        }
        Test test = new Test();
        Field field1 = test.getClass().getDeclaredField( "field1" );
        Field field2 = test.getClass().getDeclaredField( "field2" );

        //when
        String message = assertThrows( RuntimeException.class, () -> ReflectionUtil.getValueOfField( field1, test ) ).getMessage().toLowerCase();
        String value = ( String ) ReflectionUtil.getValueOfField( field2, test );

        //then
        assertThat( message ).contains( "can not access" );
        assertThat( value ).isEqualTo( test.field2 );
    }

//    @Test
//    @DisplayName( "객체의 field 에 자바 네이밍 규칙에 맞는 getter 메서드 존재 O => 값 반환됨" )
//    public void invokeExistGetterMethod() {
//        //given
//        UpdateRequest updateRequest = UpdateRequest.builder()
//                .password( "1234" ).build();
//
//        //when
//        Object password = ReflectionUtil.invokeGetterMethod( "password", updateRequest);
//
//        //then
//        assertThat( password ).isEqualTo( "1234" );
//    }
//
//    @Test
//    @DisplayName( "객체의 field 에 자바 네이밍 규칙에 맞는 getter 메서드 존재 X => 에러 발생" )
//    public void invokeNonExistGetterMethod() {
//        //given
//        UpdateRequest updateRequest = UpdateRequest.builder().build();
//        String fieldName = "test";
//
//        //when
//        String message = assertThrows( IllegalStateException.class, () -> ReflectionUtil.invokeGetterMethod( fieldName, updateRequest) ).getMessage();
//
//        //then
//        assertThat( message ).contains( fieldName );
//    }

}
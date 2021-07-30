package jin.h.mun.rowdystory.service.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    private ReflectionUtil() {}

    public static boolean checkFieldExistence( String fieldName, Object target ) {
        try {
            Field field = target.getClass().getDeclaredField( fieldName );
            return true;
        } catch ( NoSuchFieldException e ) {
            return false;
        }
    }

    public static Class<?> getFieldType( String fieldName, Object target ) {
        try {
            return target.getClass().getDeclaredField( fieldName ).getType();
        } catch ( NoSuchFieldException e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }

    public static void changeField( String fieldName, Object target, Object value ) {
        try {
            Field field = target.getClass().getDeclaredField( fieldName );
            field.setAccessible( true );
            field.set( target, value );
        } catch ( NoSuchFieldException | IllegalAccessException e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }

    public static Object invokeGetterMethod( String fieldName, Object target ) {
        try {
            Method[] methods = target.getClass().getDeclaredMethods();
            for ( Method method : methods ) {
                String methodName = method.getName().toLowerCase();
                int parameterCount = method.getParameterCount();
                if ( methodName.startsWith( "get" ) && methodName.endsWith( fieldName ) && parameterCount == 0 ) {
                    return method.invoke( target );
                }
            }
        } catch ( InvocationTargetException | IllegalAccessException e ) {
            throw new RuntimeException( e.getMessage(), e );
        }

        throw new IllegalStateException( fieldName + "'s getter method doesn't exist." );
    }

}

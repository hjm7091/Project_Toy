package jin.h.mun.rowdystory.service.util;

import java.lang.reflect.Field;

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

    public static void setValueOfField( Field field, Object target, Object value ) {
        try {
            field.set( target, value );
        } catch ( IllegalAccessException e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }

    public static Object getValueOfField( Field field, Object target ) {
        try {
            return field.get( target );
        } catch ( IllegalAccessException e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }

//    public static Object invokeGetterMethod( String fieldName, Object target ) {
//        try {
//            Method[] methods = target.getClass().getDeclaredMethods();
//            for ( Method method : methods ) {
//                String methodName = method.getName().toLowerCase();
//                int parameterCount = method.getParameterCount();
//                if ( methodName.startsWith( "get" ) && methodName.endsWith( fieldName ) && parameterCount == 0 ) {
//                    return method.invoke( target );
//                }
//            }
//        } catch ( InvocationTargetException | IllegalAccessException e ) {
//            throw new RuntimeException( e.getMessage(), e );
//        }
//
//        throw new IllegalStateException( fieldName + "'s getter method doesn't exist." );
//    }

}

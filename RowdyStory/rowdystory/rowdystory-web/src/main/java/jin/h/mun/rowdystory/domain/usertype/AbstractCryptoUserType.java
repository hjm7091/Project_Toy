package jin.h.mun.rowdystory.domain.usertype;

import jin.h.mun.rowdystory.crypto.aes256.AES256Util;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

public abstract class AbstractCryptoUserType implements UserType {

    private static final int[] sqlTypes = { Types.VARCHAR };

    private final String key;

    public AbstractCryptoUserType( String key ) {
        this.key = key;
    }

    @Override
    public int[] sqlTypes() {
        return sqlTypes;
    }

    @Override
    public Class<?> returnedClass() {
        return String.class;
    }

    @Override
    public boolean equals( Object x, Object y ) throws HibernateException {
        return Objects.equals( x, y );
    }

    @Override
    public int hashCode( Object o ) throws HibernateException {
        return o.hashCode();
    }

    @Override
    public Object nullSafeGet( ResultSet resultSet, String[] names,
                               SharedSessionContractImplementor sharedSessionContractImplementor, Object owner ) throws HibernateException, SQLException {
        final String value = resultSet.getString( names[0] );
        return value == null ? null : AES256Util.decrypt( value, key );
    }

    @Override
    public void nullSafeSet( PreparedStatement preparedStatement, Object value, int index,
                             SharedSessionContractImplementor sharedSessionContractImplementor ) throws HibernateException, SQLException {
        final String val = ( String ) value;
        if ( val == null ) {
            preparedStatement.setNull( index, Types.VARCHAR );
        } else {
            preparedStatement.setString( index, AES256Util.encrypt( val, key ) );
        }
    }

    @Override
    public Object deepCopy( Object o ) throws HibernateException {
        return o;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble( Object o ) throws HibernateException {
        return null;
    }

    @Override
    public Object assemble( Serializable serializable, Object o ) throws HibernateException {
        return null;
    }

    @Override
    public Object replace( Object original, Object target, Object owner ) throws HibernateException {
        return original;
    }
}

package jin.h.mun.rowdystory.crypto.aes256;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.util.Base64;

public class AES256Util {

    private AES256Util() {}

    private static final String SECRET_KEY_CONFIG = "PBKDF2WithHmacSHA1";

    private static final String CIPHER_CONFIG = "AES/CBC/PKCS5Padding";

    private static final String ALGORITHM = "AES";

    private static final int SALT_BYTES_SIZE = 20;

    public static String encrypt( String str, String key ) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] saltBytes = new byte[SALT_BYTES_SIZE];
            secureRandom.nextBytes( saltBytes );

            SecretKeySpec secretKeySpec = getSecretKeySpec( key, saltBytes );

            Cipher cipher = Cipher.getInstance( CIPHER_CONFIG );
            cipher.init( Cipher.ENCRYPT_MODE, secretKeySpec );
            AlgorithmParameters parameters = cipher.getParameters();
            byte[] ivBytes = parameters.getParameterSpec( IvParameterSpec.class ).getIV();

            byte[] encryptedBytes = cipher.doFinal( str.getBytes() );

            byte[] buffer = new byte[saltBytes.length + ivBytes.length + encryptedBytes.length];
            System.arraycopy( saltBytes, 0, buffer, 0, saltBytes.length );
            System.arraycopy( ivBytes, 0, buffer, saltBytes.length, ivBytes.length );
            System.arraycopy( encryptedBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedBytes.length );

            return Base64.getEncoder().encodeToString( buffer );
        } catch ( Exception e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }

    public static String decrypt( String str, String key ) {
        try {
            Cipher cipher = Cipher.getInstance( CIPHER_CONFIG );
            ByteBuffer buffer = ByteBuffer.wrap( Base64.getDecoder().decode( str ) );

            byte[] saltBytes = new byte[SALT_BYTES_SIZE];
            buffer.get( saltBytes, 0, saltBytes.length );
            byte[] ivBytes = new byte[cipher.getBlockSize()];
            buffer.get( ivBytes, 0, ivBytes.length );
            byte[] encryptedBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes.length];
            buffer.get( encryptedBytes );

            SecretKeySpec secretKeySpec = getSecretKeySpec( key, saltBytes );

            cipher.init( Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec( ivBytes ) );

            return new String( cipher.doFinal( encryptedBytes ) );
        } catch ( Exception e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }

    private static SecretKeySpec getSecretKeySpec( String key, byte[] saltBytes ) throws Exception {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance( SECRET_KEY_CONFIG );
        PBEKeySpec pbeKeySpec = new PBEKeySpec( key.toCharArray(), saltBytes, 65536, 256 );

        SecretKey secretKey = secretKeyFactory.generateSecret( pbeKeySpec );
        return new SecretKeySpec( secretKey.getEncoded(), ALGORITHM );
    }
}

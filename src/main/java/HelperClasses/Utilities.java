package HelperClasses;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * This class contains static methods as helper functions for various operations.
 */
public final class Utilities
{
    /**TODO-Gchkh16 will be used for password hashing
     * @param data string to hash
     * @return hashed string
     */
    public static String md5Hash(String data)
    {

        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = data.getBytes(Charset.defaultCharset());
            byte[] hashed = digest.digest(bytes);
            return new String(hashed,Charset.defaultCharset());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }


        return null;
    }
}

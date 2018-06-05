package HelperClasses;

public class Constraints
{

    /**
     * Timeout for each user session in minutes. When timeout expires, user logs out automatically.
     */
    public static final int SESSION_TIMEOUT = 5 * 60;

    /**
     * Attribute name for userdao object in servlet context
     */
    public static final String USERDAO_NAME = "UserDAo";
}

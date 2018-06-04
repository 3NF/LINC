package HelperClasses;

public class Constraints
{

    /**
     * Timeout for each user session in minutes. When timeout expires, user logs out automatically.
     */
    public static final int SESSION_TIMEOUT = 5 * 60;


    /**
     * Max inactivity time for logged user in minutes. When timeout expires, user logs out automatically.
     */
    public static final int MAX_INACTIVE_TIME = 15;
}

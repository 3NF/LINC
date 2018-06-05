package HelperClasses;

import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Properties;

public class EmailSender
{
    final String FROM = "";


    public static void sendVerification(String userFirstName, String userMail, String uuid)
    {
        String title = "Welcome to LINC, " + userFirstName + "!<br>";
        String body = "Your account was succesfully created! <br>" +
                " Please go to following URL to confirm E-Mail: \n" +
                "<a>" + uuid + "</a>";

        // Todo-Gchkh resume
    }
}

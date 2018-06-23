package Models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Might be temporary class which currently gets
 * info from requested files. In future it will need
 * connection with database to pull the info
 */

public class CodeItem {

    private User user;
    private String name;
    private String data;

    /**
     * Static function which generates new CodeItems.
     * it just throws NullPointerException.
     * @param user User object
     * @param name Name of the code
     * @return new CodeItem Object for requested user and code name
     */

    public static CodeItem generateCodeItem (User user, String name) {
        CodeItem codeItem = new CodeItem(user, name);
        if (codeItem.data == null) {
            System.out.println("Threre was no data for " +  user.getEmail() + ", " + name);
            return null;
        }

        return codeItem;
    }

    private CodeItem (User user, String name) {
        this.name = name;
        this.user = user;
        getData ();
    }

    /**
        Temporarily this function doesn't
        interacts with database. Instead it tries
        to read file in some directory
     */
    private void getData () {
        String pathPrefix = "CodeFiles/";
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(pathPrefix + name));
            while (scanner.hasNext()) {
                stringBuilder.append(scanner.nextLine() + '\n');
            }
            scanner.close();
            data = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            data = null;

        }
    }

    @Override
    public String toString() {
        return data;
    }
}

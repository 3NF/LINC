package Database;

import Models.User;
import java.util.HashMap;

public class CodeManager {

    /*
        HashSet for storing CodeItems
        This way only first file creates CodeItem object.
     */
    private HashMap <String, CodeItem> codeItems;
    private User user;

    public CodeManager (User user) {
        this.user = user;
        this.codeItems = new HashMap<>();
    }


    /**
     * Gets requested file.
     * @param name name of file
     * @return Text corresponding for this file
     * @throws NullPointerException in case there is no data for this file
     */
    public String get(String name) throws NullPointerException {
        CodeItem codeItem;

        if (name.equals("big_file.cpp")) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (codeItems.containsKey(name)) {
            codeItem = codeItems.get(name);
        } else {
            codeItem = CodeItem.generateCodeItem(user, name);
            codeItems.put(name, codeItem);
        }
        if (codeItem == null) {
            throw new NullPointerException();
        }

        return codeItem.toString();
    }
}

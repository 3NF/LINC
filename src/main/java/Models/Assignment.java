package Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Class that represents info of already corrected assignment
 */
public class Assignment {
    private String id;
    private String name;
    List<String> fileNames;

    public Assignment(String id, String name) {
        fileNames = new ArrayList<>();
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addFileNames(List<String> names) {
        fileNames.addAll(names);
    }
}

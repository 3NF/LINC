package Models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class File {

    public static final String FILE_NAME = "file-name";
    public static final String CONTENT = "content";

    private String fileName;
    public String getFileName() {
        return fileName;
    }

    private String content;
    public String getContent(){
        return content;
    }

    public File(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

    @Override
    public String toString() {
        JsonObject res = new JsonObject();

        res.addProperty(FILE_NAME, fileName);
        res.addProperty(CONTENT, content);
        
        return new Gson().toJson(res);

    }

}

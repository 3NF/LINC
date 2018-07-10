package Models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class File {

    public static final String FILE_NAME = "file-name";
    public static final String CONTENT = "content";
    public static final String USER = "user";

    private String fileName;
    public String getFileName() {
        return fileName;
    }
    private String content;
    public String getContent(){
        return content;
    }
    private String userID;
    public String getUserID(){return userID;}

    public File(String fileName, String content,String userID) {
        this.fileName = fileName;
        this.content = content;
        this.userID = userID;
    }

    @Override
    public String toString() {
        JsonObject res = new JsonObject();

        res.addProperty(FILE_NAME, fileName);
        res.addProperty(CONTENT, content);
        res.addProperty(USER,userID);
        return new Gson().toJson(res);

    }

}

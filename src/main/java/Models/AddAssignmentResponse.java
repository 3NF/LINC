package Models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class AddAssignmentResponse {

    public static final String MISSING_FILES = "missing";
    public static final String EXTRA_FILES = "extra";
    public static final String ERROR_MESSAGE = "error-message";


    public enum ErrorMessage {
        InvalidUser,
        AssignmentForbidden,
        WrongFiles,
        Success
    }

    /**
     *
     */
    private ErrorMessage message;
    public void setMessage(ErrorMessage message) {
        this.message = message;
    }

    /**
     *
     */
    private List<String> missingFiles;
    public void setMissingFiles(List<String> missingFiles) {
        this.missingFiles = new ArrayList<>();
        this.missingFiles.addAll(missingFiles);
    }

    /**
     *
     */
    private List<String> extraFiles;
    public void setExtraFiles(List<String> extraFiles) {
        this.extraFiles = new ArrayList<>();
        this.extraFiles.addAll(extraFiles);
    }

    @Override
    public String toString() {
        JsonObject res = new JsonObject();
        JsonParser parser = new JsonParser();

        res.addProperty(ERROR_MESSAGE, String.valueOf(message));
        Gson gsonBuilder = new GsonBuilder().create();

        res.add(MISSING_FILES, parser.parse(gsonBuilder.toJson(missingFiles)));
        res.add(EXTRA_FILES, parser.parse(gsonBuilder.toJson(extraFiles)));

        return new Gson().toJson(res);

    }
}

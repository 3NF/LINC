package Models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static Data.Constraints.*;

public class AddAssignmentResponse {

    public enum ErrorMessage {
        InvalidUser,
        AssignmentForbidden
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

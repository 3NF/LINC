package HelperClasses;

import Database.UploadedAssignment;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class UploadedAssignmentsAdapter extends TypeAdapter<UploadedAssignment> {
    @Override
    public void write(JsonWriter out, UploadedAssignment value) throws IOException {

    }

    @Override
    public UploadedAssignment read(JsonReader in) throws IOException {

        UploadedAssignment res = new UploadedAssignment();
        return new UploadedAssignment();

    }
}

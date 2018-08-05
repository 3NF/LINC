package Models;


import java.util.HashMap;
import java.util.Iterator;

public class UploadedAssignment implements Iterable<File>{


    private String assignmentID;
    public String getAssignmentID() {
        return assignmentID;
    }

    private HashMap<String, File> data;

    public UploadedAssignment(String assignmentID) {
        data = new HashMap<>();
        this.assignmentID = assignmentID;
    }

    public void addAssignmentFile(File file) {
        data.put(file.getFileName(),file);
    }

    public int size(){
        return data.size();
    }


    @Override
    public Iterator<File> iterator() {
        Iterator<String> it = data.keySet().iterator();
        return new Iterator<File>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public File next() {
                if (it.hasNext()) {
                    String fileName = it.next();
                    return new File(fileName, data.get(fileName).getContent(),data.get(fileName).getUserID());
                } else {
                    return null;
                }
            }
        };
    }

}

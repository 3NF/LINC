package Models;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class UploadedAssignment implements Iterable{


    String assignmentID;
    public String getAssignmentID() {
        return assignmentID;
    }

    HashMap<String, File> data;

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
    public Iterator iterator() {
        Iterator it = data.keySet().iterator();
        return new Iterator() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public File next() {
                if (it.hasNext()) {
                    String fileName = (String) it.next();
                    return new File(fileName, data.get(fileName).getContent(),data.get(fileName).getUserID());
                } else {
                    return null;
                }
            }
        };
    }

}

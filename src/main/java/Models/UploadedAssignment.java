package Models;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class UploadedAssignment implements Iterable{


    String assignmentID;
    public String getAssignmentID() {
        return assignmentID;
    }

    HashMap<String, String> data;

    public UploadedAssignment(String assignmentID) {
        data = new HashMap<>();
        this.assignmentID = assignmentID;
    }

    public void addAssignmentFile(File file) {
        data.put(file.getFileName(), file.getContent());
    }

    public static void main(String[] args) {
        UploadedAssignment ass = new UploadedAssignment("1");
        ass.addAssignmentFile(new File("1", "2"));
        ass.addAssignmentFile(new File("2", "3"));
        ass.addAssignmentFile(new File("3", "4"));

        for (Object file: ass) {
            System.out.println(((File)file).getContent());
        }

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
                    return new File(fileName, data.get(fileName));
                } else {
                    return null;
                }
            }
        };
    }
}

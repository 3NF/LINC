package Database;

import java.util.List;
import java.util.Set;

public class AssignmentInfoDAO {

    ConnectionPool connectionPool;

    public AssignmentInfoDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<String> getAssignmentFilesNames(String classID, String assignmentID) {
        return null;
    }

}

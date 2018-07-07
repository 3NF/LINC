package Database;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AssignmentInfoDAO {

    ConnectionPool connectionPool;

    public AssignmentInfoDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     *
     * Gets names of files for assignment, which must be assigned
     *
     * @param classroomId           id of classroom
     * @param assignmentId          id of assignment
     * @return                      List of Strings containing file names
     */
    public List<String> getAssignmentFilesNames(String classroomId, String assignmentId) {
        return null;
    }

    /**
     *
     * Gets ids of assignments which already have assigned to teacher-assistants for checking
     *
     * @param classroomId           id if classroom
     * @return                      List of strings containing assignment ids
     */
    public List<String> getAssignmentIds(String classroomId){
        return Arrays.asList(new String[] {"12", "13", "14"});
    }

}

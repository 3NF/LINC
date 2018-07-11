package Database;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class AssignmentInfoDAOTest {
    @Test
    public void test1() throws SQLException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        AssignmentInfoDAO dao = new AssignmentInfoDAO(ConnectionPool.getInstance());
        System.err.println(dao.getAssignmentFilesPath("105303857051815287047","123123123123"));
    }
}
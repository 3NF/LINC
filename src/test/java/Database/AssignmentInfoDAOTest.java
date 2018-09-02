package Database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static Database.Config.*;

public class AssignmentInfoDAOTest {
    private MysqlDataSource source;

    @Before
    public void createSectionDAO() {
        source = new MysqlDataSource();
        source.setServerName(MYSQL_DATABASE_SERVER);
        source.setDatabaseName(MYSQL_DATABASE_NAME);
        source.setUser(MYSQL_USERNAME);
        source.setPassword(MYSQL_PASSWORD);
    }


    @Test
    public void test1() throws SQLException {
        AssignmentInfoDAO dao = new AssignmentInfoDAO(ConnectionPool.getInstance());
        System.err.println(dao.getAssignmentFilesPath("105303857051815287047","123123123123"));
    }

    @Test
    public void addGradeTest(){
        String userID = "115209239224583784484";
        String assignmentID = "12373837357";
        String grade = AssignmentInfoDAO.Grade.CheckPlus.getName();
        AssignmentInfoDAO assdao = new AssignmentInfoDAO(source);
        assdao.updateGrade(userID , grade , assignmentID);
    }

    @Test
    public void getGradeTest(){
        String userID = "115209239224583784484";
        String assignmentID = "12373837357";
        AssignmentInfoDAO assdao = new AssignmentInfoDAO(source);
        String grade = assdao.getGrade(userID , assignmentID);
        System.out.println(grade);
    }


    @Test
    public void getAssignmentFilesPath() {
    }

    @Test
    public void getAssignmentIds() {
    }

    @Test
    public void addAssignment() {
    }

    @Test
    public void addGrade() {
    }

    @Test
    public void getGrade() {
    }

    @Test
    public void updateGrade() {
    }

    @Test
    public void getUsersGrades() {
    }

    @Test
    public void getAssignmentsGrades() {
        AssignmentInfoDAO dao = new AssignmentInfoDAO(source);
	    System.out.println(dao.getAssignmentsGrades("15924932251", "106052993686948851837"));
    }
}
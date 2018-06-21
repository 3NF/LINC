package Database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static Database.Config.*;

public class SuggestionDAO {

    private final MysqlDataSource connectionPool;

    /**
     * contructor of StudentDAo class
     *
     * @param connectionPool
     */


    public SuggestionDAO(MysqlDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<Reply> getSuggestionReplys(String id){
        Connection connection;
        ArrayList<Reply> suggestionsReply = new ArrayList<>();
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            System.err.println("error in get Connection");
            return null;
        }

        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM " + Config.MYSQL_DATABASE_NAME + ".reply WHERE suggestionID=" + id + " order by id";
            ResultSet result = statement.executeQuery(query);
            while (result.next()){
                String replyId = result.getString("id");
                String userId = result.getString("userId");
                String content = result.getString("text");
                String suggestionId = result.getString("suggestionID");
                Date date = new Date(result.getDate("date").getTime());
                    suggestionsReply.add(new Reply(suggestionId,replyId,userId,content,date));
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("error in creation statement");
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return null;
        }


        return suggestionsReply;
    }

    public static void main(String args[]) {
        MysqlDataSource source = new MysqlDataSource();
        source.setServerName(MYSQL_DATABASE_SERVER);
        source.setDatabaseName(MYSQL_DATABASE_NAME);
        source.setUser(MYSQL_USERNAME);
        source.setPassword(MYSQL_PASSWORD);
        SuggestionDAO DAO= new SuggestionDAO(source);
        DAO.getSuggestionReplys("1");
        //System.out.println(u.getEmail());
    }
}

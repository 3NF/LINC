package Database;

import org.apache.commons.dbcp2.BasicDataSource;

public class CodeFilesDAO {
    private final BasicDataSource connectionPool;

    public CodeFilesDAO(BasicDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }
}

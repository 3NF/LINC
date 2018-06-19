package Listeners;

import Data.Constraints;
import Database.DBManager;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlPooledConnection;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static Data.Constraints.USERDAO_NAME;
import static Database.Config.*;

@WebListener()
public class MainListener implements ServletContextListener{

	public MainListener() {
	}


	public void contextInitialized(ServletContextEvent sce) {

	    System.out.println("init");
		DBManager.initDataSource();
	}

    public void contextDestroyed(ServletContextEvent sce) {
	}
}

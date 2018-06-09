package Listeners;

import Data.Constraints;
import Database.UserDAO;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static Data.Constraints.USERDAO_NAME;
import static Database.Config.MYSQL_DATABASE_SERVER;
import static Database.Config.MYSQL_PASSWORD;
import static Database.Config.MYSQL_USERNAME;

@WebListener()
public class WebServerStarted implements ServletContextListener{

	public WebServerStarted() {
	}


	public void contextInitialized(ServletContextEvent sce) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl(MYSQL_DATABASE_SERVER);
		dataSource.setUsername(MYSQL_USERNAME);
		dataSource.setPassword(MYSQL_PASSWORD);
		sce.getServletContext().setAttribute(USERDAO_NAME, new UserDAO(dataSource));
	}

    public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().removeAttribute(USERDAO_NAME);
	}
}

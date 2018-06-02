package HelperClasses.Listeners;

import Database.Config;
import HelperClasses.UserDAo;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener()
public class WebServerStarted implements ServletContextListener {

	public WebServerStarted() {
	}

	public void contextInitialized(ServletContextEvent sce) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl(Config.MYSQL_DATABASE_SERVER);
        ds.setUsername(Config.MYSQL_USERNAME);
        ds.setPassword(Config.MYSQL_PASSWORD);
		sce.getServletContext().setAttribute("user", new UserDAo(ds));
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}
}

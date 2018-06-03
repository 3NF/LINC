package Listeners;

import Database.Config;
import HelperClasses.Constraints;
import HelperClasses.UserDAo;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener()
public class WebServerStarted implements ServletContextListener, HttpSessionListener {

	public WebServerStarted() {
	}


	public void contextInitialized(ServletContextEvent sce) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl(Config.MYSQL_DATABASE_SERVER);
		dataSource.setUsername(Config.MYSQL_USERNAME);
		dataSource.setPassword(Config.MYSQL_PASSWORD);
		sce.getServletContext().setAttribute("UserDAo", new UserDAo(dataSource));
		sce.getServletContext().setSessionTimeout(Constraints.SESSION_TIMEOUT);
	}

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setMaxInactiveInterval(Constraints.MAX_INACTIVE_TIME);
    }

    public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().removeAttribute("UserDAo");
	}
}

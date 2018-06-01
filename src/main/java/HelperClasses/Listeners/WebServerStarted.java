package HelperClasses.Listeners;

import HelperClasses.ConnectionPoolManager;
import HelperClasses.UserDAo;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener()
public class WebServerStarted implements ServletContextListener {

	public WebServerStarted() {
	}

	public void contextInitialized(ServletContextEvent sce) {
		ConnectionPoolManager connectionPoolManager = new ConnectionPoolManager();
		sce.getServletContext().setAttribute("user", new UserDAo(connectionPoolManager));
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}
}

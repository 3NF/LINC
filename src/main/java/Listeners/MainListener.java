package Listeners;

import Database.DBManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@WebListener()
public class MainListener implements ServletContextListener {

	public MainListener() {
	}


	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("init");
		DBManager.initDataSource();

		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		final ScheduledFuture<?> beepHandler = scheduler.scheduleAtFixedRate(new AssignmentDownloader(), 2, 2, TimeUnit.SECONDS);


	}

	public void contextDestroyed(ServletContextEvent sce) {
	}


	private class AssignmentDownloader implements Runnable {

		@Override
		public void run() {

		}
	}


}
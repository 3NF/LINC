package Listeners;

import Database.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static Data.Constraints.ASSIGNMENT_INFO_DAO;
import static Data.Constraints.CODE_FILES_DAO;
import static Data.Constraints.GAPI_MANAGER;


@WebListener()
public class MainListener implements ServletContextListener {

    public MainListener() {
    }


    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(CODE_FILES_DAO, new CodeFilesDAO(ConnectionPool.getInstance()));
        sce.getServletContext().setAttribute(GAPI_MANAGER, GAPIManager.getInstance());
        sce.getServletContext().setAttribute(ASSIGNMENT_INFO_DAO, new AssignmentInfoDAO(ConnectionPool.getInstance()));
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }


    private class AssignmentDownloader implements Runnable {

        @Override
        public void run() {

        }
    }


}

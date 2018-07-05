package Listeners;

import Database.*;
import Database.CodeFilesDAO;
import Database.ConnectionPool;
import Models.CodeFile;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import java.sql.Connection;

import static Data.Constraints.*;


@WebListener()
public class MainListener implements ServletContextListener {

    public MainListener() {
    }


    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(CODE_FILES_DAO, new CodeFilesDAO(ConnectionPool.getInstance()));
        sce.getServletContext().setAttribute(GAPI_MANAGER, GAPIManager.getInstance());
        sce.getServletContext().setAttribute(ASSIGNMENT_INFO_DAO, new AssignmentInfoDAO(ConnectionPool.getInstance()));
        sce.getServletContext().setAttribute(REPLY_DAO, new ReplyDAO(ConnectionPool.getInstance()));
        sce.getServletContext().setAttribute(VALIDATE_DAO, new ValidateDAO(ConnectionPool.getInstance()));
        sce.getServletContext().setAttribute(SUGGESTION_DAO, new SuggestionDAO(ConnectionPool.getInstance()));
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }


}

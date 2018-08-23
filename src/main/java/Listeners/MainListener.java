package Listeners;

import Database.*;
import Database.CodeFilesDAO;
import Database.ConnectionPool;
import Sockets.ReplySocket;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import static Data.Constraints.*;


@WebListener()
public class MainListener implements ServletContextListener {

    public MainListener() {
    }


    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(GAPI_MANAGER, GAPIManager.getInstance());

        sce.getServletContext().setAttribute(USER_STORAGE, new UserStorage(GAPIManager.getInstance()));
        sce.getServletContext().setAttribute(CODE_FILES_DAO, new CodeFilesDAO(ConnectionPool.getInstance()));
        sce.getServletContext().setAttribute(ASSIGNMENT_INFO_DAO, new AssignmentInfoDAO(ConnectionPool.getInstance()));
        sce.getServletContext().setAttribute(REPLY_DAO, new ReplyDAO(ConnectionPool.getInstance()));
        sce.getServletContext().setAttribute(VALIDATE_DAO, new ValidateDAO(ConnectionPool.getInstance()));
        sce.getServletContext().setAttribute(SUGGESTION_DAO, new SuggestionDAO(ConnectionPool.getInstance()));
        sce.getServletContext().setAttribute(SECTION_DAO, new SectionDAO(ConnectionPool.getInstance()));
        sce.getServletContext().setAttribute(REPLY_SOCKETS, new ConcurrentHashMap<String, Vector<ReplySocket>>());
    }

    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute(GAPI_MANAGER);

        sce.getServletContext().removeAttribute(USER_STORAGE);
        sce.getServletContext().removeAttribute(CODE_FILES_DAO);
        sce.getServletContext().removeAttribute(ASSIGNMENT_INFO_DAO);
        sce.getServletContext().removeAttribute(REPLY_DAO);
        sce.getServletContext().removeAttribute(VALIDATE_DAO);
        sce.getServletContext().removeAttribute(SUGGESTION_DAO);
        sce.getServletContext().removeAttribute(SECTION_DAO);
    }


}

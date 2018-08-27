package Sockets;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import static Data.Constraints.*;

public class DownloadAssignmentConfigurator extends ServerEndpointConfig.Configurator
{
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession)request.getHttpSession();
        config.getUserProperties().put(USER, httpSession.getAttribute(USER));
        config.getUserProperties().put(ASSIGNMENT_INFO_DAO, httpSession.getServletContext().getAttribute(ASSIGNMENT_INFO_DAO));
        config.getUserProperties().put(CODE_FILES_DAO, httpSession.getServletContext().getAttribute(CODE_FILES_DAO));
    }
}
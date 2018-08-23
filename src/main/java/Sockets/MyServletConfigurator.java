package Sockets;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import static Data.Constraints.*;

public class MyServletConfigurator extends ServerEndpointConfig.Configurator
{
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession)request.getHttpSession();
        config.getUserProperties().put(USER, httpSession.getAttribute(USER));
        config.getUserProperties().put(VALIDATE_DAO, httpSession.getServletContext().getAttribute(VALIDATE_DAO));
        config.getUserProperties().put(REPLY_DAO, httpSession.getServletContext().getAttribute(REPLY_DAO));
        config.getUserProperties().put(REPLY_SOCKETS, httpSession.getServletContext().getAttribute(REPLY_SOCKETS));
        config.getUserProperties().put(USER_STORAGE, httpSession.getServletContext().getAttribute(USER_STORAGE));
    }
}
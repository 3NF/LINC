package Models;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/reply_socket/{username}")
public class ReplySocket {

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("username") String username) throws IOException {
        System.out.println(session.getId() + " Socket Opened");
    }

    @OnMessage
    public void onMessage(Session session, String message)
            throws IOException {
        System.out.println(session.getId() + " Socket Message Received");
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println(session.getId() + " Socket Closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private static void broadcast(Object message)
            throws IOException, EncodeException {

    }
}

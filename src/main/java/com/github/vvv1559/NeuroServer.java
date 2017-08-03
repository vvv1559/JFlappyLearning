package com.github.vvv1559;

import com.github.vvv1559.server.NeuralPlayer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;

public class NeuroServer {

    private static final int SERVER_PORT = 8081;

    public static void main(String[] args) throws IOException {
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(SERVER_PORT);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Add a websocket to a specific path spec
        ServletHolder holderEvents = new ServletHolder("ws-events", NeuralPlayer.class);
        context.addServlet(holderEvents, "/play/*");

        try {
            server.start();
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
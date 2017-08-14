package com.github.vvv1559.server;

import com.github.vvv1559.server.NeuralPlayer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;
import java.net.URISyntaxException;

public class NeuroServer {

    private static final int SERVER_PORT = 8081;

    public static void main(String[] args) throws IOException, URISyntaxException {
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(SERVER_PORT);
        server.addConnector(connector);

        HandlerCollection handlerCollection = new HandlerCollection();

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("src/main/webapp/root");
        resourceHandler.setDirectoriesListed(true);

        ContextHandler contextHandler = new ContextHandler("/");
        contextHandler.setHandler(resourceHandler);

        handlerCollection.addHandler(contextHandler);

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(NeuralPlayer.class, "/play/*");

        handlerCollection.addHandler(servletContextHandler);

        server.setHandler(handlerCollection);


        try {
            server.start();
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }

    }
}
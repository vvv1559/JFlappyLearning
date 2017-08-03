package com.github.vvv1559.server;


import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import java.nio.ByteBuffer;

public class NeuralPlayer extends WebSocketServlet {
    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.register(NeuralPlayerAdapter.class);
    }

    public static class NeuralPlayerAdapter extends WebSocketAdapter {

        @Override
        public void onWebSocketConnect(Session sess) {
            super.onWebSocketConnect(sess);
            System.out.println("Socket Connected: " + sess);
        }

        @Override
        public void onWebSocketText(String message) {
            super.onWebSocketText(message);
            System.out.println("Received TEXT message: " + message);
            this.getSession().getRemote().sendStringByFuture("Response from server");
        }

        @Override
        public void onWebSocketBinary(byte[] payload, int offset, int len) {
            super.onWebSocketBinary(payload, offset, len);
            ByteBuffer byteBuffer = ByteBuffer.wrap(payload);
            int num = byteBuffer.getInt();
            float val = byteBuffer.getFloat(4);
            System.out.println("Get binary num = " + num + " val = " + val);
        }

        @Override
        public void onWebSocketClose(int statusCode, String reason) {
            super.onWebSocketClose(statusCode, reason);
            System.out.println("Socket Closed: [" + statusCode + "] " + reason);
        }

        @Override
        public void onWebSocketError(Throwable cause) {
            super.onWebSocketError(cause);
            cause.printStackTrace(System.err);
        }
    }

}

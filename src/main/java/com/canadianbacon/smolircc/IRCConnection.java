package com.canadianbacon.smolircc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class IRCConnection {
    private Socket clientSocket;
    private InetSocketAddress endpoint;

    private InputStream inputStream;
    private OutputStream outputStream;

    public IRCConnection(String host, int port, boolean tls, String username, String password) {
        endpoint = new InetSocketAddress(host, port);
        clientSocket = new Socket();

        try {
            clientSocket.connect(endpoint, 30);
        } catch (IOException e) {
            System.out.println("Issue connecting to server: " + e.getMessage());
            System.exit(1);
        }

        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            System.out.println("Issue obtaining streams from socket: " + e.getMessage());
        }

    }
}

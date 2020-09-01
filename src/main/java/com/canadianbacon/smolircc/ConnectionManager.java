package com.canadianbacon.smolircc;

import java.util.HashSet;

public class ConnectionManager {
    private static ConnectionManager instance = null;

    HashSet<IRCConnection> connectionList = new HashSet<>();

    private ConnectionManager(){

    }

    public static ConnectionManager getInstance() {
        if (instance == null)
            instance = new ConnectionManager();
        return instance;
    }

    public IRCConnection makeConnection(String host, int port, boolean tls, String username, String password) {
        IRCConnection temp = new IRCConnection(host, port, tls, username, password);
        connectionList.add(temp);
        return temp;
    }

    public IRCConnection makeConnection(String host, int port, boolean tls) {
        return makeConnection(host, port, tls, null, null);
    }

    public IRCConnection makeConnection(String host, int port) {
        return makeConnection(host, port, false, null, null);
    }
}

package com.mogaleaf.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class OAuthHttpServer {

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);

        server.createContext("/connect", new OauthHttpHandler());
        server.createContext("/work", new ConnectedHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}

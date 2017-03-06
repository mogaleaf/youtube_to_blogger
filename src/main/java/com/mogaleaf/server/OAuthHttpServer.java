package com.mogaleaf.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class OAuthHttpServer {

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(Integer.valueOf(System.getenv("PORT"))), 0);
      //  HttpServer server = HttpServer.create();

        server.createContext("/connect", new OauthHttpHandler());
        server.createContext("/work", new ConnectedHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}

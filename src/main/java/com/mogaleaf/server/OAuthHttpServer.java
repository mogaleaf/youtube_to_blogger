package com.mogaleaf.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class OAuthHttpServer {

    public void start() throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(Integer.valueOf(System.getenv("PORT"))), 0);
        Logger.getLogger("APPBLOG").info("SERVER CREATED");

        server.createContext("/connect", new OauthHttpHandler());
        server.createContext("/work", new ConnectedHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        Logger.getLogger("APPBLOG").info("SERVER STARTED");
        Logger.getLogger("APPBLOG").info(server.getAddress().getHostString());
    }
}

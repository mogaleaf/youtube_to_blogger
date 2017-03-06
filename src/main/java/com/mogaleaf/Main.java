package com.mogaleaf;

import com.google.api.client.util.store.FileDataStoreFactory;
import com.mogaleaf.server.Configuration;
import com.mogaleaf.server.OAuthHttpServer;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {


    public static void main(String args[]) {
        Logger.getLogger("APPBLOG").info("STARTING");
        try {
            File f = new File("token");
            Configuration.DATA_STORE_FACTORY = new FileDataStoreFactory(f);
            new OAuthHttpServer().start();

        } catch (Exception e) {
            Logger.getLogger("APPBLOG").log(Level.SEVERE,"problem",e);
            e.printStackTrace();
        }
    }


}

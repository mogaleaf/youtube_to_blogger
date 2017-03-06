package com.mogaleaf;

import com.google.api.client.util.store.FileDataStoreFactory;
import com.mogaleaf.server.Configuration;
import com.mogaleaf.server.OAuthHttpServer;

import java.io.File;

public class Main {


    public static void main(String args[]) {
        try {
            File f = new File("token");
            Configuration.DATA_STORE_FACTORY = new FileDataStoreFactory(f);
            new OAuthHttpServer().start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

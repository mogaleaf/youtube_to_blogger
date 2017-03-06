package com.mogaleaf.server;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.File;
import java.io.IOException;

// TODO propertie file
public class Configuration {


    public static String Client_Id = "468001009371-mtgdp8msgu6vl9oif8fc2rbo412ulp99.apps.googleusercontent.com";
    public static String Client_Secret = "IzvL_w8X0Zg_oFiq8d_eTv7R";
    /**
     * Global instance of the HTTP transport.
     */
    public static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Global instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    public static String ID = "cecile";

    public static final String CONNECT_URI = "http://localhost:8888/connect?id=";
    public static final String CONNECTED_HANDLER = "http://localhost:8888/work?id=";

    public static final String SCOPE_YOUTUBE = "https://www.googleapis.com/auth/youtube";
    public static final String SCOPE_BLOGGER = "https://www.googleapis.com/auth/blogger";

    public static FileDataStoreFactory DATA_STORE_FACTORY;



    public static String ID_PARAM = "id";
    public static String CODE_PARAM = "code";

    public static String APPLICATION_NAME = "blog";

    public static String BLOG_ID = "29009291853583329";

    public static String KEYWORD_DESCRIPTION = "#BLOG";
}

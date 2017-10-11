package com.mogaleaf.server;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;

// TODO propertie file
public class Configuration {


    public static String Client_Id = System.getenv("ClientId");
    public static String Client_Secret = System.getenv("ClientSecret");
    /**
     * Global instance of the HTTP transport.
     */
    public static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Global instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    public static String ID = "cecile";

    public static final String CONNECT_URI =  System.getenv("SERVER") + "connect?id=";
    public static final String CONNECTED_HANDLER = System.getenv("SERVER") + "work?id=";

    public static final String SCOPE_YOUTUBE = "https://www.googleapis.com/auth/youtube";
    public static final String SCOPE_BLOGGER = "https://www.googleapis.com/auth/blogger";

    public static DataStoreFactory DATA_STORE_FACTORY;



    public static String ID_PARAM = "id";
    public static String CODE_PARAM = "code";

    public static String APPLICATION_NAME = "blog";

    public static String BLOG_ID = "29009291853583329";

    public static String KEYWORD_DESCRIPTION = "#BLOG";

    public static final String  REDIS_PORT = System.getenv("REDIS_PORT");
    public static final String REDIS_HOST = System.getenv("REDIS_HOST");
}

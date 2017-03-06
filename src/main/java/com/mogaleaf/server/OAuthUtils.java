package com.mogaleaf.server;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;

import java.io.IOException;
import java.util.Arrays;

public class OAuthUtils {

    public static GoogleAuthorizationCodeFlow retrieveFlow() throws IOException {
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                Configuration.HTTP_TRANSPORT,
                Configuration.JSON_FACTORY,
                Configuration.Client_Id, Configuration.Client_Secret, Arrays.asList(Configuration.SCOPE_YOUTUBE,Configuration.SCOPE_BLOGGER)).setDataStoreFactory(Configuration.DATA_STORE_FACTORY).build();
        return flow;
    }
}

package com.mogaleaf.server;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OauthHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            Logger.getLogger("APPBLOG").info("connect servlet");
            Map<String, String> queryMap = HttpUtils.queryToMap(httpExchange.getRequestURI().getQuery());
            String id = queryMap.get(Configuration.ID_PARAM);
            if (id == null) {
                id = Configuration.ID;
            }
            Logger.getLogger("APPBLOG").info("id " + id);
            String code = queryMap.get(Configuration.CODE_PARAM);
            Logger.getLogger("APPBLOG").info("code " + code);
            GoogleAuthorizationCodeFlow flow = OAuthUtils.retrieveFlow();
            Credential credential = flow.loadCredential(id);
            if (credential == null && code == null) {
                Logger.getLogger("APPBLOG").info("requestCredentials ");
                requestCredentials(httpExchange, id, flow);
            } else {
                if (code != null) {
                    Logger.getLogger("APPBLOG").info("requestcode ");
                    requestCode(id, code, flow);
                }
                HttpUtils.redirectTo(Configuration.CONNECTED_HANDLER + id, httpExchange);
            }
        }catch(Exception e){
            Logger.getLogger("APPBLOG").log(Level.SEVERE,"prob",e);
        }
    }


    private void requestCode(String id, String code, GoogleAuthorizationCodeFlow flow) throws IOException {
        Logger.getLogger("APPBLOG").info("newTokenRequest " );
        GoogleAuthorizationCodeTokenRequest googleAuthorizationCodeTokenRequest = flow.newTokenRequest(code);
        Logger.getLogger("APPBLOG").info("setRedirectUri " );
        googleAuthorizationCodeTokenRequest.setRedirectUri(Configuration.CONNECT_URI + id);
        Logger.getLogger("APPBLOG").info("execute " );
        GoogleTokenResponse execute = googleAuthorizationCodeTokenRequest.execute();
        Logger.getLogger("APPBLOG").info("store " );
        flow.createAndStoreCredential(execute, id);

    }

    private void requestCredentials(HttpExchange httpExchange, String id, GoogleAuthorizationCodeFlow flow) throws IOException {
        AuthorizationCodeRequestUrl authorizationCodeRequestUrl = flow.newAuthorizationUrl();
        authorizationCodeRequestUrl.setRedirectUri( Configuration.CONNECT_URI + id);
        String url = authorizationCodeRequestUrl.toURL().toString();
        httpExchange.getResponseHeaders().put("Content-Type", Arrays.asList("text/html"));
        HttpUtils.redirectTo(url, httpExchange);
    }


}

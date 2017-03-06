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

public class OauthHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryMap = HttpUtils.queryToMap(httpExchange.getRequestURI().getQuery());
        String id = queryMap.get(Configuration.ID_PARAM);
        if (id == null) {
            id = Configuration.ID;
        }
        String code = queryMap.get(Configuration.CODE_PARAM);
        GoogleAuthorizationCodeFlow flow = OAuthUtils.retrieveFlow();
        Credential credential = flow.loadCredential(id);
        if (credential == null && code == null) {
            requestCredentials(httpExchange, id, flow);
        } else {
            if (code != null) {
                requestCode(id, code, flow);
            }
            HttpUtils.redirectTo(Configuration.CONNECTED_HANDLER + id, httpExchange);
        }
    }


    private void requestCode(String id, String code, GoogleAuthorizationCodeFlow flow) throws IOException {
        GoogleAuthorizationCodeTokenRequest googleAuthorizationCodeTokenRequest = flow.newTokenRequest(code);
        googleAuthorizationCodeTokenRequest.setRedirectUri(Configuration.CONNECT_URI + id);
        GoogleTokenResponse execute = googleAuthorizationCodeTokenRequest.execute();
        flow.createAndStoreCredential(execute, id);

    }

    private void requestCredentials(HttpExchange httpExchange, String id, GoogleAuthorizationCodeFlow flow) throws IOException {
        AuthorizationCodeRequestUrl authorizationCodeRequestUrl = flow.newAuthorizationUrl();
        authorizationCodeRequestUrl.setRedirectUri(Configuration.CONNECT_URI + id);
        String url = authorizationCodeRequestUrl.toURL().toString();
        httpExchange.getResponseHeaders().put("Content-Type", Arrays.asList("text/html"));
        HttpUtils.redirectTo(url, httpExchange);
    }


}

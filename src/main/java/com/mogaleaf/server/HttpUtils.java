package com.mogaleaf.server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                } else {
                    result.put(pair[0], "");
                }
            }
        }
        return result;
    }

    public static void redirectTo(String url, HttpExchange httpExchange) throws IOException {
        String redirectionContent = getRedirectionContent(url);
        httpExchange.sendResponseHeaders(302, redirectionContent.length());
        OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write(redirectionContent.getBytes());
        responseBody.close();
    }

    public static String getRedirectionContent(String url) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><head>\n");
        builder.append("<meta http-equiv=\"refresh\" content=\"0; URL=");
        builder.append(url);
        builder.append("\" />\n" +
                "</head>\n" +
                " \n" +
                "<body>\n" +
                "</body>\n" +
                " \n" +
                "</html>");
        return builder.toString();
    }
}

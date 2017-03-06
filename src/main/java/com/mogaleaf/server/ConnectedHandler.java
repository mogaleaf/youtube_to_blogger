package com.mogaleaf.server;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.model.Post;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.mogaleaf.model.YouTubeOwnVideo;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConnectedHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            Map<String, String> queryMap = HttpUtils.queryToMap(httpExchange.getRequestURI().getQuery());
            String id = queryMap.get(Configuration.ID_PARAM);
            if (id == null) {
                id = Configuration.ID;
            }
            GoogleAuthorizationCodeFlow flow = OAuthUtils.retrieveFlow();
            Credential credential = flow.loadCredential(id);
            if (credential == null) {
                HttpUtils.redirectTo(Configuration.CONNECT_URI + id, httpExchange);
            }
            YouTube youtube = new YouTube.Builder(Configuration.HTTP_TRANSPORT, Configuration.JSON_FACTORY, credential).setApplicationName(
                    Configuration.APPLICATION_NAME).build();
            List<YouTubeOwnVideo> youTubeOwnVideos = retrieveYoutube(youtube);
            postOnBlogger(credential, youTubeOwnVideos);
            deleteVideosDescription(youtube, youTubeOwnVideos);
            String fin = "fin";
            httpExchange.sendResponseHeaders(200, fin.length());
            OutputStream responseBody = httpExchange.getResponseBody();
            responseBody.write(fin.getBytes());
            responseBody.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void deleteVideosDescription(YouTube youtube, List<YouTubeOwnVideo> youTubeOwnVideos) throws IOException {
        for (YouTubeOwnVideo youTubeOwnVideo : youTubeOwnVideos) {
            deleteVideoDescription(youtube, youTubeOwnVideo);
        }
    }

    private void deleteVideoDescription(YouTube youtube, YouTubeOwnVideo youTubeOwnVideo) throws IOException {
        YouTube.Videos.List listVideosRequest = youtube.videos().list("snippet").setId(youTubeOwnVideo.id);
        VideoListResponse listResponse = listVideosRequest.execute();
        Video video = listResponse.getItems().get(0);
        VideoSnippet snippet = video.getSnippet();
        snippet.setDescription("");
        YouTube.Videos.Update updateVideosRequest = youtube.videos().update("snippet", video);
        updateVideosRequest.execute();
    }

    private void postOnBlogger(Credential credential,List<YouTubeOwnVideo> youTubeOwnVideos) throws IOException {
        Blogger blogger = new Blogger.Builder(Configuration.HTTP_TRANSPORT, Configuration.JSON_FACTORY, credential).setApplicationName(
                Configuration.APPLICATION_NAME).build();
        for (YouTubeOwnVideo idVideoEntry : youTubeOwnVideos) {
            post(blogger,idVideoEntry);
        }
    }

    private void post(Blogger blogger,YouTubeOwnVideo video) throws IOException {
        Post content = new Post();
        content.setTitle(video.title);
        content.setContent(retrieveHtmlContent(video));
        // The request action.
        Blogger.Posts.Insert postsInsertAction = blogger.posts()
                .insert(Configuration.BLOG_ID, content);

// Restrict the result content to just the data we need.
        postsInsertAction.setFields("author/displayName,content,published,title,url");

// This step sends the request to the server.
        postsInsertAction.execute();
    }

    private String retrieveHtmlContent(YouTubeOwnVideo video) {
        return "<iframe width=\"640\" height=\"360\" src=\"https://www.youtube.com/embed/" + video.id + "?rel=0&amp;controls=0&amp;showinfo=0\" frameborder=\"0\" allowfullscreen></iframe>";
    }

    private List<YouTubeOwnVideo> retrieveYoutube(YouTube youtube) throws IOException {
        List<YouTubeOwnVideo> idVideoNameMap = new ArrayList<>();

        YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");
        channelRequest.setMine(true);
        channelRequest.setFields("items/contentDetails,nextPageToken,pageInfo");
        ChannelListResponse channelResult = channelRequest.execute();
        if (channelResult != null) {

            List<Channel> channelsList = channelResult.getItems();
            if (channelsList != null) {
                String uploadPlaylistId = channelsList.get(0).getContentDetails().getRelatedPlaylists().getUploads();
                // Define a list to store items in the list of uploaded videos.
                List<PlaylistItem> playlistItemList = new ArrayList<>();

                // Retrieve the playlist of the channel's uploaded videos.
                YouTube.PlaylistItems.List playlistItemRequest =
                        youtube.playlistItems().list(
                                "contentDetails,id,snippet,status" );
                playlistItemRequest.setPlaylistId(uploadPlaylistId);

                // Only retrieve data used in this application, thereby making
                // the application more efficient. See:
                // https://developers.google.com/youtube/v3/getting-started#partial
               playlistItemRequest.setFields(
                        "items(snippet(description,title),contentDetails/videoId),nextPageToken,pageInfo");

                String nextToken = "";

                // Call the API one or more times to retrieve all items in the
                // list. As long as the API response returns a nextPageToken,
                // there are still more items to retrieve.
                do {
                    playlistItemRequest.setPageToken(nextToken);
                    PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();
                    playlistItemList.addAll(playlistItemResult.getItems());
                    nextToken = playlistItemResult.getNextPageToken();
                } while (nextToken != null);

                // Prints information about the results.
                for (PlaylistItem playlistItem : playlistItemList) {
                    String description = playlistItem.getSnippet().getDescription();
                    if(description != null && description.equalsIgnoreCase(Configuration.KEYWORD_DESCRIPTION)) {
                        idVideoNameMap.add( createModelVideo(playlistItem));
                    }
                }
            }
        }
        return idVideoNameMap;
    }


    private YouTubeOwnVideo createModelVideo(PlaylistItem playlistItem) {
        YouTubeOwnVideo video = new YouTubeOwnVideo();
        PlaylistItemContentDetails contentDetails = playlistItem.getContentDetails();
        PlaylistItemSnippet snippet = playlistItem.getSnippet();
        video.id = contentDetails.getVideoId();
        video.title = snippet.getTitle();
        return video;
    }

}

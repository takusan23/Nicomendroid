package io.github.takusan23.nicomendroid.JSONParse;

import org.json.JSONException;
import org.json.JSONObject;

public class LiveInfoJSONParse {
    private String response_string;
    private String webScoketUrl;
    private String ver;
    private String service;
    private String chat;
    private String control;
    private String store;
    private String title;
    private String liveId;
    private String thumbnailUrl_icon;
    private String viewers;
    private String comments;
    private String description;
    private String thumbnailUrl;


    public LiveInfoJSONParse(String response){
        response_string = response;
        jsonParse(response_string);
    }

    public String getWebScoketUrl() {
        return webScoketUrl;
    }

    public String getVer() {
        return ver;
    }

    public String getService() {
        return service;
    }

    public String getChat() {
        return chat;
    }

    public String getControl() {
        return control;
    }

    public String getStore() {
        return store;
    }

    public String getTitle() {
        return title;
    }

    public String getLiveId() {
        return liveId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getViewers() {
        return viewers;
    }

    public String getComments() {
        return comments;
    }

    public String getDescription() {
        return description;
    }

    private void jsonParse(String response_string){
        try {
            JSONObject jsonObject = new JSONObject(response_string);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject messageServer = data.getJSONObject("messageServer");
            JSONObject threads = data.getJSONObject("threads");
            //Comment
            webScoketUrl = messageServer.getString("wss");
            ver = messageServer.getString("version");
            service = messageServer.getString("service");
            chat = threads.getString("chat");
            control = threads.getString("control");
            store = threads.getString("store");
            //生放送情報
            title = data.getString("title");
            liveId = data.getString("id");
            description = data.getString("description");
            thumbnailUrl = data.getJSONObject("liveScreenshotThumbnailUrls").getString("large");
            viewers = data.getString("viewers");
            comments = data.getString("comments");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

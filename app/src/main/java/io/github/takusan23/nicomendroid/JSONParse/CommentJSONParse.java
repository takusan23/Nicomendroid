package io.github.takusan23.nicomendroid.JSONParse;

import org.json.JSONException;
import org.json.JSONObject;

public class CommentJSONParse {
    private String response_string;
    private String text="";
    private String premium="";

    public CommentJSONParse(String response_string){
        this.response_string = response_string;
        jsonParse();
    }

    public String getText() {
        return text;
    }

    public String getPremium() {
        return premium;
    }

    /**
     * JSONパース
     * */
    private void jsonParse(){
        try {
            JSONObject jsonObject = new JSONObject(response_string);
            JSONObject chat_JsonObject = jsonObject.getJSONObject("chat");
            text = chat_JsonObject.getString("content");
            if (!chat_JsonObject.isNull("premium")){
                premium = chat_JsonObject.getString("");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

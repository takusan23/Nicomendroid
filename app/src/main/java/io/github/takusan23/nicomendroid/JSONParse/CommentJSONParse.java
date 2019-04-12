package io.github.takusan23.nicomendroid.JSONParse;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.takusan23.nicomendroid.R;

public class CommentJSONParse {
    //private String response_string;
    private Context context;
    private String text = "";
    private String premium = "";
    private String userId = "";
    private String iyayo = "";
    private String mail = "";
    private String date = "";
    private String mobileData = "";

    public CommentJSONParse(Context context, String response_string) {
        //this.response_string = response_string;
        this.context = context;
        jsonParse(response_string);
    }

    public String getText() {
        return text;
    }

    public String getPremium() {
        return premium;
    }

    public String getUserId() {
        return userId;
    }

    public String getIyayo() {
        return iyayo;
    }

    public String getMail() {
        return mail;
    }

    public String getDate() {
        return date;
    }

    public String getMobileData() {
        return mobileData;
    }

    /**
     * JSONパース
     */
    private void jsonParse(String response_string) {
        try {
            JSONObject jsonObject = new JSONObject(response_string);
            JSONObject chat_JsonObject = jsonObject.getJSONObject("chat");
            text = chat_JsonObject.getString("content");
            userId = chat_JsonObject.getString("user_id");
            iyayo = chat_JsonObject.getString("anonymity");
            mail = chat_JsonObject.getString("mail");
            date = chat_JsonObject.getString("date");
            //プレ垢・運営コメ・一般コメ
            if (!chat_JsonObject.isNull("premium")) {
                if (chat_JsonObject.getInt("premium") == 1) {
                    premium = context.getString(R.string.premium_account);
                } else {
                    premium = context.getString(R.string.management);
                }
            } else {
                premium = context.getString(R.string.general_account);
            }
            //モバイルデータ回線から投稿
            if (mail.contains("docomo")){
                mobileData = "docomo";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

package io.github.takusan23.nicomendroid.JSONParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.takusan23.nicomendroid.Fragment.GiftFragment;

public class GiftJSONParse {

    private String user_name="";
    private String item_name="";

    public GiftJSONParse(String response){
        jsonParse(response);
    }

    public String getUser_name() {
        return user_name;
    }

    public String getItem_name() {
        return item_name;
    }

    private void jsonParse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            user_name = jsonObject.getString("advertiserName");
            item_name = jsonObject.getJSONObject("item").getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

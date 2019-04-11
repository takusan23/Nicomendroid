package io.github.takusan23.nicomendroid;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LiveIDActivity extends AppCompatActivity {

    EditText live_id_EditText;
    Button live_id_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_id_activity);

        live_id_EditText = findViewById(R.id.live_id_editText);
        live_id_Button = findViewById(R.id.live_id_button);
        //取得
        getCommentInfo();
    }

    /**
     * コメント取得に使うWebSocketとかを取得する
     */
    private void getCommentInfo() {
        live_id_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //API叩く
                String url = "https://api.cas.nicovideo.jp/v1/services/live/programs/" + live_id_EditText.getText().toString() + "/threads";
                Request request = new Request.Builder().url(url).get().build();
                OkHttpClient client = new OkHttpClient();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        showUIThreadToast(getString(R.string.error));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String response_string = response.body().string();
                        if (!response.isSuccessful()){
                            showUIThreadToast(getString(R.string.error) + " : " + String.valueOf(response.code()));
                        }else {
                            //JSONパース
                            try {
                                JSONObject jsonObject = new JSONObject(response_string);
                                JSONObject data = jsonObject.getJSONObject("data");
                                JSONObject messageServer = data.getJSONObject("messageServer");
                                JSONObject threads = data.getJSONObject("threads");
                                String webSocketURL = messageServer.getString("wss");
                                String version = messageServer.getString("version");
                                String service = messageServer.getString("service");
                                String chat = threads.getString("chat");
                                String control = threads.getString("control");
                                String store = threads.getString("store");
                                //Intent
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(LiveIDActivity.this,CommentViewMainActivity.class);
                                        intent.putExtra("fragment","comment_list");
                                        intent.putExtra("url",webSocketURL);
                                        intent.putExtra("version",version);
                                        intent.putExtra("service",service);
                                        intent.putExtra("chat",chat);
                                        intent.putExtra("control",control);
                                        intent.putExtra("store",store);
                                        startActivity(intent);
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * ToastをUIスレッド以外でも呼べるようにする
     * */
    private void showUIThreadToast(String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LiveIDActivity.this,text,Toast.LENGTH_SHORT).show();
            }
        });
    }


}


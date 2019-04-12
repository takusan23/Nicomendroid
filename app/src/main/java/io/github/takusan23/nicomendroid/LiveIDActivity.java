package io.github.takusan23.nicomendroid;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import io.github.takusan23.nicomendroid.Util.SnackberProgress;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

public class LiveIDActivity extends AppCompatActivity {

    private EditText live_id_EditText;
    private Button live_id_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_id_activity);

        live_id_EditText = findViewById(R.id.live_id_editText);
        live_id_Button = findViewById(R.id.live_id_button);
        //取得
        getCommentInfo();
        //クリップボードから取得？
        getClickBordLiveID();
        //タイトル
        setTitle(getString(R.string.id_activity));
    }

    /**
     * コメント取得に使うWebSocketとかを取得する
     */
    private void getCommentInfo() {
        live_id_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //API叩く
                String url = "https://api.cas.nicovideo.jp/v1/services/live/programs/" + live_id_EditText.getText().toString();
                //くるくる
                SnackberProgress snackberProgress = new SnackberProgress(Snackbar.make(v, getString(R.string.loading) + "\n" + url, Snackbar.LENGTH_INDEFINITE));
                snackberProgress.showSnackBerProgress();
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
                        if (!response.isSuccessful()) {
                            showUIThreadToast(getString(R.string.error) + " : " + String.valueOf(response.code()));
                        } else {
                            //JSONパース
                            Intent intent = new Intent(LiveIDActivity.this, CommentViewMainActivity.class);
                            intent.putExtra("fragment", "comment_list");
                            intent.putExtra("response", response_string);
                            intent.putExtra("id", live_id_EditText.getText().toString());
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    /**
     * ToastをUIスレッド以外でも呼べるようにする
     */
    private void showUIThreadToast(String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LiveIDActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * クリップボード取得
     * 参照 : https://developer.android.com/guide/topics/text/copy-paste
     */
    private void getClickBordLiveID() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.getPrimaryClip()!=null){
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            String pasteData = item.getText().toString();
            //正規表現で生放送IDを取る
            Pattern p = Pattern.compile("lv[0-9]");
            if (p.matcher(pasteData).find()) {
                //https://を消して生放送IDのみにする
                if (pasteData.contains("nicovideo")) {
                    pasteData = pasteData.replace("https://live.nicovideo.jp/watch/", "");
                    pasteData = pasteData.replace("https://live2.nicovideo.jp/watch/", "");
                }
                live_id_EditText.setText(pasteData);
            }
        }
    }

}


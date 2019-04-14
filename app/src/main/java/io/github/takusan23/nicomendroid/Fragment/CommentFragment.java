package io.github.takusan23.nicomendroid.Fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.extensions.IExtension;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.protocols.IProtocol;
import org.java_websocket.protocols.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.github.takusan23.nicomendroid.CommentViewMainActivity;
import io.github.takusan23.nicomendroid.JSONParse.CommentJSONParse;
import io.github.takusan23.nicomendroid.JSONParse.LiveInfoJSONParse;
import io.github.takusan23.nicomendroid.R;
import io.github.takusan23.nicomendroid.RecyclerView.CommentRecyclerViewAdapter;
import io.github.takusan23.nicomendroid.Util.SnackberProgress;
import okhttp3.WebSocket;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    private WebSocketClient webSocketClient;
    private ArrayList<ArrayList> recyclerViewList;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private boolean one_mode = false;
    private LiveInfoJSONParse api;
    private TextToSpeech tts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.comment_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerViewList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.comment_recycler_view);
        //ここから下三行必須
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        CommentRecyclerViewAdapter customMenuRecyclerViewAdapter = new CommentRecyclerViewAdapter(recyclerViewList, "comment");
        recyclerView.setAdapter(customMenuRecyclerViewAdapter);
        recyclerViewLayoutManager = recyclerView.getLayoutManager();
        api = new LiveInfoJSONParse(getArguments().getString("response"));

        //WebSocket
        setConnetCommendWebSocket();
        //一番上に
        setClickScrollTop();
        //タイトル
        setTitle();
        setNavigationDrawer();
    }

    /**
     * WebSocket接続
     */
    private void setConnetCommendWebSocket() {
        //最低限必要なやつ
        String url = api.getWebScoketUrl();
        String chat = api.getChat();
        String service = api.getService();
        String version = api.getVer();
        //接続時最初に送るJSONObject作成
        JSONObject send_JsonObject = new JSONObject();
        try {
            JSONObject thread_JsonObject = new JSONObject();
            thread_JsonObject.put("version", version);
            thread_JsonObject.put("thread", chat);
            thread_JsonObject.put("service", service);
            thread_JsonObject.put("scores", 1); //NGスコアを取得できるようにする
            thread_JsonObject.put("res_from", -100); //10コメント前を取得できるように
            send_JsonObject.put("thread", thread_JsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //くるくる
        SnackberProgress snackberProgress = new SnackberProgress(Snackbar.make(recyclerView, getString(R.string.loading) + "\n" + url, Snackbar.LENGTH_INDEFINITE));
        snackberProgress.showSnackBerProgress();
        //プロトコル？
        Draft_6455 draft_ocppOnly = new Draft_6455(Collections.<IExtension>emptyList(), Collections.<IProtocol>singletonList(new Protocol("msg.nicovideo.jp#json")));
        //WebSocket接続
        try {
            webSocketClient = new WebSocketClient(new URI(url), draft_ocppOnly) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("おーぷん");
                    //送る
                    webSocketClient.send(send_JsonObject.toString());
                    snackberProgress.dismissSnackberProgress();
                }

                @Override
                public void onMessage(String message) {
                    //はじめだけ過去コメが流れてくるのでパース
                    snackberProgress.dismissSnackberProgress();
                    if (!one_mode) {
                        //ここ一度だけ動く
                        one_mode = true;
                        try {
                            JSONArray jsonArray = new JSONArray(message);
                            //最初のJSONObjectはコメント情報じゃない
                            for (int i = 1; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //追加
                                ArrayList<String> item = new ArrayList<>();
                                item.add(jsonObject.toString());
                                recyclerViewList.add(0, item);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //追加
                        ArrayList<String> item = new ArrayList<>();
                        item.add(message);
                        recyclerViewList.add(0, item);
                        CommentJSONParse api = new CommentJSONParse(getContext(), message);
                        //Toast
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (CommentViewMainActivity.isShowToast) {
                                    showToast(api);
                                }
                                //TTS
                                if (CommentViewMainActivity.isTTS) {
                                    //TTSのインスタンス生成
                                    if (tts== null) {
                                        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                                            @Override
                                            public void onInit(int status) {
                                                if (TextToSpeech.SUCCESS == status) {
                                                    //初期化完了
                                                    Toast.makeText(getContext(), getString(R.string.tts_ok), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        tts.setSpeechRate(2);
                                        tts.speak(api.getText(), TextToSpeech.QUEUE_ADD, null, null);
                                    }
                                }
                            }
                        });

                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (((LinearLayoutManager) recyclerViewLayoutManager) != null) {
                                // 画面上で最上部に表示されているビューのポジションとTopを記録しておく
                                int pos = ((LinearLayoutManager) recyclerViewLayoutManager).findFirstVisibleItemPosition();
                                int top = 0;
                                if (((LinearLayoutManager) recyclerViewLayoutManager).getChildCount() > 0) {
                                    top = ((LinearLayoutManager) recyclerViewLayoutManager).getChildAt(0).getTop();
                                }
                                CommentRecyclerViewAdapter customMenuRecyclerViewAdapter = new CommentRecyclerViewAdapter(recyclerViewList, "comment");
                                recyclerView.setAdapter(customMenuRecyclerViewAdapter);
                                //一番上なら追いかける
                                if (pos == 0) {
                                    recyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            recyclerView.smoothScrollToPosition(0);
                                        }
                                    });
                                } else {
                                    ((LinearLayoutManager) recyclerViewLayoutManager).scrollToPositionWithOffset(pos + 1, top);
                                }
                            }
                        }
                    });
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("おわり");
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), getString(R.string.end), Toast.LENGTH_LONG).show();
                                snackberProgress.dismissSnackberProgress();
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception ex) {
                    System.out.println("えらー");
                    ex.printStackTrace();
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                                snackberProgress.dismissSnackberProgress();
                            }
                        });
                    }
                }
            };
            //接続
            webSocketClient.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 一番上に
     */
    private void setClickScrollTop() {
        ((CommentViewMainActivity) getActivity()).getToolbar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //これ一番上に移動するやつ
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    /**
     * タイトル設定
     */
    private void setTitle() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(api.getTitle());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(api.getLiveId());
    }

    /**
     * ナビゲーションドロワー
     */
    private void setNavigationDrawer() {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        //どろわーのイメージとか文字とか
        View navHeaderView = navigationView.getHeaderView(0);
        ImageView back_ImageView = navHeaderView.findViewById(R.id.nav_background);
        //ImageView icon_ImageView = navHeaderView.findViewById(R.id.nav_icon);
        TextView description_TextView = navHeaderView.findViewById(R.id.nav_description);
        TextView viewer_count_TextView = navHeaderView.findViewById(R.id.nav_viewer_count);
        TextView comment_count_TextView = navHeaderView.findViewById(R.id.nav_comment_count);
        //追加
        Glide.with(navHeaderView).load(api.getThumbnailUrl()).into(back_ImageView);
        //Glide.with(navHeaderView).load(api.getThumbnailUrl()).into(icon_ImageView);
        description_TextView.setText(Html.fromHtml(api.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        viewer_count_TextView.setText(" : " + api.getViewers());
        comment_count_TextView.setText(" : " + api.getComments());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //終わったら閉じとく
        if (webSocketClient != null) {
            webSocketClient.close();
        }
        //TTS終了
        if (tts != null) {
            tts.shutdown();
        }
    }

    private void showToast(CommentJSONParse api) {
        //Toastだすか
        if (CommentViewMainActivity.isShowToast) {
            Toast.makeText(getContext(), api.getPremium() + "\n" + api.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    private void startTTS(Context context, CommentJSONParse api) {

    }
}

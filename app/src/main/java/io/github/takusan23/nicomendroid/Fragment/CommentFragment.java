package io.github.takusan23.nicomendroid.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.extensions.IExtension;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.protocols.IProtocol;
import org.java_websocket.protocols.Protocol;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.github.takusan23.nicomendroid.R;
import okhttp3.WebSocket;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {
    View view;
    TextView textView;
    private WebSocketClient webSocketClient;

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

        textView = view.findViewById(R.id.comment_view_textview);
        //WebSocket
        setConnetCommendWebSocket();
    }

    /**
     * WebSocket接続
     */
    private void setConnetCommendWebSocket() {
        //最低限必要なやつ
        String url = getArguments().getString("url");
        String chat = getArguments().getString("chat");
        String service = getArguments().getString("service");
        String version = getArguments().getString("version");
        //接続時最初に送るJSONObject作成
        JSONObject send_JsonObject = new JSONObject();
        try {
            JSONObject thread_JsonObject = new JSONObject();
            thread_JsonObject.put("version", version);
            thread_JsonObject.put("thread", chat);
            thread_JsonObject.put("service", service);
            thread_JsonObject.put("res_from", -10); //10コメント前を取得できるように
            send_JsonObject.put("thread", thread_JsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //プロトコル？
         Draft_6455 draft_ocppOnly = new Draft_6455(Collections.<IExtension>emptyList(), Collections.<IProtocol>singletonList(new Protocol("msg.nicovideo.jp#json")));
        //WebSocket接続
        try {
            webSocketClient = new WebSocketClient(new URI(url),draft_ocppOnly) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("おーぷん");
                    //送る
                    webSocketClient.send(send_JsonObject.toString());
                }

                @Override
                public void onMessage(String message) {
                    //TextViewに入れる
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.append(message);
                            textView.append("\n");
                        }
                    });
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("おわり");

                }

                @Override
                public void onError(Exception ex) {
                    System.out.println("えらー");
                    ex.printStackTrace();
                }
            };
            //接続
            webSocketClient.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //終わったら閉じとく
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}

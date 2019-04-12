package io.github.takusan23.nicomendroid.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.github.takusan23.nicomendroid.JSONParse.GiftJSONParse;
import io.github.takusan23.nicomendroid.R;
import io.github.takusan23.nicomendroid.RecyclerView.CommentRecyclerViewAdapter;
import io.github.takusan23.nicomendroid.Util.SnackberProgress;
import io.github.takusan23.nicomendroid.Util.UIThreadToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class GiftFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ArrayList<ArrayList> recyclerViewList;
    private String liveId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gift, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerViewList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.gift_recycler_view);
        //ここから下三行必須
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        CommentRecyclerViewAdapter customMenuRecyclerViewAdapter = new CommentRecyclerViewAdapter(recyclerViewList,"gift");
        recyclerView.setAdapter(customMenuRecyclerViewAdapter);
        recyclerViewLayoutManager = recyclerView.getLayoutManager();

        //API叩く
        liveId = getArguments().getString("id");
        getGiftAPI();
    }

    /**
     * API叩く
     */
    private void getGiftAPI() {
        //API叩く
        String url = "https://api.nicoad.nicovideo.jp/v2/contents/nage_agv/" + liveId + "/histories?limit=60";
        //くるくる
        SnackberProgress snackberProgress = new SnackberProgress(Snackbar.make(view, view.getContext().getString(R.string.loading) + "\n" + url, Snackbar.LENGTH_INDEFINITE));
        snackberProgress.showSnackBerProgress();
        Request request = new Request.Builder().url(url).get().build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                UIThreadToast.showUIThreadToast(view.getContext().getString(R.string.error), getActivity());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String response_string = response.body().string();
                if (!response.isSuccessful()) {
                    UIThreadToast.showUIThreadToast(view.getContext().getString(R.string.error) + " : " + String.valueOf(response.code()), getActivity());
                } else {
                    try {
                        //Historyだけ
                        JSONObject jsonObject = new JSONObject(response_string);
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("histories");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject history_JsonObject = jsonArray.getJSONObject(i);
                            //追加
                            ArrayList<String> item = new ArrayList<>();
                            item.add(history_JsonObject.toString());
                            recyclerViewList.add(item);
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
                                        CommentRecyclerViewAdapter customMenuRecyclerViewAdapter = new CommentRecyclerViewAdapter(recyclerViewList,"gift");
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        snackberProgress.dismissSnackberProgress();
    }


}

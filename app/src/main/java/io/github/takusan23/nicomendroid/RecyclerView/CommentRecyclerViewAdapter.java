package io.github.takusan23.nicomendroid.RecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.github.takusan23.nicomendroid.Fragment.GiftFragment;
import io.github.takusan23.nicomendroid.JSONParse.CommentJSONParse;
import io.github.takusan23.nicomendroid.JSONParse.GiftJSONParse;
import io.github.takusan23.nicomendroid.R;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ArrayList> itemList;
    private String mode = "";

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username_TextView;
        public TextView text_TextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username_TextView = itemView.findViewById(R.id.adapter_name);
            text_TextView = itemView.findViewById(R.id.adapter_text);
        }
    }

    public CommentRecyclerViewAdapter(ArrayList<ArrayList> arrayList, String mode) {
        this.itemList = arrayList;
       this.mode=mode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_recycler_view_adapter_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //UI
        ArrayList<String> item = itemList.get(i);
        if (mode.contains("gift")) {
            GiftJSONParse api = new GiftJSONParse(item.get(0));
            viewHolder.text_TextView.setText(api.getItem_name());
            viewHolder.username_TextView.setText(api.getUser_name());
        } else if (mode.contains("comment")) {
            CommentJSONParse api = new CommentJSONParse(viewHolder.text_TextView.getContext(), item.get(0));
            viewHolder.username_TextView.setText(api.getPremium() + "\n" + api.getUserId());
            viewHolder.text_TextView.setText(api.getText());
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}

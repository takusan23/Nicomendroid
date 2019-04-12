package io.github.takusan23.nicomendroid.RecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.github.takusan23.nicomendroid.JSONParse.CommentJSONParse;
import io.github.takusan23.nicomendroid.R;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ArrayList> itemList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username_TextView;
        public TextView text_TextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username_TextView = itemView.findViewById(R.id.adapter_name);
            text_TextView = itemView.findViewById(R.id.adapter_text);
        }
    }
    public CommentRecyclerViewAdapter(ArrayList<ArrayList> arrayList){
        this.itemList = arrayList;
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
        ArrayList<String> item= itemList.get(i);
        CommentJSONParse api = new CommentJSONParse(item.get(0));
        viewHolder.text_TextView.setText(api.getText());
        viewHolder.username_TextView.setText(api.getPremium());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}

package com.example.microtemp.microblog.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.microtemp.microblog.R;

import java.util.List;

public class MicroblogRecyclerViewAdapter extends RecyclerView.Adapter<MicroblogRecyclerViewAdapter.ViewHolder> {


    private List<GetMicroblogResponse> listItemMicroblogList;
    private Context context;

    public MicroblogRecyclerViewAdapter(List<GetMicroblogResponse> listItemMicroblogList, Context context) {
        this.listItemMicroblogList = listItemMicroblogList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_microblog, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetMicroblogResponse listItemMicroblog = listItemMicroblogList.get(position);

        holder.textViewAuthor.setText(Integer.toString(listItemMicroblog.getAuthor()));
        holder.textViewTitle.setText(listItemMicroblog.getName());
        holder.textViewTags.setText(listItemMicroblog.getTags());
    }

    @Override
    public int getItemCount() {
        return listItemMicroblogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewAuthor;
        public TextView textViewTitle;
        public TextView textViewTags;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewTags = itemView.findViewById(R.id.textViewTags);
        }
    }
}

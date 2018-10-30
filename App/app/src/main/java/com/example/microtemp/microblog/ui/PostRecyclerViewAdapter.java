package com.example.microtemp.microblog.ui;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.microtemp.microblog.R;

import java.util.List;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {


    private List<ListItemPost> listItemPosts;
    private Context context;

    public PostRecyclerViewAdapter(List<ListItemPost> listItemPosts, Context context) {
        this.listItemPosts = listItemPosts;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItemPost listItemPost = listItemPosts.get(position);

        holder.textViewAuthor.setText(listItemPost.getAuthor());
        holder.textViewTitle.setText(listItemPost.getTitle());
        holder.textViewTags.setText(listItemPost.getTags());
        holder.textViewViews.setText(listItemPost.getViews());
        /* holder.imageView.setImageIcon(R.mipmap.ic_launcher.i); */

    }

    @Override
    public int getItemCount() {
        return listItemPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewAuthor;
        public TextView textViewTitle;
        public TextView textViewTags;
        public ImageView imageView;
        public TextView textViewViews;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewAuthor = (TextView) itemView.findViewById(R.id.textViewAuthor);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewTags = (TextView) itemView.findViewById(R.id.textViewTags);
            textViewViews = (TextView) itemView.findViewById(R.id.textViewViews);
            imageView = itemView.findViewById(R.id.image);

        }
    }
}

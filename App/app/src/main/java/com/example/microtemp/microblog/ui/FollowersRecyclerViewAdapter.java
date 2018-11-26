package com.example.microtemp.microblog.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.model.User;

import java.util.List;


public class FollowersRecyclerViewAdapter extends RecyclerView.Adapter<FollowersRecyclerViewAdapter.ViewHolder> {


    private List<User> userList;
    private Context context;

    public FollowersRecyclerViewAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_followers, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        User listItemPost = userList.get(position);
        holder.textViewAuthor.setText(listItemPost.getUsername().replaceAll("\"", ""));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewAuthor;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthorf);
        }
    }

}

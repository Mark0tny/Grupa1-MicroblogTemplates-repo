package com.example.microtemp.microblog.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.activity.FollowersActivity;
import com.example.microtemp.microblog.api.RetrofitClient;
import com.example.microtemp.microblog.api.SessionManager;
import com.example.microtemp.microblog.api.GetMicroblogResponse;
import com.example.microtemp.microblog.model.User;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MicroblogRecyclerViewAdapter extends RecyclerView.Adapter<MicroblogRecyclerViewAdapter.ViewHolder> {


    private List<GetMicroblogResponse> listItemMicroblogList;
    private List<User> userList;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        GetMicroblogResponse listItemMicroblog = listItemMicroblogList.get(position);

        JsonObject jsonUsers = new JsonObject();
        jsonUsers.addProperty("id", listItemMicroblogList.get(position).getIdMicroblog());
        loadFollowers(jsonUsers);

        holder.textViewAuthor.setText(listItemMicroblog.getUsername().replaceAll("\"", ""));
        holder.textViewTitle.setText(listItemMicroblog.getName().replaceAll("\"", ""));
        if(listItemMicroblog.getTags() != null)
            holder.textViewTags.setText( listItemMicroblog.getTags().replaceAll("\"", ""));
        else
            holder.textViewTags.setText("");

        holder.textViewTime.setText(listItemMicroblog.getTimeCreated());
        if (userList == null || userList.size() == 0) {
            holder.textViewFollowers.setText("0");
        } else {
            holder.textViewFollowers.setText(Integer.toString(userList.size()));
        }
        holder.buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followBlogCall(listItemMicroblogList.get(position).getIdMicroblog());
            }
        });

        holder.textFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FollowersActivity.class);
                intent.putExtra("id_blog", listItemMicroblogList.get(position).getIdMicroblog());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemMicroblogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewAuthor;
        public TextView textViewTitle;
        public TextView textViewTags;
        public TextView textViewFollowers;
        public TextView textViewTime;
        public TextView textFollowers;
        public Button buttonFollow;


        public ViewHolder(View itemView) {
            super(itemView);

            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewTags = itemView.findViewById(R.id.textViewTags);
            textViewTime = itemView.findViewById(R.id.time_blog);
            textViewFollowers = itemView.findViewById(R.id.follow_count);
            textFollowers = itemView.findViewById(R.id.textFollowers);
            buttonFollow = itemView.findViewById(R.id.follow_blog);
        }
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;


        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

            public void onShowPress(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onShowPress(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public void loadFollowers(JsonObject jsonFollowersList) {
        retrofit2.Call<List<User>> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .getfollowers(jsonFollowersList);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    userList = response.body();
                    Log.d("USERLIST", userList.toString());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("USERLIST ERROR", userList.toString());
            }
        });

    }

    public void followBlogCall(Integer blogID) {
        User user = SessionManager.getInstance(context).getUser();
        JsonObject jsonFollow = new JsonObject();
        jsonFollow.addProperty("userid", user.getId());
        jsonFollow.addProperty("blogid", blogID);

        retrofit2.Call<JsonObject> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .followBlog(jsonFollow);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "Microlog followed", Toast.LENGTH_LONG).show();
                Log.d("GOOD", t.getMessage());
            }
        });
    }


}

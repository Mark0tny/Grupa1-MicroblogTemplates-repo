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
import com.example.microtemp.microblog.activity.PostActivity;
import com.example.microtemp.microblog.api.RetrofitClient;
import com.example.microtemp.microblog.api.GetPostResponse;
import com.google.gson.JsonObject;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {


    private List<GetPostResponse> listItemPosts;
    private Context context;

    public PostRecyclerViewAdapter(List<GetPostResponse> listItemPosts, Context context) {
        this.listItemPosts = listItemPosts;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        GetPostResponse listItemPost = listItemPosts.get(position);

        holder.textViewAuthor.setText(listItemPost.getUsername().replaceAll("\"", ""));
        holder.textViewTitle.setText(listItemPost.getUsername().replaceAll("\"", ""));
        holder.textViewTags.setText(listItemPost.getTags().replaceAll("\"", ""));
        holder.textViewTime.setText(listItemPost.getTimeCreated().replaceAll("\"", ""));
        holder.textViewLikes.setText(Integer.toString(listItemPost.getViews()));
        holder.textViewComments.setText(Integer.toString(listItemPost.getCount()));

        holder.buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLikeCall(listItemPosts.get(position).getIdPost());
                holder.buttonLike.setEnabled(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewAuthor;
        public TextView textViewTime;
        public TextView textViewTitle;
        public TextView textViewTags;
        public TextView textViewLikes;
        public TextView textViewComments;

        public Button buttonLike;


        public ViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthorp);
            textViewTime = itemView.findViewById(R.id.time_post);
            textViewTitle = itemView.findViewById(R.id.textViewTitlep);
            textViewTags = itemView.findViewById(R.id.textViewTagsp);
            textViewLikes = itemView.findViewById(R.id.text_like_count);
            textViewComments = itemView.findViewById(R.id.comm_count);
            buttonLike = itemView.findViewById(R.id.addlike);
        }
    }
    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private PostRecyclerViewAdapter.RecyclerItemClickListener.OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
            public void onShowPress(View view, int position);
        }
        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, PostRecyclerViewAdapter.RecyclerItemClickListener.OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    mListener.onItemLongClick(child,recyclerView.getChildAdapterPosition(child));
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

    public void addLikeCall(Integer postID) {

        JsonObject jsonLike = new JsonObject();
        jsonLike.addProperty("id_post", postID);

        retrofit2.Call<JsonObject> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .likePost(jsonLike);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "Post LIKE", Toast.LENGTH_LONG).show();
                Log.d("GOOD", t.getMessage());
            }
        });
    }

}

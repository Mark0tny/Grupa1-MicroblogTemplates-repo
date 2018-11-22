package com.example.microtemp.microblog.ui;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.microtemp.microblog.R;

import java.util.List;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetPostResponse listItemPost = listItemPosts.get(position);

        holder.textViewAuthor.setText(Integer.toString(listItemPost.getAuthor()));
        holder.textViewTitle.setText(listItemPost.getTitle());
        holder.textViewTags.setText(listItemPost.getContent());
        holder.textViewViews.setText("0");
        //holder.textViewViews.setText(listItemPost.getViews());
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

            textViewAuthor = (TextView) itemView.findViewById(R.id.textViewAuthorp);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitlep);
            textViewTags = (TextView) itemView.findViewById(R.id.textViewTagsp);
            textViewViews = (TextView) itemView.findViewById(R.id.textViewViews);
            imageView = itemView.findViewById(R.id.image);

        }
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private PostRecyclerViewAdapter.RecyclerItemClickListener.OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

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
}

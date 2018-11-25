package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.SessionManager;
import com.example.microtemp.microblog.model.Comment;
import com.example.microtemp.microblog.model.User;
import com.example.microtemp.microblog.ui.CommentsRecyclerViewAdapter;
import com.example.microtemp.microblog.ui.GetPostResponse;
import com.example.microtemp.microblog.ui.PostRecyclerViewAdapter;

import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    private Button buttonAddComment;
    private EditText commentContent;
    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        final Integer id = intent.getIntExtra("id_post",0);
        User user = SessionManager.getInstance(this).getUser();

        buttonAddComment = findViewById(R.id.comment_post_btn);
        commentContent = findViewById(R.id.comment_field);


        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void recyclerViewInit(final List<Comment> commentList) {

        recyclerView = findViewById(R.id.comment_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new CommentsRecyclerViewAdapter(commentList, getApplicationContext());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new PostRecyclerViewAdapter.
                        RecyclerItemClickListener(this, recyclerView, new PostRecyclerViewAdapter.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }

                    @Override
                    public void onShowPress(View view, int position) {

                    }

                }));

    }



}

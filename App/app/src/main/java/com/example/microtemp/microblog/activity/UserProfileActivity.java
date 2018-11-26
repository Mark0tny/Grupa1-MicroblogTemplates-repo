package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.RetrofitClient;
import com.example.microtemp.microblog.api.SessionManager;
import com.example.microtemp.microblog.model.User;
import com.example.microtemp.microblog.api.GetMicroblogResponse;
import com.example.microtemp.microblog.ui.MicroblogRecyclerViewAdapter;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    private NavigationView navigation_view;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu drawerMenu;
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    List<GetMicroblogResponse> microblogList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        User user = SessionManager.getInstance(this).getUser();

        Log.d("USER ID", Integer.toString(user.getId()));
        JsonObject jsonMicroblog = new JsonObject();
        jsonMicroblog.addProperty("id", user.getId());

        loadMicroblogs(jsonMicroblog);

        TextView txtViewResponeUsername = (TextView) findViewById(R.id.responseUsername);
        txtViewResponeUsername.setText("Witaj " + user.getEmail());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_profile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, CreateMicroblogActivity.class);
                startActivity(intent);
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, SearchActivity.class);
                startActivity(intent);
                return false;
            }
        });

        initNavBar();

    }

    public void logout() {
        SessionManager.getInstance(this).clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public void loadMicroblogs(JsonObject jsonMicroblog) {

        retrofit2.Call<List<GetMicroblogResponse>> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .getMicroblogs(jsonMicroblog);

        call.enqueue(new Callback<List<GetMicroblogResponse>>() {
            @Override
            public void onResponse(Call<List<GetMicroblogResponse>> call, Response<List<GetMicroblogResponse>> response) {
                if (response.isSuccessful()) {
                    microblogList = response.body();
                    for (GetMicroblogResponse mess : microblogList
                            ) {
                        Log.d("LIST TIEM", mess.getName());
                    }
                    recyclerViewInit(microblogList);
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetMicroblogResponse>> call, Throwable t) {
                Log.d("ERROR", t.toString());
            }
        });
    }

    public void recyclerViewInit(final List<GetMicroblogResponse> microblogList) {
        recyclerView = (RecyclerView) findViewById(R.id.microblog_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new MicroblogRecyclerViewAdapter(microblogList, getApplicationContext());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new MicroblogRecyclerViewAdapter.
                        RecyclerItemClickListener(this, recyclerView, new MicroblogRecyclerViewAdapter.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(UserProfileActivity.this, PostActivity.class);
                        intent.putExtra("name", microblogList.get(position).getName());
                        intent.putExtra("id", microblogList.get(position).getIdMicroblog());
                        startActivity(intent);
                    }

                    @Override
                    public void onShowPress(View view, int position) {

                    }

                }));

    }
    public void initNavBar(){

        navigation_view = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerMenu = navigation_view.getMenu();
        for(int i = 0; i < drawerMenu.size(); i++) {
            drawerMenu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.nav_add) {

                    } else if (id == R.id.nav_search) {
                        Intent intent = new Intent(UserProfileActivity.this, SearchActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (id == R.id.nav_exit) {
                        finish();
                        System.exit(0);
                    } else if (id == R.id.nav_logout) {
                        logout();
                    }
                    return false;
                }
            });
        }
        View header = navigation_view.getHeaderView(0);
        TextView name = header.findViewById(R.id.Log_username);
        User user = SessionManager.getInstance(this).getUser();
        name.setText(user.getUsername());


    }
    public void onBackPressed() {
        //super.onBackPressed();
        logout();
    }
}

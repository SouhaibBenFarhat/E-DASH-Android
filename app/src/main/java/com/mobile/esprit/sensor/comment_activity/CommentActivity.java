package com.mobile.esprit.sensor.comment_activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mobile.esprit.sensor.Entities.Comment;
import com.mobile.esprit.sensor.Entities.Recipe;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.background_tasks.CommentManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommentActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ImageButton sendCommentButton;
    EditText commentText;
    private Recipe recipe;
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Feeds");
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.comments_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        recipe = getIntent().getExtras().getParcelable("Recipe");
        recyclerView = (RecyclerView) findViewById(R.id.rv_comments);
        final CommentManager commentManager = new CommentManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        commentManager.getCommentByRecipe(recipe.getId(), recyclerView);


        sendCommentButton = (ImageButton) findViewById(R.id.send_comment_button);
        commentText = (EditText) findViewById(R.id.comment_text);

        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentText.getText().toString().equals("")) {
                    Toast.makeText(CommentActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                } else {
                    Comment comment = new Comment();
                    comment.setContent(commentText.getText().toString());
                    comment.setDate(now());
                    commentManager.addComment(recipe.getId(), comment, recyclerView);
                    commentText.setText("");
                    commentText.clearFocus();
                    InputMethodManager inputManager =
                            (InputMethodManager) getApplicationContext().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            getWindow().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                }
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {




        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                final CommentManager commentManager = new CommentManager(getApplicationContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                commentManager.getCommentByRecipe(recipe.getId(), recyclerView);
                swipeRefreshLayout.setRefreshing(false);
            }

        }, 2000);
    }
}

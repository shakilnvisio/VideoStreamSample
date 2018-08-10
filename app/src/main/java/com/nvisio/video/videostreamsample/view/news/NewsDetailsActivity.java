package com.nvisio.video.videostreamsample.view.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nvisio.video.videostreamsample.R;
import com.nvisio.video.videostreamsample.view.NavigationActivity;

public class NewsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_details);

        TextView newsHead = findViewById(R.id.newsHead);
        TextView author = findViewById(R.id.author);
        ImageView image = findViewById(R.id.ImageNews);
        TextView description = findViewById(R.id.description);

        String headline = getIntent().getStringExtra("headline");
        String authorStr = getIntent().getStringExtra("author");
        String imageUrl = getIntent().getStringExtra("image");
        String desc = getIntent().getStringExtra("desc");

        newsHead.setText(headline);
        author.setText(authorStr);
        description.setText(desc);
        Glide.with(this).load(imageUrl).into(image);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NewsDetailsActivity.this, NewsActivity.class));
        finish();
    }
}

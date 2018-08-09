package com.nvisio.video.videostreamsample.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nvisio.video.videostreamsample.R;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);
    }

    public void GameActivity(View view) {
        startActivity(new Intent(NavigationActivity.this,GameViewActivity.class));
        finish();
    }

    public void VideoActivity(View view) {
        startActivity(new Intent(NavigationActivity.this,VideoActivity.class));
        finish();
    }
}

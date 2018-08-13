package com.nvisio.video.videostreamsample.view.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.util.Util;
import com.nvisio.video.videostreamsample.AudioPlayerService;
import com.nvisio.video.videostreamsample.R;

public class AudioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);
        Intent intent = new Intent(this, AudioPlayerService.class);
        Util.startForegroundService(this,intent);
    }
}

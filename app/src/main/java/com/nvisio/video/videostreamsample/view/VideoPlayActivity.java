package com.nvisio.video.videostreamsample.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.nvisio.video.videostreamsample.R;
import com.nvisio.video.videostreamsample.view.news.NewsActivity;

public class VideoPlayActivity extends AppCompatActivity {

    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_HAVE_DATA = "havedata";

    private final String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
    private PlayerView playerView;
    private SimpleExoPlayer player;

    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private SharedPreferences preferences;
    private ComponentListener componentListener;
    private String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);
        preferences = getSharedPreferences("shared",MODE_PRIVATE);
        componentListener = new ComponentListener();
        if (!preferences.getBoolean(KEY_HAVE_DATA,false)){
            playWhenReady = true;
            currentWindow = 0;
             playbackPosition = 0;
        }
        else{
            playWhenReady = preferences.getBoolean(KEY_PLAY_WHEN_READY,false);
            currentWindow = preferences.getInt(KEY_WINDOW,0);
            playbackPosition = preferences.getLong(KEY_POSITION,0);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_HAVE_DATA,false).apply();

            Log.d("boo>>","oncretae: pos: "+playbackPosition);
        }
       //link = getIntent().getStringExtra("link");
        mediaDataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
    }


    private void initializePlayer(){
        if (player == null){
            playerView = findViewById(R.id.playerView);

            // create a default TrackSelector
            bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // create a player
            player = ExoPlayerFactory.newSimpleInstance(this,trackSelector);
            player.addListener(componentListener);
            playerView.setPlayer(player);
            player.setPlayWhenReady(true);
            Log.d("boo>>","win: "+currentWindow+" pos: "+playbackPosition);
            player.seekTo(currentWindow,playbackPosition);
            MediaSource mediaSource = buildMediaSource(Uri.parse(getString(R.string.media_url_dash)));
            player.prepare(mediaSource,false,false);
        }



        /*MediaSource mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(Uri.parse(vidAddress));*/

        //player.prepare(mediaSource,true,false);


    }

    /*private MediaSource buildMediaSource(Uri uri){
        DataSource.Factory manifestDataSourceFactory = new DefaultHttpDataSourceFactory("ua");
        DashChunkSource.Factory dashChunkSourceFactory = new DefaultDashChunkSource.Factory(
                new DefaultHttpDataSourceFactory("ua", BANDWIDTH_METER));
        return new DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
                .createMediaSource(uri);
    }*/

    private MediaSource buildMediaSource(Uri uri){
        String userAgent = "exoplayer-codelab";
        //return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent)).createMediaSource(uri);
        if (uri.getLastPathSegment().contains("mp4")){
            return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent)).createMediaSource(uri);
        }
        else{
            DataSource.Factory manifestDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);
            DashChunkSource.Factory dashChunkSourceFactory = new DefaultDashChunkSource.Factory(
                    new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER));
            return new DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
                    .createMediaSource(uri);
        }
    }


    private void releasePlayer(){
        if (player!=null){
            updateStartPosition();
            player.removeListener(componentListener);
            player.release();
            player = null;
            trackSelector = null;


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUi();
        initializePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void updateStartPosition() {
        playbackPosition = player.getCurrentPosition();
        currentWindow = player.getCurrentWindowIndex();
        playWhenReady = player.getPlayWhenReady();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_HAVE_DATA,true);
        editor.putBoolean(KEY_PLAY_WHEN_READY,playWhenReady);
        editor.putInt(KEY_WINDOW,currentWindow);
        editor.putLong(KEY_POSITION,playbackPosition);
        editor.apply();
    }


    private class ComponentListener extends Player.DefaultEventListener{

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            String stateString;

            switch (playbackState){
                case Player.STATE_IDLE:{
                    stateString = "idle";
                    Log.d("state>>", stateString);
                    break;
                }
                case Player.STATE_BUFFERING:{
                    stateString = "buffering";
                    Log.d("state>>", stateString);
                    break;
                }
                case Player.STATE_READY:{
                    stateString = "ready";
                    Log.d("state>>", stateString);
                    break;
                }
                case Player.STATE_ENDED:{
                    stateString = "ended";
                    Log.d("state>>", stateString);
                    break;
                }
                default:
                    stateString = "unknown state";
                    Log.d("state>>", stateString);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(VideoPlayActivity.this, VideoActivity.class));
        finish();
    }
}

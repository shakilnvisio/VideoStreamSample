package com.nvisio.video.videostreamsample;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.nvisio.video.videostreamsample.view.music.AudioActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AudioPlayerService extends Service {

    private SimpleExoPlayer player;
    private String link = "http://www.tmbg.com/_media/_pod/TMBGPodcast9A.mp3";
    private PlayerNotificationManager playerNotificationManager;
    private String PLAYBACK_CHANNEL_ID= "audio";
    private int PLAYBACK_NOTIFICATION_ID= 2;
    Context context;
    private List<String> songList;
    private final LocalBinder mLocalBinder = new LocalBinder();
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        player = ExoPlayerFactory.newSimpleInstance(context,new DefaultTrackSelector());
        streamCodeLong();
        // notification
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(context,
                PLAYBACK_CHANNEL_ID, R.string.playback_channel_name, PLAYBACK_NOTIFICATION_ID,
                new PlayerNotificationManager.MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {
                        // return title
                        return null;
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
                        Intent intent = new Intent(context, AudioActivity.class);
                        return PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {
                        // return description
                        return null;
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                        // return icon
                        return null;
                    }
                });

        // this listener makes our notification lifecycle aware
        playerNotificationManager.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            // it is called when a notification is created
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {
               // system wont kill our notification anymore
                startForeground(notificationId,notification);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
            stopSelf();
            }
        });

        playerNotificationManager.setPlayer(player);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
       // return null;
        return mLocalBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return START_STICKY;
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        playerNotificationManager.setPlayer(null);
        player.release();
        player = null;
    }

    public SimpleExoPlayer getPlayer() {
        Log.d("pla>>","service: "+player);
        return player;
    }

    public void streamPlaylist(){
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context,"audioDemo");

        String[] songsListFromArray = context.getResources().getStringArray(R.array.songs);
        songList = new ArrayList<>();
        songList.addAll(Arrays.asList(songsListFromArray));

        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        for (int i = 0; i <songList.size() ; i++) {
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(songList.get(i)));
        }

        player.prepare(concatenatingMediaSource);
        player.setPlayWhenReady(true);
    }


    public void streamCodeLong(){
        //songs list
        String[] songsListFromArray = context.getResources().getStringArray(R.array.songs);
        songList = new ArrayList<>();
        songList.addAll(Arrays.asList(songsListFromArray));

        //player
        // DefaultBandwidthMeter class implies estimated available network bandwidth based on measured download speed.
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // An ExtractorFactory is there for providing an array of the extractor for the media formats.
        final  ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // AdaptiveTrackSelection.Factory is a bandwidth based adaptive TrackSelection. It gives the highest quality state of a buffer according to your network condition.
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        // DefaultDataSourceFactory supports almost all non-adaptive audio video formats supported on Android. It will recognize our mp3 file and play it nicely.
        //DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,"audio", (TransferListener<? super DataSource>) bandwidthMeter);
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context,"AudioDemo"));
        MediaSource[] mediaSources = new MediaSource[songList.size()];
        for (int i = 0; i <mediaSources.length ; i++) {
            String songUri = songList.get(i);
            //mediaSources[i] = new ExtractorMediaSource(Uri.parse(songUri), dataSourceFactory, extractorsFactory, null, Throwable::printStackTrace);
            mediaSources[i] = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(songUri));
        }
        MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]: new ConcatenatingMediaSource(mediaSources);


        /*//DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context,"AudioDemo"));
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        // to support multiple audio songs

        *//*for (int i = 0; i <audioSongs.size ; i++) {
            MediaSource audioSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(link));
            concatenatingMediaSource.addMediaSource(audioSource);
        }
        player.prepare(concatenatingMediaSource);*//*
        MediaSource audioSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(link));*/
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
    }

    public class LocalBinder extends Binder{
        public AudioPlayerService getService(){
            return AudioPlayerService.this;
        }
    }
}

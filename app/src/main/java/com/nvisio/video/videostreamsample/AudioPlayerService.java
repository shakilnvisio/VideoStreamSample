package com.nvisio.video.videostreamsample;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nvisio.video.videostreamsample.view.music.AudioActivity;

public class AudioPlayerService extends Service {

    private SimpleExoPlayer player;
    private String link = "http://www.tmbg.com/_media/_pod/TMBGPodcast9A.mp3";
    private PlayerNotificationManager playerNotificationManager;
    private String PLAYBACK_CHANNEL_ID= "audio";
    private int PLAYBACK_NOTIFICATION_ID= 2;

    @Override
    public void onCreate() {
        super.onCreate();
        final Context context = this;

        player = ExoPlayerFactory.newSimpleInstance(context,new DefaultTrackSelector());
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context,"AudioDemo"));
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        // to support multiple audio songs

        /*for (int i = 0; i <audioSongs.size ; i++) {
            MediaSource audioSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(link));
            concatenatingMediaSource.addMediaSource(audioSource);
        }
        player.prepare(concatenatingMediaSource);*/
        MediaSource audioSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(link));
        player.prepare(audioSource);
        player.setPlayWhenReady(true);

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
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        playerNotificationManager.setPlayer(null);
        player.release();
        player = null;
    }
}

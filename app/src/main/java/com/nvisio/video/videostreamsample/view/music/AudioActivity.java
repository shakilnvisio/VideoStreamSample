package com.nvisio.video.videostreamsample.view.music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.nvisio.video.videostreamsample.AudioPlayerService;
import com.nvisio.video.videostreamsample.R;
import com.nvisio.video.videostreamsample.network.RetrofitClient;
import com.nvisio.video.videostreamsample.network.RetrofitInstance;
import com.nvisio.video.videostreamsample.view.NavigationActivity;
import com.nvisio.video.videostreamsample.view.music.database.AppDatabase;
import com.nvisio.video.videostreamsample.view.music.database.databaseModel.AudioContent;
import com.nvisio.video.videostreamsample.view.music.database.databaseModel.TagContent;
import com.nvisio.video.videostreamsample.view.music.model.Audio;
import com.nvisio.video.videostreamsample.view.music.model.AudioDataModel;
import com.nvisio.video.videostreamsample.view.music.model.Songs;
import com.nvisio.video.videostreamsample.view.music.model.Tags;
import com.nvisio.video.videostreamsample.view.music.model.Tag;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AudioActivity extends AppCompatActivity {
    private PlayerView playerView;
    //testing
    public SimpleExoPlayer player;
    AudioPlayerService service = null;
    Intent intent;

    //web api
    private CompositeDisposable disposable;
    private RetrofitClient retrofitClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audioplayer);
        playerView = findViewById(R.id.playerView);

        //web api call starts
        disposable = new CompositeDisposable();
        RetrofitInstance.changeApiBaseUrl(getString(R.string.audio_url));
        retrofitClient = RetrofitInstance.createService(RetrofitClient.class);
        getAudioData();
        //web api call ends

        /*intent = new Intent(this, AudioPlayerService.class);
        Util.startForegroundService(this,intent);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);*/
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            //service = ((service.LocalBinder) iBinder).getInstance();
            service =((AudioPlayerService.LocalBinder) iBinder).getService();
            Log.d("pla>>","service: "+service.getPlayer());
            playerView.setPlayer(service.getPlayer());
            // now you have the instance of service.
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service = null;
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
       /* if (service!=null){
            unbindService(mConnection);
            stopService(intent);
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AudioActivity.this, NavigationActivity.class));
        finish();
    }

    private void getAudioData(){
        disposable.add(retrofitClient.getAudio()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleAudioResponse,this::handleError));
    }

    private void handleAudioResponse(AudioDataModel audioDataModel) {
        List<Audio> getAllAudio = audioDataModel.getAudio();
        for (int i = 0; i <getAllAudio.size() ; i++) {
            String rqstFor = getAllAudio.get(i).getRequestFor();
            if (rqstFor.equals("tag")){
                List<Tags> getTag = getAllAudio.get(i).getTagData();
                for (int j = 0; j <getTag.size() ; j++) {
                    TagContent tag = new TagContent();
                    tag.setTagName(getTag.get(j).getTagName());
                    tag.setPosterUrl(getTag.get(j).getPoster());
                    AppDatabase.getAppDatabase(this).tagDao().insertTags(tag);
                }
            }
            else{
                List<Songs> getSongs = getAllAudio.get(i).getSongData();
                for (int j = 0; j <getSongs.size() ; j++) {
                    StringBuilder tagStr = new StringBuilder();
                    List<Tag> tags = getSongs.get(j).getTag();
                    for (int k = 0; k <tags.size() ; k++) {
                        if (k>0){
                            tagStr.append(" ").append(tags.get(k).getName());
                        }
                        else{
                            tagStr.append(tags.get(k).getName());
                        }
                    }
                    AudioContent singleSong = new AudioContent();
                    singleSong.setSongId(getSongs.get(j).getId());
                    singleSong.setSongTitle(getSongs.get(j).getTitle());
                    singleSong.setSongPoster(getSongs.get(j).getPoster());
                    singleSong.setSongUrl(getSongs.get(j).getSong());
                    singleSong.setSongTags(tagStr.toString());
                    AppDatabase.getAppDatabase(this).songsDao().insertSongs(singleSong);
                }

            }
        }
    }


    private void handleError(Throwable error) {
        Toast.makeText(this, "Some error occured!", Toast.LENGTH_SHORT).show();
        Log.d("err>>", ""+error.getMessage());
        // hideLoading();
    }

    private void handleResponseWayNumberOne(AudioDataModel audioDataModel){
        List<Audio> getAllAudio = audioDataModel.getAudio();

        List<Tags> getTag = getAllAudio.get(0).getTagData();
        List<Songs> getSongs = getAllAudio.get(1).getSongData();

        for (int i = 0; i <getTag.size() ; i++) {
            String tagName = getTag.get(i).getTagName();
            String tagPoster = getTag.get(i).getPoster();
            Log.d("tagR>>","Name: "+tagName+" poster: "+tagPoster+"\n");
        }

        for (int i = 0; i <getSongs.size() ; i++) {
            String tag = "";
            String id = getSongs.get(i).getId();
            String songName = getSongs.get(i).getTitle();
            String poster  = getSongs.get(i).getPoster();
            String url = getSongs.get(i).getSong();
            List<Tag> tags = getSongs.get(i).getTag();
            for (int j = 0; j <tags.size() ; j++) {
                tag = tag + tags.get(j).getName()+", ";
            }
            String allInOne= "id: "+id+" title: "+songName+" poster: "+poster+" url: "+url+" tag: "+tag+"\n";
            Log.d("songR>>",allInOne);
        }
    }

    public void GetTag(View view) {
        List<TagContent> AllTag = AppDatabase.getAppDatabase(this).tagDao().getAllTag();
        for (int i = 0; i <AllTag.size() ; i++) {
            Log.d("tags>>","tag"+i+": "+AllTag.get(i).getTagName());
        }

    }

    public void DeleteTag(View view) {
        AppDatabase.getAppDatabase(this).tagDao().deleteAllTags();
    }

    public void GetAllSongs(View view) {
        List<AudioContent> allSongs = AppDatabase.getAppDatabase(this).songsDao().getAllSongs();
        for (int i = 0; i <allSongs.size() ; i++) {
            Log.d("allSongs>>","title: "+allSongs.get(i).getSongTitle()+" tags: "+allSongs.get(i).getSongTags());
        }
    }

    public void GetSongsBasedOnTag(View view) {
        List<AudioContent> getSong = AppDatabase.getAppDatabase(this).songsDao().getSongsBasedOn("%Bangla Top 100%");
        for (int i = 0; i <getSong.size() ; i++) {
            Log.d("tagBS>>","title: "+getSong.get(i).getSongTitle());
        }
    }

    public void DeleteSongs(View view) {
        AppDatabase.getAppDatabase(this).songsDao().deleteAllSongs();
    }
}

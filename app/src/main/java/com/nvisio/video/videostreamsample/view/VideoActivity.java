package com.nvisio.video.videostreamsample.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.nvisio.video.videostreamsample.R;
import com.nvisio.video.videostreamsample.adapter.YoutubeAdapter;
import com.nvisio.video.videostreamsample.model.youtube.Item;
import com.nvisio.video.videostreamsample.model.youtube.YoutubeApiModel;
import com.nvisio.video.videostreamsample.network.RetrofitClient;
import com.nvisio.video.videostreamsample.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VideoActivity extends AppCompatActivity implements YoutubeAdapter.YoutubeListener {

    private RecyclerView recyclerViewOne;
    private RecyclerView recyclerViewTwo;
    private RecyclerView recyclerViewThree;
    private CompositeDisposable disposable;
    private RetrofitClient service;
    private YoutubeAdapter.YoutubeListener popOneListener;
    private YoutubeAdapter.YoutubeListener popTwoListener;
    private YoutubeAdapter.YoutubeListener popThreeListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        disposable = new CompositeDisposable();
        service = RetrofitInstance.createService(RetrofitClient.class);
        //RetrofitInstance.changeApiBaseUrl(getString(R.string.youtube_base_url));
        initRec();
        //initListener();
        getVideosOne();
        getVideosTwo();
        getVideosThree();
    }

    private void initRec(){
        recyclerViewOne = findViewById(R.id.vidRecOne);
        recyclerViewTwo = findViewById(R.id.vidRecTwo);
        recyclerViewThree = findViewById(R.id.vidRecThree);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewOne.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManagerTwo = new LinearLayoutManager(this);
        linearLayoutManagerTwo.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTwo.setLayoutManager(linearLayoutManagerTwo);

        LinearLayoutManager linearLayoutManagerThree = new LinearLayoutManager(this);
        linearLayoutManagerThree.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewThree.setLayoutManager(linearLayoutManagerThree);
    }

    private void getVideosOne(){
        disposable.add(service.getPopularVideo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleVideoOne,this::handleError));
    }

    private void getVideosTwo(){
        disposable.add(service.getPopularVideo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleVideoTwo,this::handleError));
    }

    private void getVideosThree(){
        disposable.add(service.getPopularVideo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleVideoThree,this::handleError));
    }

    public void handleVideoOne(YoutubeApiModel youtubeApiModel){
        List<Item> itemOne = new ArrayList<>();
        itemOne = youtubeApiModel.getItems();
        YoutubeAdapter adapter = new YoutubeAdapter(this, itemOne);
        recyclerViewOne.setAdapter(adapter);
        adapter.setOnClicked(this);
    }
    public void handleVideoTwo(YoutubeApiModel youtubeApiModel){
        List<Item> itemTwo = new ArrayList<>();
        itemTwo = youtubeApiModel.getItems();
        YoutubeAdapter adapter = new YoutubeAdapter(this, itemTwo);
        recyclerViewTwo.setAdapter(adapter);
       adapter.setOnClicked(this);
    }
    public void handleVideoThree(YoutubeApiModel youtubeApiModel){
        List<Item> itemThree = new ArrayList<>();
        itemThree = youtubeApiModel.getItems();
        YoutubeAdapter adapter = new YoutubeAdapter(this, itemThree);
        recyclerViewThree.setAdapter(adapter);
        adapter.setOnClicked(this);
    }
    private void handleError(Throwable error) {
        Toast.makeText(this, "Some error occured!", Toast.LENGTH_SHORT).show();
        Log.d("err>>", ""+error.getMessage());
        // hideLoading();
    }
   /* private void initListener(){
       popOneListener = new YoutubeAdapter.YoutubeListener() {
            @Override
            public void singleVideoClicked(String url) {
                Toast.makeText(VideoActivity.this, ""+url, Toast.LENGTH_SHORT).show();
            }
        };

        popTwoListener = new YoutubeAdapter.YoutubeListener() {
            @Override
            public void singleVideoClicked(String url) {
                Toast.makeText(VideoActivity.this, ""+url, Toast.LENGTH_SHORT).show();
            }
        };

        popThreeListener = new YoutubeAdapter.YoutubeListener() {
            @Override
            public void singleVideoClicked(String url) {
                Toast.makeText(VideoActivity.this, ""+url, Toast.LENGTH_SHORT).show();
            }
        };
    }*/

    @Override
    public void singleVideoClicked(String url) {
        Intent intent = new Intent(VideoActivity.this,VideoPlayActivity.class);
        intent.putExtra("link",url);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}

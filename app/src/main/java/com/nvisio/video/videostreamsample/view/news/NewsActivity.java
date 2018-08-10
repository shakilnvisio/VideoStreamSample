package com.nvisio.video.videostreamsample.view.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.nvisio.video.videostreamsample.R;
import com.nvisio.video.videostreamsample.adapter.NewsAdapter;
import com.nvisio.video.videostreamsample.model.news.Article;
import com.nvisio.video.videostreamsample.model.news.NewsApiModel;
import com.nvisio.video.videostreamsample.network.RetrofitClient;
import com.nvisio.video.videostreamsample.network.RetrofitInstance;
import com.nvisio.video.videostreamsample.view.NavigationActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NewsActivity extends AppCompatActivity implements NewsAdapter.ReadMoreListener {

    private CompositeDisposable disposable;
    private RetrofitClient service;
    private RecyclerView recyclerView;
    private List<Article> articlesData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        disposable = new CompositeDisposable();
        RetrofitInstance.changeApiBaseUrl(getString(R.string.news_base_url));
        service = RetrofitInstance.createService(RetrofitClient.class);
        init();
        getNews();
    }

    private void init(){
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private void getNews() {
        articlesData = new ArrayList<>();
        disposable.add(service.getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getData, this::handleError));
    }

    private void getData(NewsApiModel newsApiModels) {
        //RetrofitInstance.changeBaseUrlToDefualt();
            articlesData.clear();
            articlesData = newsApiModels.getArticles();
            NewsAdapter adapter = new NewsAdapter(this,articlesData);
            recyclerView.setAdapter(adapter);
            adapter.setOnClicked(this);


    }

    private void handleError(Throwable error) {
        //RetrofitInstance.changeBaseUrlToDefualt();
        Toast.makeText(this, "Some error occured!", Toast.LENGTH_SHORT).show();
        Log.d("err>>", ""+error.getMessage());
        // hideLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    @Override
    public void readMoreClicked(int p) {
        String title = articlesData.get(p).getTitle();
        Toast.makeText(this, ""+title, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(NewsActivity.this,NewsDetailsActivity.class);
        intent.putExtra("headline",title);
        intent.putExtra("image",articlesData.get(p).getUrlToImage());
        intent.putExtra("author",articlesData.get(p).getAuthor());
        intent.putExtra("desc",articlesData.get(p).getDescription());
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NewsActivity.this, NavigationActivity.class));
        finish();
    }
}

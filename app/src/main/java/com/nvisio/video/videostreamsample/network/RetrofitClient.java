package com.nvisio.video.videostreamsample.network;

import com.nvisio.video.videostreamsample.model.news.NewsApiModel;
import com.nvisio.video.videostreamsample.model.youtube.YoutubeApiModel;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface RetrofitClient {

    @GET("videos?part=snippet&contentDetails&chart=mostPopular&regionCode=IN&maxResults=10&key=AIzaSyD590OOBqFEQOwTICyYmIcZHw7iT2_K1O0")
    Single<YoutubeApiModel> getPopularVideo();

    @GET ("top-headlines?country=us&apiKey=e40634bf56ef420e93819dbc493de0d5")
    Single<NewsApiModel> getNews();
}

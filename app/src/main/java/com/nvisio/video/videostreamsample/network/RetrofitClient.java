package com.nvisio.video.videostreamsample.network;

import com.nvisio.video.videostreamsample.model.youtube.YoutubeApiModel;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface RetrofitClient {

    @GET("videos?part=snippet&contentDetails&chart=mostPopular&regionCode=IN&maxResults=10&key=AIzaSyD590OOBqFEQOwTICyYmIcZHw7iT2_K1O0")
    Single<YoutubeApiModel> getPopularVideo();
}

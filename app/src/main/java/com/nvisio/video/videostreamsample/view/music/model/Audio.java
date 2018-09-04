
package com.nvisio.video.videostreamsample.view.music.model;

import android.arch.persistence.room.Entity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Audio {

    @SerializedName("requestFor")
    @Expose
    private String requestFor;
    @SerializedName("tagData")
    @Expose
    private List<Tags> tagData = null;
    @SerializedName("songData")
    @Expose
    private List<Songs> songData = null;

    public String getRequestFor() {
        return requestFor;
    }

    public void setRequestFor(String requestFor) {
        this.requestFor = requestFor;
    }

    public List<Tags> getTagData() {
        return tagData;
    }

    public void setTagData(List<Tags> tagData) {
        this.tagData = tagData;
    }

    public List<Songs> getSongData() {
        return songData;
    }

    public void setSongData(List<Songs> songData) {
        this.songData = songData;
    }

}

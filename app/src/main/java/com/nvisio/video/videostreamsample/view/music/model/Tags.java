
package com.nvisio.video.videostreamsample.view.music.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Tags {
    @SerializedName("tagName")
    @Expose
    private String tagName;
    @SerializedName("poster")
    @Expose
    private String poster;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

}

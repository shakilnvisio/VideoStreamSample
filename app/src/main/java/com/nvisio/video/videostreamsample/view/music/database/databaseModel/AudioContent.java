package com.nvisio.video.videostreamsample.view.music.database.databaseModel;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Audio")
public class AudioContent {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String songId;
    private String songTitle;
    private String songPoster;
    private String songUrl;
    private String songTags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongPoster() {
        return songPoster;
    }

    public void setSongPoster(String songPoster) {
        this.songPoster = songPoster;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getSongTags() {
        return songTags;
    }

    public void setSongTags(String songTags) {
        this.songTags = songTags;
    }
}

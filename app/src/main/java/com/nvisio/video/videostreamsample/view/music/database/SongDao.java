package com.nvisio.video.videostreamsample.view.music.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.nvisio.video.videostreamsample.view.music.database.databaseModel.AudioContent;
import com.nvisio.video.videostreamsample.view.music.model.Songs;

import java.util.List;

import io.reactivex.Maybe;
@Dao
public interface SongDao {

    @Insert
    void insertSongs(AudioContent song);

    @Delete
    void deleteAllDelivery(AudioContent song);

    @Update
    void updateSong(AudioContent song);

    @Query("SELECT * FROM Audio")
    List<AudioContent> getAllSongs();

    @Query("SELECT * FROM Audio WHERE songTags LIKE :Tag")
    List<AudioContent> getSongsBasedOn(String Tag);

    @Query("DELETE FROM Audio")
    void deleteAllSongs();
}

package com.nvisio.video.videostreamsample.view.music.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.nvisio.video.videostreamsample.view.music.database.databaseModel.TagContent;
import com.nvisio.video.videostreamsample.view.music.model.Songs;
import com.nvisio.video.videostreamsample.view.music.model.Tag;
import com.nvisio.video.videostreamsample.view.music.model.Tags;

import java.util.List;

import io.reactivex.Maybe;
@Dao
public interface TagDao {

    @Insert
    void insertTags(TagContent tag);

    @Delete
    void deleteAllDelivery(TagContent tag);

    @Update
    void updateTag(TagContent tag);

    @Query("SELECT * FROM Tags")
    List<TagContent> getAllTag();

    @Query("DELETE FROM Tags")
    void deleteAllTags();
}

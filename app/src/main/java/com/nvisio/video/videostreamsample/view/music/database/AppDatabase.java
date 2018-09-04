package com.nvisio.video.videostreamsample.view.music.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.nvisio.video.videostreamsample.view.music.database.databaseModel.AudioContent;
import com.nvisio.video.videostreamsample.view.music.database.databaseModel.TagContent;
import com.nvisio.video.videostreamsample.view.music.model.Songs;
import com.nvisio.video.videostreamsample.view.music.model.Tags;

@Database(entities = {TagContent.class, AudioContent.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SongDao songsDao();
    public abstract TagDao tagDao();
    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context){
        if (INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"ImpulseBuy").allowMainThreadQueries().build();

        }
        return INSTANCE;
    }

}

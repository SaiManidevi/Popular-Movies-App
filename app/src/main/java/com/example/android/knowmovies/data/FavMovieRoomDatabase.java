package com.example.android.knowmovies.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;


@Database(entities = {Movie.class}, version = 1)
public abstract class FavMovieRoomDatabase extends RoomDatabase {
    public abstract FavMovieDao favMovieDao();

    private static FavMovieRoomDatabase INSTANCE;
    private static final String DATABASE_NAME = "movie_database";

    public static FavMovieRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavMovieRoomDatabase.class) {
                if (INSTANCE == null) {
                    //CREATES DATABASE HERE
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavMovieRoomDatabase.class,
                            DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

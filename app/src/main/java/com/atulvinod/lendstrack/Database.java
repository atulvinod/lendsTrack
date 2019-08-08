package com.atulvinod.lendstrack;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.atulvinod.lendstrack.EntityDAO;

@android.arch.persistence.room.Database(entities = Entity.class,version = 1,exportSchema = false)
public abstract class Database extends RoomDatabase {

    public abstract EntityDAO mEntityDAO();

    private static Database INSTANCE;

    static Database getDatabase(final Context c) {

    if(INSTANCE==null){
        synchronized (Database.class){
            if(INSTANCE==null){
                INSTANCE = Room.databaseBuilder(c.getApplicationContext(),
                        Database.class, "entity_database").fallbackToDestructiveMigration().addCallback(callback).build();

                /* .fallbackToDestructiveMigration clears the database when schema of the table is changed*/
            }
        }
    }

        return INSTANCE;
    }
    private static RoomDatabase.Callback callback = new RoomDatabase.Callback(){

    public void onOpen(@NonNull SupportSQLiteDatabase db){
        super.onOpen(db);
    }
};
}

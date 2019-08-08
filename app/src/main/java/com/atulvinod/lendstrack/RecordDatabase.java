package com.atulvinod.lendstrack;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = Record.class,version = 1,exportSchema = false)
public abstract class RecordDatabase extends RoomDatabase {
    public abstract RecordDAO dao();
    public static RecordDatabase INSTANCE;

    static com.atulvinod.lendstrack.RecordDatabase getDatabase(final Context c) {

        if(INSTANCE==null){
            synchronized (RecordDatabase.class){
                INSTANCE = Room.databaseBuilder(c.getApplicationContext(),RecordDatabase.class,"record_database").fallbackToDestructiveMigration().addCallback(callback).build();

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

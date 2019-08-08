package com.atulvinod.lendstrack;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface EntityDAO {

    @Query("SELECT * FROM entity_table ORDER BY mID ASC")
    LiveData<List<Entity>> getEntity();

    @Insert
    void insert(Entity entity);

    @Query("DELETE FROM entity_table")
    void deleteAll();

    @Query("DELETE FROM entity_table WHERE mID = :id")
    void deleteRow(int id);

    @Query("UPDATE entity_table SET amount =:value WHERE mID = :id")
    void update(int id,int value);


}

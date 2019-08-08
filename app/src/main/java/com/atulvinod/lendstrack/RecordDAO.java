package com.atulvinod.lendstrack;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao

public interface RecordDAO {

    @Query("SELECT * FROM record_table where ID =:id ORDER BY SNo DESC")
    public List<Record> getRecord(int id);

    @Insert
    void insert(Record r);

    @Query("DELETE FROM record_table Where SNo=:id")
    void delete(int id);

    @Query("DELETE FROM record_table Where ID=:id")
    void deleteAll(int id);

    @Query("SELECT COUNT(*) FROM record_table Where ID=:id")
    int getCount(int id);





}

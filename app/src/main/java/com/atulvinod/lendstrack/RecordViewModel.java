package com.atulvinod.lendstrack;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.atulvinod.lendstrack.RecordRepository;

import java.util.List;

public class RecordViewModel extends AndroidViewModel {
    private RecordRepository repo;
    private List<Record> mRecordList;

    private RecordViewModel(Application app,int ID){
        super(app);
        repo = new RecordRepository(app,ID);
        mRecordList = repo.getRecordList();


    }
    public List<Record> getRecordList(){
        return mRecordList;
    }
    public void insertRecord(Record r){
        repo.insertRecord(r);
    }
    public void deleteRecord(int ID){
        repo.deleteRecord(ID);
    }

}

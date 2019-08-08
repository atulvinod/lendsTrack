package com.atulvinod.lendstrack;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

public class RecordRepository {
    private RecordDAO dao;
    private List<Record> mRecordList;
    RecordRepository(Application app,int ID){
        RecordDatabase db = RecordDatabase.getDatabase(app);
        dao = db.dao();
        mRecordList = dao.getRecord(ID);

    }

    public List<Record> getRecordList() {
        return mRecordList;
    }
    public void insertRecord(Record r){
       new insertTask(dao).execute(r);
    }
    public void deleteRecord(int ID){
        new deleteTask(dao).execute(ID);
    }
    public void deleteAll(int ID){new deleteAllTask(dao).execute(ID);}
    public int getCount(int ID){
        return dao.getCount(ID);
    }

    private static class deleteAllTask extends AsyncTask<Integer,Void,Void> {
        private RecordDAO d;
        deleteAllTask(RecordDAO dao){
            this.d = dao;
        }

        @Override
        protected Void doInBackground(Integer... strings) {
            d.deleteAll(strings[0]);
            return null;
        }
    }


    private static class deleteTask extends AsyncTask<Integer,Void,Void> {
        private RecordDAO d;
        deleteTask(RecordDAO dao){
            this.d = dao;
        }

        @Override
        protected Void doInBackground(Integer... strings) {
            d.delete(strings[0]);
            return null;
        }
    }
    private static class insertTask extends AsyncTask<Record,Void,Void> {
        private RecordDAO d;
        insertTask(RecordDAO dao){
            this.d = dao;
        }

        @Override
        protected Void doInBackground(Record... strings) {
            d.insert(strings[0]);
            return null;
        }
    }
}

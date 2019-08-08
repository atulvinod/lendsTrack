package com.atulvinod.lendstrack;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class Repository {

    private EntityDAO dao;
    private LiveData<List<Entity>> allEntities;

    Repository(Application app){
        Database db = Database.getDatabase(app);
        dao = db.mEntityDAO();
        allEntities = dao.getEntity();
    }
    LiveData<List<Entity>> getAllEntities(){return allEntities;}

    public void insert(Entity e){
        new insertTask(dao).execute(e);
    }
    public void delete(int ID){new deleteTask(dao).execute(ID);}
    public void update(EntityData e){ new updateTask(dao).execute(e);}

    private static class deleteTask extends AsyncTask<Integer,Void,Void>{
        private EntityDAO d;
        deleteTask(EntityDAO dao){
            this.d = dao;
        }

        @Override
        protected Void doInBackground(Integer... strings) {
            d.deleteRow(strings[0]);
            return null;
        }
    }

    private static class insertTask extends AsyncTask<Entity,Void,Void>{
        private EntityDAO d;

        @Override
        protected Void doInBackground(Entity... entities) {
           d.insert(entities[0]);
            return null;
        }

        insertTask(EntityDAO edao){
            d = edao;
        }
    }
    private static class updateTask extends AsyncTask<EntityData,Void,Void>{
        private EntityDAO d;
        updateTask(EntityDAO dao){
            d =dao;
        }
        @Override
        protected Void doInBackground(EntityData... integers) {
            d.update(integers[0].getID(),integers[0].getAmount());
            return null;
        }
    }
}

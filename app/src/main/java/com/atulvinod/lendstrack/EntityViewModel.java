package com.atulvinod.lendstrack;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.atulvinod.lendstrack.EntityData;
import com.atulvinod.lendstrack.Repository;

import java.util.List;

public class EntityViewModel extends AndroidViewModel {

    private Repository repo;
    private LiveData<List<Entity>> entities;

    public EntityViewModel(Application app){
        super(app);
        repo = new Repository(app);
        entities = repo.getAllEntities();
    }
    LiveData<List<Entity>> getAllWords(){
        return entities;
    }
    public void insert(Entity e){
        repo.insert(e);
    }
    public void delete(int id){
        repo.delete(id);
    }
    public void update(EntityData e){
        repo.update(e);
    }
}

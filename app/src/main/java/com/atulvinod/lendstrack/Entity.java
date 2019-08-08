package com.atulvinod.lendstrack;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@android.arch.persistence.room.Entity(tableName = "entity_table")
public class Entity {
    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name="amount")
    private int mAmount;



    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="mID")
    private int mID;


    public Entity( int amount,String name,int mID){
        this.mAmount=amount;
        this.mID = mID;
        this.mName = name;
    }
    public String getName(){
        return this.mName;
    }

    public int getID(){return this.mID;}

    public void setName(String name){
        this.mName = name;
    }
    public void setID(int id){
        this.mID = id;
    }
    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int amount) {
        mAmount = amount;
    }

}

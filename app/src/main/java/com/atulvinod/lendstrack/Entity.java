package com.atulvinod.lendstrack;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.time.LocalDate;

@android.arch.persistence.room.Entity(tableName = "entity_table")
public class Entity {
    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name="amount")
    private int mAmount;

    @ColumnInfo(name="initialDiscription")
    private String mInitialDiscription;

    @ColumnInfo(name="givenOrTaken")
    private String givenOrTaken;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="mID")
    private int mID;

    @ColumnInfo(name = "date")
    String date;

    public Entity( int amount,String name,String initialDiscription,String givenOrTaken,int mID){
        this.mAmount=amount;
        this.mID = mID;
        this.mName = name;
        this.mInitialDiscription = initialDiscription;
        this.givenOrTaken = givenOrTaken;
        this.date =  LocalDate.now().toString();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGivenOrTaken(){
        return this.givenOrTaken;
    }
    public void setGivenOrTaken(){
        this.givenOrTaken = givenOrTaken;
    }
    public String getInitialDiscription(){
        return this.mInitialDiscription;
    }
    public void setInitialDiscription(String initialDiscription){
        this.mInitialDiscription = initialDiscription;
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

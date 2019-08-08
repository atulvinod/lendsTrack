package com.atulvinod.lendstrack;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;

@android.arch.persistence.room.Entity(tableName = "record_table")
public class Record {

    @ColumnInfo(name="Amount")
    String Amount;

    @ColumnInfo(name="Description")
    String Description;

    @ColumnInfo(name= "Date")
    String Date;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="SNo")
    int SNo;

    @ColumnInfo(name = "ID")
    int ID;

    public Record(String Amount,String Description,String Date,int ID){
        this.Amount = Amount;
        this.Description = Description;
        this.Date = Date;
        this.ID = ID;
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public int getSno(){
        return SNo;
    }
}

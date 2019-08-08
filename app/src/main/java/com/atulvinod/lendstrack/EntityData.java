package com.atulvinod.lendstrack;

public class EntityData {

    int id,amount;

    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public EntityData(int id,int am){
        this.id=id;
        this.amount = am;
    }
}

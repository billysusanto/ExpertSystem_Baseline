package com.mobile.expertsystem.model;

public class Organ {
    String name;
    int id;

    public Organ(){}

    public Organ(String name, int id){
        this.name = name;
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setId(int Id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}

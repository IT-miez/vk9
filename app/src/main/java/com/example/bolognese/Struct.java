package com.example.bolognese;

public class Struct {
    private String name;
    private String ID;

    public Struct(String name1, String ID1){
        name = name1;
        ID = ID1;
    }

    public String getID(){
        return ID;
    }

    public String getName(){
        return name;
    }
}

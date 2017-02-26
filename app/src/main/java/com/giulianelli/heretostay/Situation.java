package com.giulianelli.heretostay;

/**
 * Created by Thomas on 2/25/2017.
 */

public class Situation {
    String name;

    //constructor
    public Situation (String situationName){
        name = situationName;
    }

    //return name
    public String toString(){
        return name;
    }
}

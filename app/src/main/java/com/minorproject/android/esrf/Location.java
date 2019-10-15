package com.minorproject.android.esrf;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

@IgnoreExtraProperties
public class Location implements Serializable {
    public String name;
    public ArrayList<com.minorproject.android.esrf.LatLng> coords;

    public Location(){

    }

    public Location(String name,ArrayList<LatLng> coords){
        this.name = name;
        this.coords = new ArrayList<>();
        this.coords = coords;

    }
}

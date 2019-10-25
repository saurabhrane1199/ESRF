package com.minorproject.android.esrf.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class firstAidLoc implements Serializable {
    public String name;
    public Double latitude;
    public Double longitude;
    public String type;

    public firstAidLoc(String name,Double latitude,Double longitude,String type){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type =  type;
    }
    public firstAidLoc(){

    }



}

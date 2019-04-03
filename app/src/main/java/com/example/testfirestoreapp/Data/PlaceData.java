package com.example.testfirestoreapp.Data;

public class PlaceData {
    public CharSequence placeId;
    public CharSequence description;
    public CharSequence placename;
    public PlaceData(CharSequence id, CharSequence ds,CharSequence pname){
        this.placeId=id;
        this.description=ds;
        this.placename =pname;
    }

    public CharSequence getDescription() {
        return description;
    }

    public CharSequence getPlaceId() {
        return placeId;
    }

    public void setDescription(CharSequence description) {
        this.description = description;
    }

    public void setPlaceId(CharSequence placeId) {
        this.placeId = placeId;
    }

    public CharSequence getPlacename() {
        return placename;
    }

    public void setPlacename(CharSequence placename) {
        this.placename = placename;
    }
}

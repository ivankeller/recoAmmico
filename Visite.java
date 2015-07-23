package com.example.ivan.recoammico;

import android.database.Cursor;

public class Visite {

    private int _id;
    private String _date;
    private String _time;
    private double _posX;
    private double _posY;
    private double _posZ;
    private String _evenement;
    private String _id_poi;
    private int _id_com;

    protected Visite() {
    }

    protected Visite(int id, String date, String time, double posX, double posY, double posZ, String evenement, String id_poi, int id_com) {
        this._id = id;
        this._date = date;
        this._time = time;
        this._posX = posX;
        this._posY = posY;
        this._posZ = posZ;
        this._evenement = evenement;
        this._id_poi = id_poi;
        this._id_com = id_com;
    }

    // Constructor parsing Visite Database tuple
    protected Visite(Cursor cursor){
        this._id = cursor.getInt(cursor.getColumnIndex("ID"));
        this._date = cursor.getString(cursor.getColumnIndex("DATE"));
        this._time = cursor.getString(cursor.getColumnIndex("TIME"));
        this._posX = cursor.getDouble(cursor.getColumnIndex("POSITION__X"));
        this._posY = cursor.getDouble(cursor.getColumnIndex("POSITION__Y"));
        this._posZ = cursor.getDouble(cursor.getColumnIndex("POSITION__Z"));
        this._evenement = cursor.getString(cursor.getColumnIndex("EVENEMENT"));
        this._id_poi = cursor.getString(cursor.getColumnIndex("ID_POI"));
        this._id_com = cursor.getInt(cursor.getColumnIndex("ID_COM"));
    }

    protected int getId() {
        return this._id;
    }

    protected String getDate() {
        return this._date;
    }

    protected String getTime() {
        return this._time;
    }

    protected double getPosX() {
        return this._posX;
    }

    protected double getPosY() {
        return this._posY;
    }

    protected double getPosZ() {
        return this._posZ;
    }

    protected String getEvenement() {
        return this._evenement;
    }

    protected String getIdPoi() {
        return this._id_poi;
    }

    protected int getIdCom() {
        return this._id_com;
    }
}

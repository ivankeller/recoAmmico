package com.example.ivan.recoammico;

import android.database.Cursor;

/**
 * POI representation as described in the database: attributes correspond to column names in DB.
 * id correspond to the DB id and id_poi refers to the POI Id (stop_id_poi without the 'stop-'
 * Ex: id = 1; id_poi = "stop-345"; id_poi = 345
 */

public class POI {

    private int _id;
    private String _stop_id_poi;
    private int _id_poi;
    private String _title_poi;
    private String _tour_id;

    public static  int parseStopId(String stop_id) {
        String[] tokens = stop_id.split("-");
        return Integer.parseInt(tokens[1]);
    }

    protected POI(String stop_id_poi) {
        this._stop_id_poi = stop_id_poi;
        this._id_poi = this.parseStopId(stop_id_poi);
    }

    protected POI(int id, String stop_id_poi, String title_poi, String tour_id) {
        this._id = id;
        this._stop_id_poi = stop_id_poi;
        this._id_poi = this.parseStopId(this._stop_id_poi);
        this._title_poi = title_poi;
        this._tour_id = tour_id;
    }

    // Constructor parsing POI Database tuple
    protected POI(Cursor cursor){
        this._id = cursor.getInt(cursor.getColumnIndex("_id"));
        this._stop_id_poi = cursor.getString(cursor.getColumnIndex("ID_POI"));
        this._id_poi = this.parseStopId(this._stop_id_poi);
        this._title_poi = cursor.getString(cursor.getColumnIndex("TITLE_POI"));
        this._tour_id = cursor.getString(cursor.getColumnIndex("TOUR_ID"));
    }

    protected int getId() {
        return this._id;
    }

    protected String getStopIdPoi() {
        return this._stop_id_poi;
    }

    protected int getIdPoi() {
        return this._id_poi;
    }

    protected String getTitlePoi() {
        return this._title_poi;
    }

    protected String getTourId() {
        return this._tour_id;
    }

}

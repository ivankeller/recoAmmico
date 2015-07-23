package com.example.ivan.recoammico;

/**
 * Handler for VISITE table
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VisitesDBHandler {

    private String _visiteID;
    private static final String DB_PATH = RecommendationService.dbPath;
    private static final String VISITES_TABLE_NAME = "VISITES";
    private static final List EMPTY_LIST = Collections.unmodifiableList(new ArrayList());
    // for debugging:
    private static final String TAG = "reco";

    public VisitesDBHandler(String visiteID) {
        this._visiteID = visiteID;
    }

    // Safely open database.
    // Return null if exception occurs.
    public SQLiteDatabase openDatabaseSafe() {
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch(SQLiteException e){
            Log.i(TAG, "Error: could not open database.", e);
        }
        return db;
    }

    // Safely query database.
    // Return null if exception occurs
    public Cursor safeQuery(SQLiteDatabase db, String query) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
        } catch(SQLiteException e){
            Log.i(TAG, "Error: problem with database query.");
        }
        return cursor;
    }

    // Return a list of unique POI ids matching a evenement (Play, Like, etc.)
    // Return an empty list if any exception occurs.
    public List<Integer> getIdPOIs(String evenement) {
        List<Integer> uniqueIdPOIs = EMPTY_LIST;
        SQLiteDatabase db = openDatabaseSafe();
        if (db != null) {
            String columnName = "ID_POI";
            String conditionString = "ID = " + this._visiteID + " AND EVENEMENT = \"" + evenement + "\"";
            String query = "Select " + columnName + " FROM " + VISITES_TABLE_NAME + " WHERE " + conditionString;
            Cursor cursor = safeQuery(db, query);
            if (cursor != null) {
                List<Integer> idPOIs = POIsDBHandler.queryResultToList(cursor, columnName);
                Set<Integer> idPOIsSet = new HashSet<Integer>(idPOIs);  //remove duplicates
                uniqueIdPOIs = new ArrayList<Integer>(idPOIsSet);
            }
            db.close();
        }
        return uniqueIdPOIs;
    }

    public List<Integer> getLikedIdPOIs() {
        return getIdPOIs("Like");
    }

    public List<Integer> getVisitedIdPOIs() { return getIdPOIs("Play"); }

    public Integer getLastVisitedOrLikedPOI() {
        SQLiteDatabase db = openDatabaseSafe();
        List<Integer> idPOIs = EMPTY_LIST;
        if (db != null) {
            String query = "Select ID_POI, max(TIME) from VISITES where EVENEMENT = \"Play\" OR EVENEMENT = \"Like\"";
            Cursor cursor = safeQuery(db, query);
            if (cursor != null) {
                idPOIs = POIsDBHandler.queryResultToList(cursor, "ID_POI");
            }
            db.close();
        }
        Integer lastElem = 0;
        try {
            lastElem = idPOIs.get(idPOIs.size() - 1);
        } catch(IndexOutOfBoundsException e){
            Log.i(TAG, "Error: " + e);
        }
        return lastElem;
    }

//    public List<Visite> getPOIs(String evenement) {
//        List<Visite> POIs = new ArrayList<Visite>();
//        String conditionString = "ID = " + this._visiteID + " AND EVENEMENT = \"" + evenement + "\"";
//        String query = "Select * FROM " + VISITES_TABLE_NAME + " WHERE " + conditionString;
//        SQLiteDatabase db = openDatabaseSafe();
//        Cursor cursor = db.rawQuery(query, null);
//        if (cursor.moveToFirst()) {
//            do {
//                Visite poi = new Visite(cursor);
//                POIs.add(poi);
//            } while (cursor.moveToNext());
//        }
//        db.close();
//        return POIs;
//    }
//
//    public List<Visite> getVisitedPOIs() {
//        return getPOIs("Play");
//    }
//
//    public List<Visite> getLikedPOIs() {
//        return getPOIs("Like");
//    }

}
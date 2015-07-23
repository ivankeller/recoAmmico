package com.example.ivan.recoammico;

/**
 * Handler for POI table
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class POIsDBHandler {

    private static final String DB_PATH = RecommendationService.dbPath;
    private static final String POIS_TABLE_NAME = "POIS";
    private static final List EMPTY_LIST = Collections.unmodifiableList(new ArrayList());
    // for debugging:
    private static final String TAG = "reco";

    POIsDBHandler() {}

    // Safely open database.
    // Return null if exception occurs.
    public SQLiteDatabase openDatabaseSafe() {
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch(SQLiteException e){
            Log.i(TAG, "could not open database.");
        }
        return db;
    }

    // Safely query database.
    // Return null if exception occurs.
    public Cursor safeQuery(SQLiteDatabase db, String query) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
        } catch(SQLiteException e){
            Log.i(TAG, "could not query databse.");
        }
        return cursor;
    }

    // Return a list containing all the POI ids.
    // Return an empty list if any exception occurs.
    public List<Integer> getAllIdPOIs() {
        List<Integer> idPOIs = EMPTY_LIST;
        SQLiteDatabase db = openDatabaseSafe();
        if (db != null) {
            String columnName = "ID_POI";
            String query = "Select " + columnName + " FROM " + POIS_TABLE_NAME;
            Cursor cursor = safeQuery(db, query);
            if (cursor != null) {
                idPOIs = queryResultToList(cursor, columnName);
            }
            db.close();
        }
        return idPOIs;
    }

    // Helper function for retrieving in a list a specific result from a db query.
    protected static List<Integer> queryResultToList(Cursor cursor, String columnName) {
        List<Integer> idPOIs = new ArrayList<Integer>();
        if (cursor.moveToFirst()) {
            do {
                int idPOI = POI.parseStopId(cursor.getString(cursor.getColumnIndex(columnName)));
                idPOIs.add(idPOI);
            } while (cursor.moveToNext());
        }
        return idPOIs;
    }

//    public List<POI> getAllPOIs() {
//        List<POI> pois = EMPTY_LIST;
//        SQLiteDatabase db = openDatabaseSafe();
//        if (db != null) {
//            String query = "Select * FROM " + POIS_TABLE_NAME;
//            Cursor cursor = db.rawQuery(query, null);
//            if (cursor.moveToFirst()) {
//                do {
//                    POI poi = new POI(cursor);
//                    pois.add(poi);
//                } while (cursor.moveToNext());
//            }
//            db.close();
//        }
//        return pois;
//    }
}

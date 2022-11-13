package com.example.m_hikeapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.m_hikeapp.models.Hike;
import com.example.m_hikeapp.models.HikeObservation;
import com.example.m_hikeapp.utils.Constants;

public class DatabaseConnection extends SQLiteOpenHelper {

    private Context context;

    public DatabaseConnection(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Create Hike table in sqlite
        String query = "CREATE TABLE " + Constants.FIRST_TABLE_NAME
                + " (" + Constants.FIRST_TABLE_COLUM_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.FIRST_TABLE_COLUM_2 + " TEXT, "
                + Constants.FIRST_TABLE_COLUM_3 + " TEXT, "
                + Constants.FIRST_TABLE_COLUM_4 + " TEXT, "
                + Constants.FIRST_TABLE_COLUM_5 + " TEXT, "
                + Constants.FIRST_TABLE_COLUM_6 + " TEXT, "
                + Constants.FIRST_TABLE_COLUM_7 + " TEXT, "
                + Constants.FIRST_TABLE_COLUM_8 + " TEXT, "
                + Constants.FIRST_TABLE_COLUM_9 + " TEXT, "
                + Constants.FIRST_TABLE_COLUM_10 + " TEXT, "
                + Constants.FIRST_TABLE_COLUM_11 + " TEXT);";

        db.execSQL(query);

//        Create Observation table in sqlite
        String query1 = "CREATE TABLE " + Constants.SECOND_TABLE_NAME
                + " (" + Constants.SECOND_TABLE_COLUM_1 + " INTEGER, "
                + Constants.SECOND_TABLE_COLUM_2 + " TEXT, "
                + Constants.SECOND_TABLE_COLUM_3 + " TEXT, "
                + Constants.SECOND_TABLE_COLUM_4 + " TEXT, "
                + Constants.SECOND_TABLE_COLUM_5 + " TEXT);";

        db.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.FIRST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.SECOND_TABLE_NAME);
        onCreate(db);
    }

    public long addHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.FIRST_TABLE_COLUM_2, hike.getHikeName());
        contentValues.put(Constants.FIRST_TABLE_COLUM_3, hike.getLocation());
        contentValues.put(Constants.FIRST_TABLE_COLUM_4, hike.getDate());
        contentValues.put(Constants.FIRST_TABLE_COLUM_5, hike.getDistance());
        contentValues.put(Constants.FIRST_TABLE_COLUM_6, hike.getPurposeOfHike());
        contentValues.put(Constants.FIRST_TABLE_COLUM_7, hike.getDescription());
        contentValues.put(Constants.FIRST_TABLE_COLUM_8, hike.getNumberOfPersons());
        contentValues.put(Constants.FIRST_TABLE_COLUM_9, hike.getParkingAvailable());
        contentValues.put(Constants.FIRST_TABLE_COLUM_10, hike.getCamping());
        contentValues.put(Constants.FIRST_TABLE_COLUM_10, hike.getImageURL());

        return db.insert(Constants.FIRST_TABLE_NAME, null, contentValues);
    }

    public long addObservation(HikeObservation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.SECOND_TABLE_COLUM_1, observation.getId());
        contentValues.put(Constants.SECOND_TABLE_COLUM_2, observation.getObservation());
        contentValues.put(Constants.SECOND_TABLE_COLUM_3, observation.getTime());
        contentValues.put(Constants.SECOND_TABLE_COLUM_4, observation.getAdditionalComment());
        contentValues.put(Constants.SECOND_TABLE_COLUM_5, observation.getHikeObservationImage());

        return db.insert(Constants.SECOND_TABLE_NAME, null, contentValues);
    }

    public Cursor getAllHikes() {
        String query = Constants.SELECT_ALL_QUERY + Constants.FIRST_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getObservations(int id) {
        String tempString = " WHERE " + Constants.SECOND_TABLE_COLUM_1 + " = " + id;
        String query = Constants.SELECT_ALL_QUERY + Constants.SECOND_TABLE_NAME + tempString;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public long updateHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

//        contentValues.put(Constants.FIRST_TABLE_COLUM_1, hike.getId());
        contentValues.put(Constants.FIRST_TABLE_COLUM_2, hike.getHikeName());
        contentValues.put(Constants.FIRST_TABLE_COLUM_3, hike.getLocation());
        contentValues.put(Constants.FIRST_TABLE_COLUM_4, hike.getDate());
        contentValues.put(Constants.FIRST_TABLE_COLUM_5, hike.getDistance());
        contentValues.put(Constants.FIRST_TABLE_COLUM_6, hike.getPurposeOfHike());
        contentValues.put(Constants.FIRST_TABLE_COLUM_7, hike.getDescription());
        contentValues.put(Constants.FIRST_TABLE_COLUM_8, hike.getNumberOfPersons());
        contentValues.put(Constants.FIRST_TABLE_COLUM_9, hike.getParkingAvailable());
        contentValues.put(Constants.FIRST_TABLE_COLUM_10, hike.getCamping());
        contentValues.put(Constants.FIRST_TABLE_COLUM_10, hike.getImageURL());

        long result = db.update(Constants.FIRST_TABLE_NAME, contentValues,
                Constants.FIRST_TABLE_COLUM_1.concat("=?"), new String[]{String.valueOf(hike.getId())});

        return result;
    }

    public long deleteHike(String id) {
//        String tempString = " WHERE " + Constants.FIRST_TABLE_COLUM_1 + " = " + id;
//        String query = Constants.DELETE_QUERY + Constants.FIRST_TABLE_NAME + tempString;
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(Constants.FIRST_TABLE_NAME, Constants.FIRST_TABLE_COLUM_1.concat("=?"), new String[]{id});

        return result;
    }

    public void deleteAllHikes(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(Constants.DELETE_QUERY + Constants.FIRST_TABLE_NAME);
        db.execSQL(Constants.DELETE_QUERY + Constants.SECOND_TABLE_NAME);
    }
}




























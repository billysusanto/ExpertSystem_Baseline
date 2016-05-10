package com.mobile.expertsystem.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "m-Health";

    // Table Names
    private static final String TABLE_ATURAN = "aturan";
    private static final String TABLE_GEJALA = "gejala";
    private static final String TABLE_ORGAN = "organ";
    private static final String TABLE_PENYAKIT = "penyakit";

    // Common column names
    private static final String KEY_ID = "id";

    // NOTES Table - column names
    private static final String KEY_XML = "xml";

    // Table Create Statements
    private static final String CREATE_TABLE_ATURAN =
            "CREATE TABLE " + TABLE_ATURAN +
                    "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_XML + " TEXT )";

    // todo_tag table create statement
    private static final String CREATE_TABLE_GEJALA =
            "CREATE TABLE " + TABLE_GEJALA
                    + "(" + KEY_ID + " INTEGER,"
                    + KEY_XML + " TEXT)";

    private static final String CREATE_TABLE_PENYAKIT =
            "CREATE TABLE " + TABLE_PENYAKIT
                    + "(" + KEY_ID + " INTEGER,"
                    + KEY_XML + " TEXT)";

    private static final String CREATE_TABLE_ORGAN =
            "CREATE TABLE " + TABLE_ORGAN
                    + "(" + KEY_ID + " INTEGER,"
                    + KEY_XML + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_ATURAN);
        db.execSQL(CREATE_TABLE_GEJALA);
        db.execSQL(CREATE_TABLE_PENYAKIT);
        db.execSQL(CREATE_TABLE_ORGAN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ATURAN);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_GEJALA);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_PENYAKIT);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ORGAN);

        // create new tables
        onCreate(db);
    }

    // =====================  ATURAN - START ================================
    public long insertAturan (String xml){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, 1);
        values.put(KEY_XML, xml);

        Log.e("CREATE INIT", CREATE_TABLE_ATURAN);

        long aturan_id = db.insert(TABLE_ATURAN, null, values);

        return aturan_id;
    }

    public List <String> getAturan(){
        List<String> xmlAturan = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ATURAN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                xmlAturan.add(c.getString(c.getColumnIndex(KEY_XML)));
            } while (c.moveToNext());
        }
        return xmlAturan;
    }

    public void resetDBAturan(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATURAN);
        db.execSQL(CREATE_TABLE_ATURAN);
    }

    // =====================  ATURAN - END ==================================

    // =====================  Penyakit - START ================================
    public long insertPenyakit (String xml){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, 1);
        values.put(KEY_XML, xml);

        Log.e("CREATE INIT", CREATE_TABLE_PENYAKIT);

        long penyakit_id = db.insert(TABLE_PENYAKIT, null, values);

        return penyakit_id;
    }

    public List <String> getPenyakit(){
        List<String> xmlPenyakit = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PENYAKIT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                xmlPenyakit.add(c.getString(c.getColumnIndex(KEY_XML)));
            } while (c.moveToNext());
        }
        return xmlPenyakit;
    }

    public void resetDBPenyakit(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENYAKIT);
        db.execSQL(CREATE_TABLE_PENYAKIT);
    }

    // =====================  PENYAKIT - END ==================================

    // =====================  GEJALA - START ================================
    public long insertGejala (String xml){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, 1);
        values.put(KEY_XML, xml);

        Log.e("CREATE INIT", CREATE_TABLE_GEJALA);

        long gejala_id = db.insert(TABLE_GEJALA, null, values);

        return gejala_id;
    }

    public List <String> getGejala(){
        List<String> xmlGejala = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_GEJALA;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                xmlGejala.add(c.getString(c.getColumnIndex(KEY_XML)));
            } while (c.moveToNext());
        }
        return xmlGejala;
    }

    public void resetDBGejala(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GEJALA);
        db.execSQL(CREATE_TABLE_GEJALA);
    }

    // =====================  GEJALA - END ==================================

    // =====================  ORGAN - START ================================
    public long insertOrgan (String xml){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, 1);
        values.put(KEY_XML, xml);

        Log.e("CREATE INIT", CREATE_TABLE_ORGAN);

        long organ_id = db.insert(TABLE_ORGAN, null, values);

        return organ_id;
    }

    public List <String> getOrgan(){
        List<String> xmlOrgan = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORGAN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                xmlOrgan.add(c.getString(c.getColumnIndex(KEY_XML)));
            } while (c.moveToNext());
        }
        return xmlOrgan;
    }

    public void resetDBOrgan(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORGAN);
        db.execSQL(CREATE_TABLE_ORGAN);
    }

    // =====================  ORGAN - END ==================================
}
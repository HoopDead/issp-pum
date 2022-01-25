package com.example.studentcrimelab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;
import java.util.UUID;


public class DBHandler extends SQLiteOpenHelper {


    CrimeLab mCrimeLab;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "CrimeDB.db";

    public static final String TABLE_NAME = "crimes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CRIME_ID = "crime_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SOLVED = "solved";
    public static final String COLUMN_IMAGE = "image";

    public DBHandler(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);

        mCrimeLab = CrimeLab.get(context);

    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        System.out.println("[INFO] Initialized empty crime database.");
        String INITIALIZE_TABLE = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s BOOLEAN, %s TEXT)", TABLE_NAME, COLUMN_ID, COLUMN_CRIME_ID, COLUMN_TITLE, COLUMN_DATE, COLUMN_SOLVED, COLUMN_IMAGE
        );
        db.execSQL(INITIALIZE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
        onCreate(db);
    }

    public Cursor getCrimes()
    {
        String q = String.format("SELECT * FROM %s", TABLE_NAME);
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(q, null);
    }

    public Cursor getCrime(UUID id)
    {
        String uuidString = id.toString();
        String q = String.format("SELECT * FROM %s WHERE = ?", TABLE_NAME);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(q, new String[]{String.valueOf(uuidString)});
    }

    public void addCrime(Crime crime)
    {
        ContentValues v = new ContentValues();
        v.put(COLUMN_CRIME_ID, crime.getId().toString());
        v.put(COLUMN_TITLE, crime.getTitle());
        v.put(COLUMN_DATE, crime.getDate().toString());
        v.put(COLUMN_SOLVED, crime.isSolved());
        v.put(COLUMN_IMAGE, crime.getImage());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, v);
        db.close();

        mCrimeLab.addCrime(crime);
    }

    public void deleteCrime(Crime crime)
    {
        String uuidString = crime.getId().toString();
        String q = String.format("SELECT * FROM %s WHERE %s = %s", TABLE_NAME, COLUMN_CRIME_ID, uuidString);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_CRIME_ID + " = ?", new String[]{String.valueOf(uuidString)});
        db.close();

        mCrimeLab.deleteCrime(crime.getId());
    }

    public void updateCrime(UUID id, String title, Date date, boolean solved, String image)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DATE, date.toString());
        cv.put(COLUMN_SOLVED, solved);
        cv.put(COLUMN_IMAGE, image);

        db.update(TABLE_NAME, cv, COLUMN_CRIME_ID + " = ?", new String[]{String.valueOf(id.toString())});
        db.close();
    }
}

package com.example.fitnesstracker;

import static com.example.fitnesstracker.DatabaseUser.DATABASE_NAME;
import static com.example.fitnesstracker.DatabaseUser.DATABASE_VERSION;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseFitness extends SQLiteOpenHelper {

    private static final String TABLE_FITNESS = "Fitness";
    private static final String COLUMN_FITNESS_ID = "fitnessID";
    private static final String COLUMN_USER_ID = "userID";
    private static final String COLUMN_DATE = "fitnessDate";
    private static final String COLUMN_WALK = "fitnessWalk";
    private String CREATE_FITNESS_TABLE = "CREATE TABLE " + TABLE_FITNESS + "("
            + COLUMN_FITNESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_DATE + " STRING," + COLUMN_WALK + "INTEGER "
            + "CONSTRAINT fk_users FOREIGN KEY(" +COLUMN_USER_ID + ")"
            + "REFERENCES " + DATABASE_NAME + "(" + DatabaseUser.COLUMN_USER_ID + ")"
                    + ")";
    private String DROP_FITNESS_TABLE = "DROP TABLE IF EXISTS " + TABLE_FITNESS;
    public DatabaseFitness(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FITNESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_FITNESS_TABLE);
        onCreate(db);
    }

    public void addFitnessToday(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        values.put(COLUMN_DATE, format.format(date));
        values.put(COLUMN_WALK, 0);
    }
    @SuppressLint("Range")
    public void getFitness(int userId, String date) {
        String[] columns = {
                COLUMN_FITNESS_ID,
                COLUMN_WALK
        };
        String whereClause = COLUMN_USER_ID + " = " + userId + " AND " + COLUMN_DATE + " = " + date;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FITNESS,
                columns,
                whereClause,
                null,
                null,
                null,
                null);
        Fitness fitness = new Fitness();
        if(cursor.moveToFirst()) {
            fitness.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_FITNESS_ID))));
            fitness.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
            fitness.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            fitness.setWalk(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_WALK))));
        }
        cursor.close();
        db.close();
    }
}

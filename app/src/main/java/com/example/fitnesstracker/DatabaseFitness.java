package com.example.fitnesstracker;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseFitness extends DatabaseMain {

    private static final String TABLE_FITNESS = "Fitness";
    private static final String COLUMN_FITNESS_ID = "fitnessID";
    private static final String COLUMN_USER_ID = "userID";
    private static final String COLUMN_DATE = "fitnessDate";
    private static final String COLUMN_WALK = "fitnessWalk";
    public static String CREATE_FITNESS_TABLE = "CREATE TABLE " + TABLE_FITNESS + "("
            + COLUMN_FITNESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_DATE + " STRING," + COLUMN_WALK + " INTEGER, "
            + "FOREIGN KEY (" +COLUMN_USER_ID + ") "
            + " REFERENCES " + DatabaseUser.TABLE_USER + " (" + DatabaseUser.COLUMN_USER_ID + ")"
                    + ")";
    public static String UPDATE_FITNESS_TABLE = "UPDATE " + TABLE_FITNESS + " SET ";
    public static String DROP_FITNESS_TABLE = "DROP TABLE IF EXISTS " + TABLE_FITNESS;
    public DatabaseFitness(Context context) {
        super(context);
    }

    public void addFitness(int userId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        values.put(COLUMN_DATE, format.format(date));
        values.put(COLUMN_WALK, 0);
        db.insert(TABLE_FITNESS, null, values);
        db.close();
    }
    private void addFitness(int userId, String date) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_WALK, 0);
        db.insert(TABLE_FITNESS, null, values);
//        dbWritable.close();
        db.close();
    }
    @SuppressLint("Range")
    public Fitness getFitness(int userId, String date) {

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                COLUMN_FITNESS_ID,
                COLUMN_WALK
        };
        String whereClause = COLUMN_USER_ID + " = " + userId + " AND " + COLUMN_DATE + " = " + date;

        Cursor cursor = db.query(TABLE_FITNESS,
                columns,
                whereClause,
                null,
                null,
                null,
                null);
        Fitness fitness = new Fitness(userId, date);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date dateToday = new Date();

        if(cursor.moveToFirst()) {
            fitness.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_FITNESS_ID))));
            fitness.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
            fitness.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            fitness.setWalk(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_WALK))));
        } else if (date.equals(dateToday)) {
            addFitness(userId);
        }
        cursor.close();
        db.close();
        return fitness;
    }
    public void updateFitnessValue(int userId, String date, int value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_WALK, value);
        String whereClause = COLUMN_USER_ID + " = " + userId + " AND " + COLUMN_DATE + " = " + date;
        db.update(TABLE_FITNESS, values, whereClause,null);
        db.close();
    }
}

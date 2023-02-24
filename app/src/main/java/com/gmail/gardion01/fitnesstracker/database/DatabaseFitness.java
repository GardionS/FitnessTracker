package com.gmail.gardion01.fitnesstracker.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.gardion01.fitnesstracker.model.Fitness;

public class DatabaseFitness extends DatabaseMain {

    public static final String TABLE_FITNESS = "Fitness";
    public static final String COLUMN_FITNESS_ID = "fitnessID";
    public static final String COLUMN_USER_ID = "userID";
    public static final String COLUMN_FITNESS_TYPE = "fitnessType";
    public static final String COLUMN_DATE = "fitnessDate";
    public static final String COLUMN_VALUE = "fitnessValue";
    public static String CREATE_FITNESS_TABLE = "CREATE TABLE " + TABLE_FITNESS + "("
            + COLUMN_FITNESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_FITNESS_TYPE + " INTEGER, " + COLUMN_DATE + " STRING,"  + COLUMN_VALUE + " INTEGER,"
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") "
            + "REFERENCES " + DatabaseUser.TABLE_USER + " (" + DatabaseUser.COLUMN_USER_ID + "), "
            + "FOREIGN KEY (" + COLUMN_FITNESS_TYPE + ") "
            + "REFERENCES " + DatabaseFitnessType.TABLE_FITNESS_TYPE + " (" + DatabaseFitnessType.COLUMN_FITNESS_TYPE_ID + ")"
            + ")";
    public static String UPDATE_FITNESS_TABLE = "UPDATE " + TABLE_FITNESS + " SET ";
    public static String DROP_FITNESS_TABLE = "DROP TABLE IF EXISTS " + TABLE_FITNESS;

    public DatabaseFitness(Context context) {
        super(context);
    }

    public void addFitness(int userId, int type) { //Today

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_DATE, DatabaseMain.getCurrentDate("dd-MM-yyyy"));
        values.put(COLUMN_FITNESS_TYPE, type);
        values.put(COLUMN_VALUE, 0);
        db.insert(TABLE_FITNESS, null, values);
        db.close();
    }
    public void addFitness(int userId, int type, String date) { //Specific Date

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_FITNESS_TYPE, type);
        values.put(COLUMN_VALUE, 0);
        db.insert(TABLE_FITNESS, null, values);
        db.close();
    }
    @SuppressLint("Range")
    public Fitness getFitness(int userId, int type, String date) {

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                COLUMN_FITNESS_ID,
                COLUMN_VALUE
        };
        String whereClause = COLUMN_USER_ID + " = ?" + " AND " + COLUMN_FITNESS_TYPE + " = ?"+ " AND " + COLUMN_DATE + " = ?" ;
        String[] selectionArgs = {Integer.toString(userId), Integer.toString(type) , date};
        Cursor cursor = db.query(TABLE_FITNESS,
                columns,
                whereClause,
                selectionArgs,
                null,
                null,
                null);
        Fitness fitness = new Fitness(userId, type, date);

        if (cursor.moveToFirst()) { //Check if there is data
            fitness.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_FITNESS_ID))));
            fitness.setValue(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_VALUE))));
        } else if (date.equals(DatabaseMain.getCurrentDate("dd-MM-yyyy"))) { //There is no data in db for today, so create new record
            addFitness(userId, type);
            fitness.setValue(0);
        }
        cursor.close();
        db.close();
        return fitness;
    }

    public void updateFitness(int userId, int type, String date, int value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_VALUE, value);
        String whereClause = COLUMN_USER_ID + " = ? " + " AND " + COLUMN_FITNESS_TYPE + " = ?" + " AND " + COLUMN_DATE + " = ?";
        String[] selectionArgs = {Integer.toString(userId), Integer.toString(type), date};
        db.update(TABLE_FITNESS, values, whereClause, selectionArgs);
        db.close();
    }

}

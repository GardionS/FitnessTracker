package com.gmail.gardion01.fitnesstracker.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.gmail.gardion01.fitnesstracker.model.FitnessType;
import com.gmail.gardion01.fitnesstracker.model.QuestType;

public class DatabaseFitnessType extends DatabaseMain{
    public static final String TABLE_FITNESS_TYPE = "fitnessType";
    public static final String COLUMN_FITNESS_TYPE_ID = "fitnessTypeId";
    public static final String COLUMN_FITNESS_NAME = "fitnessName";
    public static final String COLUMN_FITNESS_TARGET_VALUE = "fitnessTargetValue";
    public static String CREATE_FITNESS_TYPE_TABLE = "CREATE TABLE " + TABLE_FITNESS_TYPE + "("
            + COLUMN_FITNESS_TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FITNESS_NAME + " STRING,"
            + COLUMN_FITNESS_TARGET_VALUE + " INTEGER" + ")";
    public static String DROP_FITNESS_TYPE_TABLE = "DROP TABLE IF EXISTS " + TABLE_FITNESS_TYPE;

    public DatabaseFitnessType(@Nullable Context context) {
        super(context);
    }
    public void addFitnessType(int fitnessTypeId, String fitnessName, int targetValue) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FITNESS_TYPE_ID, fitnessTypeId);
        values.put(COLUMN_FITNESS_NAME, fitnessName);
        values.put(COLUMN_FITNESS_TARGET_VALUE, targetValue);
        db.insert(TABLE_FITNESS_TYPE, null, values);
        db.close();
    }
    @SuppressLint("Range")
    public FitnessType getFitnessType(int fitnessId) {

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                COLUMN_FITNESS_NAME,
                COLUMN_FITNESS_TARGET_VALUE
        };
        String whereClause = COLUMN_FITNESS_TYPE_ID + " = ?" ;
        String[] selectionArgs = {Integer.toString(fitnessId)};
        Cursor cursor = db.query(TABLE_FITNESS_TYPE,
                columns,
                whereClause,
                selectionArgs,
                null,
                null,
                null);
        FitnessType fitnessType = new FitnessType(fitnessId);

        if (cursor.moveToFirst()) { //Check if there is data
            fitnessType.setName(cursor.getString(cursor.getColumnIndex(COLUMN_FITNESS_NAME)));
            fitnessType.setTargetValue(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_FITNESS_TARGET_VALUE))));
        }
        cursor.close();
        db.close();
        return fitnessType;
    }
}

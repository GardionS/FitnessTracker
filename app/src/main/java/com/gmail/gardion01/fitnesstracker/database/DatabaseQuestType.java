package com.gmail.gardion01.fitnesstracker.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.gmail.gardion01.fitnesstracker.model.Fitness;
import com.gmail.gardion01.fitnesstracker.model.QuestType;

public class DatabaseQuestType extends DatabaseMain {

    public static final String TABLE_QUEST_TYPE = "questType";
    public static final String COLUMN_QUEST_TYPE_ID = "questTypeId";
    private static final String COLUMN_FITNESS_TYPE = "fitnessType";
    private static final String COLUMN_QUEST_NAME = "questName";
    private static final String COLUMN_QUEST_TARGET_VALUE = "questTargetValue";
    public static String CREATE_QUEST_TYPE_TABLE = "CREATE TABLE " + TABLE_QUEST_TYPE + "("
            + COLUMN_QUEST_TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FITNESS_TYPE + " INTEGER,"
            + COLUMN_QUEST_NAME + " STRING," + COLUMN_QUEST_TARGET_VALUE + " INTEGER, "
            + "FOREIGN KEY (" + COLUMN_FITNESS_TYPE + ") "
            + " REFERENCES " + DatabaseFitnessType.TABLE_FITNESS_TYPE + " (" + DatabaseFitnessType.COLUMN_FITNESS_TYPE_ID + ") "
            + ")";
    public static String DROP_QUEST_TYPE_TABLE = "DROP TABLE IF EXISTS " + TABLE_QUEST_TYPE;

    public DatabaseQuestType(@Nullable Context context) {
        super(context);
    }
    public void addQuestType(int questTypeId, int fitnessType, String questName, int targetValue) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUEST_TYPE_ID, questTypeId);
        values.put(COLUMN_FITNESS_TYPE, fitnessType);
        values.put(COLUMN_QUEST_NAME, questName);
        values.put(COLUMN_QUEST_TARGET_VALUE, targetValue);
        db.insert(TABLE_QUEST_TYPE, null, values);
        db.close();
    }
    @SuppressLint("Range")
    public QuestType getQuestType(int questTypeId) {

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                COLUMN_FITNESS_TYPE,
                COLUMN_QUEST_NAME,
                COLUMN_QUEST_TARGET_VALUE
        };
        String whereClause = COLUMN_QUEST_TYPE_ID + " = ?" ;
        String[] selectionArgs = {Integer.toString(questTypeId)};
        Cursor cursor = db.query(TABLE_QUEST_TYPE,
                columns,
                whereClause,
                selectionArgs,
                null,
                null,
                null);
        QuestType questType = new QuestType(questTypeId);

        if (cursor.moveToFirst()) { //Check if there is data
            questType.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_FITNESS_TYPE))));
            questType.setQuestName(cursor.getString(cursor.getColumnIndex(COLUMN_QUEST_NAME)));
            questType.setQuestTargetValue(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_QUEST_TARGET_VALUE))));
        }
        cursor.close();
        db.close();
        return questType;
    }
}

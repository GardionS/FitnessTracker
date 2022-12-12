package com.gmail.gardion01.fitnesstracker.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.gmail.gardion01.fitnesstracker.model.Quest;
import com.gmail.gardion01.fitnesstracker.model.QuestType;

public class DatabaseQuest extends DatabaseMain{

    public static final String TABLE_QUEST = "quest";

    public static final String COLUMN_QUEST_ID = "questId";
    private static final String COLUMN_USER_ID = "userId";
    private static final String COLUMN_QUEST_TYPE = "questType";
    private static final String COLUMN_QUEST_DATE = "questDate";
    private static final String COLUMN_QUEST_VALUE = "questValue";
    public static String CREATE_QUEST_TABLE = "CREATE TABLE " + TABLE_QUEST + "("
            + COLUMN_QUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_QUEST_TYPE + " INTEGER," + COLUMN_QUEST_DATE + " STRING, " + COLUMN_QUEST_VALUE + " INTEGER, "
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") "
            + " REFERENCES " + DatabaseUser.TABLE_USER + " (" + DatabaseUser.COLUMN_USER_ID + "), "
            + "FOREIGN KEY (" + COLUMN_QUEST_TYPE + ") "
            + " REFERENCES " + DatabaseQuestType.TABLE_QUEST_TYPE + " (" + DatabaseQuestType.COLUMN_QUEST_TYPE_ID + ")"
            + ")";
    public static String DROP_QUEST_TABLE = "DROP TABLE IF EXISTS " + TABLE_QUEST;

    public DatabaseQuest(@Nullable Context context) {
        super(context);
    }

    public void addQuest(int userId, int questTypeId, String date, int value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_QUEST_TYPE, questTypeId);
        values.put(COLUMN_QUEST_DATE, date);
        values.put(COLUMN_QUEST_VALUE, value);
        db.insert(TABLE_QUEST, null, values);
        db.close();
    }
    @SuppressLint("Range")
    public Quest getQuest(int userId, int questTypeId, String date) {

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                COLUMN_QUEST_ID,
                COLUMN_QUEST_VALUE
        };
        String whereClause = COLUMN_USER_ID + " = ?" + " AND " + COLUMN_QUEST_TYPE + " = ?" + " AND " + COLUMN_QUEST_DATE + " = ?"  ;
        String[] selectionArgs = {Integer.toString(userId), Integer.toString(questTypeId), date};
        Cursor cursor = db.query(TABLE_QUEST,
                columns,
                whereClause,
                selectionArgs,
                null,
                null,
                null);
        Quest quest = new Quest(userId, questTypeId, date);

        if (cursor.moveToFirst()) { //Check if there is data

            quest.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_QUEST_ID))));
            quest.setValue(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_QUEST_VALUE))));
        }
        cursor.close();
        db.close();
        return quest;
    }
    public void completeQuest(int userId, int questType, String date, int value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUEST_VALUE, value);
        String whereClause = COLUMN_USER_ID + " = ?" + " AND " + COLUMN_QUEST_TYPE + " = ?" + " AND " + COLUMN_QUEST_DATE + " = ?" ;
        String[] selectionArgs = {Integer.toString(userId), Integer.toString(questType), date};
        db.update(TABLE_QUEST, values, whereClause, selectionArgs);
        db.close();
    }
}

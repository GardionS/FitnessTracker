package com.example.fitnesstracker;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseUser extends DatabaseMain {
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID = "userId";
    private static final String COLUMN_USER_NAME = "userName";
    private static final String COLUMN_USER_EMAIL = "userEmail";
    private static final String COLUMN_USER_PASSWORD = "userPassword";
    private static final String COLUMN_USER_AGE = "userAge";
    private static final String COLUMN_USER_WEIGHT = "userWeight";
    private static final String COLUMN_USER_EXP = "userExp";
    private static final String COLUMN_USER_DAILY_QUEST = "userDailyQuest";
    public static String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_USER_AGE + " INTEGER," + COLUMN_USER_WEIGHT + " INTEGER,"
            + COLUMN_USER_EXP + " INTEGER," + COLUMN_USER_DAILY_QUEST + " BOOLEAN" + ")";
    public static String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    public DatabaseUser(Context context) {
        super(context);

    }

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_AGE, user.getAge());
        values.put(COLUMN_USER_WEIGHT, user.getWeight());
        values.put(COLUMN_USER_EXP, 0);
        values.put(COLUMN_USER_DAILY_QUEST, false);
        user.setExp(0);
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public User getUser(String email) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_AGE,
                COLUMN_USER_WEIGHT,
                COLUMN_USER_EXP,
                COLUMN_USER_DAILY_QUEST
        };
        String whereClause = COLUMN_USER_EMAIL + " = '" + email + "'";

        Cursor cursor = db.query(TABLE_USER,
                columns,
                whereClause,
                null,
                null,
                null,
                null);

        User user = new User();
        if (cursor.moveToFirst()) {
            user = new User(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_WEIGHT))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_AGE))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EXP))),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_USER_DAILY_QUEST))));
        }
        cursor.close();
        db.close();
        return user;
    }

    public void updateUser(int userId, String username, String email, int age, int weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, username);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_AGE, age);
        values.put(COLUMN_USER_WEIGHT, weight);
        String whereClause = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {Integer.toString(userId)};
        db.update(TABLE_USER,
                values,
                whereClause,
                selectionArgs);
        db.close();
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public boolean checkUser(String email) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                COLUMN_USER_ID
        };
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    public boolean checkUser(String email, String password) {

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                COLUMN_USER_ID
        };
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    @SuppressLint("Range")
    public Boolean comparePassword(int userId, String oldPassword) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                COLUMN_USER_PASSWORD
        };
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {Integer.toString(userId)};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        String password = null;
        if (cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD));
        }
        return password.equals(oldPassword);
    }

    public void insertNewPassword(int userId, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASSWORD, newPassword);
        String whereClause = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {Integer.toString(userId)};
        db.update(TABLE_USER,
                values,
                whereClause,
                selectionArgs);
        db.close();
    }

    public void completeDailyQuest(int userId, int exp) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_DAILY_QUEST, true);
        values.put(COLUMN_USER_EXP, exp);
        String whereClause = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {Integer.toString(userId)};
        db.update(TABLE_USER,
                values,
                whereClause,
                selectionArgs);
        db.close();
    }
}
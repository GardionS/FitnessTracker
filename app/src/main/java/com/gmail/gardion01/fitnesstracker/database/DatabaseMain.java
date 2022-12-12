package com.gmail.gardion01.fitnesstracker.database;

import static com.gmail.gardion01.fitnesstracker.enumeration.FitnessType.RUNNING;
import static com.gmail.gardion01.fitnesstracker.enumeration.FitnessType.WALKING;
import static com.gmail.gardion01.fitnesstracker.enumeration.QuestType.DAILY_QUEST;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseMain extends SQLiteOpenHelper {//
    public static final int DATABASE_VERSION = 11;
    public static final String DATABASE_NAME = "FitnessTracker.db";
    private Context context;
    public DatabaseMain(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static String getCurrentDate(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = new Date();
        return format.format(date);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseUser.CREATE_USER_TABLE);
        db.execSQL(DatabaseQuestType.CREATE_QUEST_TYPE_TABLE);
        db.execSQL(DatabaseFitnessType.CREATE_FITNESS_TYPE_TABLE);
        db.execSQL(DatabaseQuest.CREATE_QUEST_TABLE);
        db.execSQL(DatabaseFitness.CREATE_FITNESS_TABLE);
//        db.close();
//        initDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseFitness.DROP_FITNESS_TABLE);
        db.execSQL(DatabaseUser.DROP_USER_TABLE);
        db.execSQL(DatabaseQuestType.DROP_QUEST_TYPE_TABLE);
        db.execSQL(DatabaseFitnessType.DROP_FITNESS_TYPE_TABLE);
        onCreate(db);
    }

    public void initDatabase() {
        DatabaseQuestType databaseQuestType = new DatabaseQuestType(context);
        databaseQuestType.addQuestType(DAILY_QUEST.getValue(), WALKING.getValue(), "Daily Quest", 6000);
        DatabaseFitnessType databaseFitnessType = new DatabaseFitnessType(context);
        databaseFitnessType.addFitnessType(WALKING.getValue(), WALKING.name(), 6000);
        databaseFitnessType.addFitnessType(RUNNING.getValue(), RUNNING.name(), 2000);
    }
    public boolean checkVersion(){
        SQLiteDatabase db = getWritableDatabase();
        if(db.getVersion() != DATABASE_VERSION) {
            onUpgrade(db, db.getVersion(), DATABASE_VERSION);
            return true;
        }
        return false;
    }
}

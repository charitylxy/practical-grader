package com.example.prac_grader.sql_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.prac_grader.sql_database.DBSchema.AccountTable;
import com.example.prac_grader.sql_database.DBSchema.StudentTable;
import com.example.prac_grader.sql_database.DBSchema.PracticalTable;
import com.example.prac_grader.sql_database.DBSchema.ResultTable;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "pracGrader.db";

    public DBHelper (Context context){
        super (context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + AccountTable.NAME + "(" +
                AccountTable.Cols.USERNAME + " TEXT PRIMARY KEY, " +
                AccountTable.Cols.PIN + " INTEGER, " +
                AccountTable.Cols.NAME + " TEXT, " +
                AccountTable.Cols.EMAIL + " TEXT, " +
                AccountTable.Cols.COUNTRY + " INTEGER, " +
                AccountTable.Cols.TYPE + " INTEGER)");

        db.execSQL("CREATE TABLE " + StudentTable.NAME + "(" +
                StudentTable.Cols.USERNAME + " TEXT, " +
                StudentTable.Cols.INSTRUCTOR + " TEXT)");

        db.execSQL("CREATE TABLE " + PracticalTable.NAME + "(" +
                PracticalTable.Cols.ID + " TEXT PRIMARY KEY, " +
                PracticalTable.Cols.TITLE + " TEXT, " +
                PracticalTable.Cols.DESC + " TEXT, " +
                PracticalTable.Cols.MAX_MARKS + " DOUBLE)");

        db.execSQL("CREATE TABLE " + ResultTable.NAME + "(" +
                ResultTable.Cols.ID + " TEXT PRIMARY KEY, " +
                ResultTable.Cols.PRAC_ID + " TEXT, " +
                ResultTable.Cols.STUDENT + " TEXT, " +
                ResultTable.Cols.MARKS + " DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        throw new UnsupportedOperationException("Sorry");
    }
}


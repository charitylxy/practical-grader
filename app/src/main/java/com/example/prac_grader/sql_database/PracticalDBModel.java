package com.example.prac_grader.sql_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prac_grader.classes.Account;
import com.example.prac_grader.classes.Practical;

import java.util.ArrayList;
import java.util.List;

public class PracticalDBModel {
    private List<Practical> practicals = new ArrayList<>();
    private SQLiteDatabase db;

    public PracticalDBModel(){}

    public void load (Context context){
        this.db = new DBHelper(context).getWritableDatabase();
    }

    public int size(){
        return practicals.size();
    }

    public Practical get(int i)
    {
        return practicals.get(i);
    }

    public void add (Practical newPractical){
        practicals.add(newPractical);
        ContentValues cv = new ContentValues();
        cv.put (DBSchema.PracticalTable.Cols.ID, newPractical.getId());
        cv.put (DBSchema.PracticalTable.Cols.TITLE, newPractical.getTitle());
        cv.put (DBSchema.PracticalTable.Cols.DESC, newPractical.getDescription());
        cv.put (DBSchema.PracticalTable.Cols.MAX_MARKS, newPractical.getMarks());
        db.insert(DBSchema.PracticalTable.NAME, null, cv);
    }

    public void edit (Practical newPractical){
        ContentValues cv = new ContentValues();
        cv.put (DBSchema.PracticalTable.Cols.ID, newPractical.getId());
        cv.put (DBSchema.PracticalTable.Cols.TITLE, newPractical.getTitle());
        cv.put (DBSchema.PracticalTable.Cols.DESC, newPractical.getDescription());
        cv.put (DBSchema.PracticalTable.Cols.MAX_MARKS, newPractical.getMarks());

        String[] whereValues = {String.valueOf(newPractical.getId())};
        db.update(DBSchema.PracticalTable.NAME, cv,
                DBSchema.PracticalTable.Cols.ID + "=?", whereValues);
    }

    public void remove(Practical rmPractical)
    {
        practicals.remove(rmPractical);
        // remove faction from database
        String[] whereValues = {String.valueOf(rmPractical.getId())};
        db.delete(DBSchema.PracticalTable.NAME,
                DBSchema.PracticalTable.Cols.ID + "=?",
                whereValues);
    }

    //get all practicals from database
    public List<Practical> getAllPracticals(){
        List <Practical> practicalList = new ArrayList<>();
        Cursor cursor = db.query(DBSchema.PracticalTable.NAME,
                null,null,null,
                null,null,null);
        DBCursor practicalDBCursor = new DBCursor(cursor);

        try{
            practicalDBCursor.moveToFirst();
            while (!practicalDBCursor.isAfterLast()){
                practicalList.add(practicalDBCursor.getPractical());
                practicalDBCursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }
        return practicalList;
    }




}

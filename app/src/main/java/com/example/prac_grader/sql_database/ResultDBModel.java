package com.example.prac_grader.sql_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prac_grader.classes.Account;
import com.example.prac_grader.classes.Practical;
import com.example.prac_grader.classes.Result;

import java.util.ArrayList;
import java.util.List;

public class ResultDBModel {
    private List<Result> results = new ArrayList<>();
    private SQLiteDatabase db;

    public ResultDBModel(){}

    public void load (Context context){
        this.db = new DBHelper(context).getWritableDatabase();
    }

    public int size(){
        return results.size();
    }

    public Result get(int i)
    {
        return results.get(i);
    }

    public void add (Result newResult){
        results.add(newResult);
        ContentValues cv = new ContentValues();
        cv.put (DBSchema.ResultTable.Cols.ID, newResult.getId());
        cv.put (DBSchema.ResultTable.Cols.STUDENT, newResult.getStudent_username());
        cv.put (DBSchema.ResultTable.Cols.PRAC_ID, newResult.getPractical_id());
        cv.put (DBSchema.ResultTable.Cols.MARKS, newResult.getMarks());
        db.insert(DBSchema.ResultTable.NAME, null, cv);
        //return accounts.size() -1;
    }

    public void remove(Result rmResult) {
        results.remove(rmResult);
        // remove faction from database
        String[] whereValues = {String.valueOf(rmResult.getId())};
        db.delete(DBSchema.ResultTable.NAME,
                DBSchema.ResultTable.Cols.ID + "=?",
                whereValues);
    }

    //get all student's result from database
    public List<Result> getStudentResults(String username){
        List <Result> resultList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBSchema.ResultTable.NAME + " WHERE "
                + DBSchema.ResultTable.Cols.STUDENT + " = '" + username + "'";

        Cursor cursor = db.rawQuery(selectQuery,
                null);
        DBCursor resultDBCursor = new DBCursor(cursor);

        try{
            resultDBCursor.moveToFirst();
            while (!resultDBCursor.isAfterLast()){
                resultList.add(resultDBCursor.getResult());
                resultDBCursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }
        return resultList;
    }

    //get all student's result from database
    public List<Result> getPracticalResults(String id){
        List <Result> resultList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBSchema.ResultTable.NAME + " WHERE "
                + DBSchema.ResultTable.Cols.PRAC_ID + " ='" + id + "'";

        Cursor cursor = db.rawQuery(selectQuery,
                null);
        DBCursor resultDBCursor = new DBCursor(cursor);

        try{
            resultDBCursor.moveToFirst();
            while (!resultDBCursor.isAfterLast()){
                resultList.add(resultDBCursor.getResult());
                resultDBCursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }
        return resultList;
    }

}

package com.example.prac_grader.sql_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prac_grader.classes.Account;
import com.example.prac_grader.classes.Student;

import java.util.ArrayList;
import java.util.List;

public class AccountDBModel {
    private List<Account> accounts = new ArrayList<>();
    private SQLiteDatabase db;
    StudentDBModel studentDBModel;

    public AccountDBModel(){}

    public void load (Context context){
        this.db = new DBHelper(context).getWritableDatabase();
        studentDBModel = new StudentDBModel();
        studentDBModel.load(context);
    }

    public int size(){
        return accounts.size();
    }

    public Account get(int i)
    {
        return accounts.get(i);
    }

    public void add (Account newAccount){
        accounts.add(newAccount);
        ContentValues cv = new ContentValues();
        cv.put (DBSchema.AccountTable.Cols.USERNAME, newAccount.getUsername());
        cv.put (DBSchema.AccountTable.Cols.PIN, newAccount.getPin());
        cv.put (DBSchema.AccountTable.Cols.NAME, newAccount.getName());
        cv.put (DBSchema.AccountTable.Cols.EMAIL, newAccount.getEmail());
        cv.put (DBSchema.AccountTable.Cols.COUNTRY, newAccount.getCountry());
        cv.put (DBSchema.AccountTable.Cols.TYPE, newAccount.getType());
        db.insert(DBSchema.AccountTable.NAME, null, cv);
        //return accounts.size() -1;
    }

    public void edit (Account newAccount){
        ContentValues cv = new ContentValues();
        cv.put (DBSchema.AccountTable.Cols.USERNAME, newAccount.getUsername());
        cv.put (DBSchema.AccountTable.Cols.PIN, newAccount.getPin());
        cv.put (DBSchema.AccountTable.Cols.NAME, newAccount.getName());
        cv.put (DBSchema.AccountTable.Cols.EMAIL, newAccount.getEmail());
        cv.put (DBSchema.AccountTable.Cols.COUNTRY, newAccount.getCountry());
        cv.put (DBSchema.AccountTable.Cols.TYPE, newAccount.getType());

        String[] whereValues = {String.valueOf(newAccount.getUsername())};
        db.update(DBSchema.AccountTable.NAME, cv,
                DBSchema.AccountTable.Cols.USERNAME + "=?", whereValues);

    }

    public void remove(Account rmAccount)
    {
        accounts.remove(rmAccount);
        // remove faction from database
        String[] whereValues = {String.valueOf(rmAccount.getUsername())};
        db.delete(DBSchema.AccountTable.NAME,
                DBSchema.AccountTable.Cols.USERNAME + "=?",
                whereValues);
    }

    //get all instructors from database
    public List<Account> getAllInstructor(){
        List <Account> accountList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBSchema.AccountTable.NAME + " WHERE "
                + DBSchema.AccountTable.Cols.TYPE + " = " + Account.INSTRUCTOR +
                  " ORDER BY " + DBSchema.AccountTable.Cols.NAME +
                " COLLATE NOCASE ASC";
        ;

        Cursor cursor = db.rawQuery(selectQuery,
                null);
        DBCursor accountDBCursor = new DBCursor(cursor);

        try{
            accountDBCursor.moveToFirst();
            while (!accountDBCursor.isAfterLast()){
                accountList.add(accountDBCursor.getAccount());
                accountDBCursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }
        return accountList;
    }

    // get all students from database
    public List<Account> getAllStudents(){
        List <Account> accountList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBSchema.AccountTable.NAME + " WHERE "
                + DBSchema.AccountTable.Cols.TYPE + " = " + Account.STUDENT +
                " ORDER BY " + DBSchema.AccountTable.Cols.NAME +
                " COLLATE NOCASE ASC";

        Cursor cursor = db.rawQuery(selectQuery,
                null);
        DBCursor accountDBCursor = new DBCursor(cursor);

        try{
            accountDBCursor.moveToFirst();
            while (!accountDBCursor.isAfterLast()){
                accountList.add(accountDBCursor.getAccount());
                accountDBCursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }
        return accountList;
    }

    //get all students added by the instructor
    public List<Account> getInstructorStudents(String username){
        List <Account> accountList = new ArrayList<>();
        List <Student>studentList = new ArrayList<>();

        studentList = studentDBModel.getInstrutorStudent(username) ;

        //check the username from all accounts
        String selectQuery = "SELECT  * FROM " + DBSchema.AccountTable.NAME + " WHERE "
                + DBSchema.AccountTable.Cols.TYPE + " = " + Account.STUDENT +
                " ORDER BY " + DBSchema.AccountTable.Cols.NAME +
                " COLLATE NOCASE ASC";

        Cursor cursor = db.rawQuery(selectQuery,
                null);
        DBCursor accountDBCursor = new DBCursor(cursor);

        try{
            accountDBCursor.moveToFirst();
            while (!accountDBCursor.isAfterLast()){
                //check the student username list with all accounts
                for (Student student : studentList) {
                    if ((accountDBCursor.getAccountUsername()).equals(student.getUsername()) ) {
                        accountList.add(accountDBCursor.getAccount());
                    }
                }
                accountDBCursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }

        return accountList;
    }

    //get all accounts from database
    public List<Account> getAllAccount(){
        List <Account> accountList = new ArrayList<>();
        Cursor cursor = db.query(DBSchema.AccountTable.NAME,
                null,null,null,
                null,null,null);
        DBCursor accountDBCursor = new DBCursor(cursor);

        try{
            accountDBCursor.moveToFirst();
            while (!accountDBCursor.isAfterLast()){
                accountList.add(accountDBCursor.getAccount());
                accountDBCursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }
        return accountList;
    }

}

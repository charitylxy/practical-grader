package com.example.prac_grader.sql_database;
import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.prac_grader.classes.Practical;
import com.example.prac_grader.classes.Result;
import com.example.prac_grader.sql_database.DBSchema.AccountTable;
import com.example.prac_grader.sql_database.DBSchema.StudentTable;
import com.example.prac_grader.sql_database.DBSchema.PracticalTable;
import com.example.prac_grader.sql_database.DBSchema.ResultTable;

import com.example.prac_grader.classes.Account;
import com.example.prac_grader.classes.Student;

public class DBCursor extends CursorWrapper {

    public DBCursor(Cursor cursor) {
        super(cursor);
    }

    public Account getAccount(){
        String username = getString(getColumnIndex(AccountTable.Cols.USERNAME));
        int pin = getInt(getColumnIndex(AccountTable.Cols.PIN));
        String name = getString(getColumnIndex(AccountTable.Cols.NAME));
        String email = getString(getColumnIndex(AccountTable.Cols.EMAIL));
        int country = getInt(getColumnIndex(AccountTable.Cols.COUNTRY));
        int type = getInt(getColumnIndex(AccountTable.Cols.TYPE));
        return new Account (username, pin, name, email, country, type);
    }

    public String getAccountUsername(){
        String username = getString(getColumnIndex(AccountTable.Cols.USERNAME));
        return username;
    }

    public Student getStudent(){
        String username = getString(getColumnIndex(StudentTable.Cols.USERNAME));
        String instructor = getString(getColumnIndex(StudentTable.Cols.INSTRUCTOR));
        return new Student(username,instructor);
    }

    public Practical getPractical(){
        String id = getString(getColumnIndex(PracticalTable.Cols.ID));
        String title = getString(getColumnIndex(PracticalTable.Cols.TITLE));
        String description = getString(getColumnIndex(PracticalTable.Cols.DESC));
        double maxMark = getDouble(getColumnIndex(PracticalTable.Cols.MAX_MARKS));
        return new Practical (id,title, description, maxMark);
    }

    public Result getResult(){
        String id = getString(getColumnIndex(ResultTable.Cols.ID));
        String practical = getString(getColumnIndex(ResultTable.Cols.PRAC_ID));
        String student = getString(getColumnIndex(ResultTable.Cols.STUDENT));
        double marks = getDouble(getColumnIndex(ResultTable.Cols.MARKS));

        return new Result (id, practical, student, marks);
    }
}

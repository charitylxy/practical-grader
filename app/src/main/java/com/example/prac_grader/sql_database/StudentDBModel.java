package com.example.prac_grader.sql_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prac_grader.classes.Practical;
import com.example.prac_grader.classes.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentDBModel {
    private List<Student> students = new ArrayList<>();
    private SQLiteDatabase db;

    public StudentDBModel(){}

    public void load (Context context){
        this.db = new DBHelper(context).getWritableDatabase();
    }

    public int size(){
        return students.size();
    }

    public Student get(int i)
    {
        return students.get(i);
    }

    public void add (Student newStudent){
        students.add(newStudent);
        ContentValues cv = new ContentValues();
        cv.put (DBSchema.StudentTable.Cols.USERNAME, newStudent.getUsername());
        cv.put (DBSchema.StudentTable.Cols.INSTRUCTOR, newStudent.getInstructor());
        db.insert(DBSchema.StudentTable.NAME, null, cv);
    }


    public void remove(Student rmStudent)
    {
        students.remove(rmStudent);
        // remove faction from database
        String[] whereValues = {String.valueOf(rmStudent.getUsername())};
        db.delete(DBSchema.StudentTable.NAME,
                DBSchema.StudentTable.Cols.USERNAME + "=?",
                whereValues);
    }

    //get all practicals from database
    public List<Student> getInstrutorStudent(String instructor) {
        List<Student> studentList = new ArrayList<>();

        //store student username that added by instructor
        String selectQuery1 = "SELECT  * FROM " + DBSchema.StudentTable.NAME + " WHERE "
                + DBSchema.StudentTable.Cols.INSTRUCTOR + " = '" + instructor + "'";

        Cursor cursor = db.rawQuery(selectQuery1,
                null);
        DBCursor studentDBCursor = new DBCursor(cursor);

        try {
            studentDBCursor.moveToFirst();
            while (!studentDBCursor.isAfterLast()) {
                studentList.add(studentDBCursor.getStudent());
                studentDBCursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return studentList;
    }
}

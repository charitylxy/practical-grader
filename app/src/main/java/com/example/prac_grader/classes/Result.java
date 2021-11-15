package com.example.prac_grader.classes;

import com.example.prac_grader.sql_database.ResultDBModel;

import java.util.UUID;

public class Result {
  //  private static int nextId = 0;

    private String id;
    private String practical_id;
    private String student_username;
    private double marks;

    public Result( String id, String practical_id, String student_username, double marks) {

        this.id = id;
        this.practical_id = practical_id;
        this.student_username = student_username;
        this.marks = marks;
        //nextId = id +1;
    }

    public Result(String practical_id, String student_username, double marks) {
        this (UUID.randomUUID().toString(), practical_id, student_username, marks);
    }

    public String getId() {
        return id;
    }

    public String getPractical_id() {
        return practical_id;
    }


    public String getStudent_username() {
        return student_username;
    }


    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

}

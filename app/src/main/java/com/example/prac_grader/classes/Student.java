package com.example.prac_grader.classes;

import java.util.List;

public class Student {
    private String username;
    private String instructor;

    public Student(String username, String instructor) {
        this.username = username;
        this.instructor = instructor;
    }

    public String getUsername() {
        return username;
    }

    public String getInstructor() {
        return instructor;
    }

}

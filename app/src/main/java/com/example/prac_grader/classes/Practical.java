package com.example.prac_grader.classes;

import java.io.Serializable;
import java.util.UUID;

public class Practical implements Serializable {

    private String id;
    private String title;
    private String description;
    private double marks;

    public Practical(String id, String title, String description, double marks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.marks = marks;
    }

    public Practical(String title, String description, double marks) {
       this (UUID.randomUUID().toString(), title, description, marks);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }
}

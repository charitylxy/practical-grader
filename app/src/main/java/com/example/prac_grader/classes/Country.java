package com.example.prac_grader.classes;

public class Country {
    private final int drawableId;
    private String name;

    public Country(int drawableId, String name) {
        this.drawableId = drawableId;
        this.name = name;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public String getName() {
        return name;
    }
}

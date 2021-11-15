package com.example.prac_grader.classes;

import java.io.Serializable;

public class Account implements Serializable {

    public static final int ADMIN = 0;
    public static final int INSTRUCTOR = 1;
    public static final int STUDENT = 2;

    private String username;
    private int pin;
    private String name;
    private String email;
    private int country;
    private int type;

    public Account(String username, int pin, String name, String email, int country, int type) {
        this.username = username;
        this.pin = pin;
        this.name = name;
        this.email = email;
        this.country = country;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public int getType() {
        return type;
    }

}

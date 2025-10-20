package com.myapp;

// This is our "JavaBean" or "Model"
public class Student {
    
    // 1. Private properties
    private int id;
    private String name;
    private String email;

    // 2. Public no-argument constructor
    public Student() {
    }

    // Constructor for easy creation
    public Student(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // 3. Public Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
}
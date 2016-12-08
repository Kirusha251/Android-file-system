package com.example.kirill.lab6db;


/**
 * Created by Kirill on 08.11.2016.
 */

public class Student {
    private int idGroup;
    private  int idStudent;
    private String name;

    public Student(int idGroup, String name) {
        this.idGroup = idGroup;
        this.name = name;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

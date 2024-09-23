package com.morg.cacheandroidsample;

public class Student {
    private String name;
    private String classStudent;

    public Student() {
    }

    public Student(String name, String classStudent) {
        this.name = name;
        this.classStudent = classStudent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassStudent() {
        return classStudent;
    }

    public void setClassStudent(String classStudent) {
        this.classStudent = classStudent;
    }
}

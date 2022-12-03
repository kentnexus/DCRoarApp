package com.paszlelab.dcroarapp.models;

public class CourseModel {
    // variables for our course
    // code and name
    private String courseCode;
    private String courseName;

    // creating constructor for our variables.
    public CourseModel(String courseCode, String courseName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    // creating getter and setter methods.
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
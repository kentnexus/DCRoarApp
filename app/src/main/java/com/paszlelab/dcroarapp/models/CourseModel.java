package com.paszlelab.dcroarapp.models;

public class CourseModel {
    // variables for our course
    // code and name
    private String courseDept;
    private String courseCode;
    private String courseName;

    // creating constructor for our variables.
    public CourseModel(String courseDept, String courseCode, String courseName) {
        this.courseDept = courseDept;
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    public CourseModel() {
    }

    // creating getter and setter methods.

    public String getCourseDept() {
        return courseDept;
    }

    public void setCourseDept(String courseDept) {
        this.courseDept = courseDept;
    }

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
package com.paszlelab.dcroarapp.models;

import java.io.Serializable;
import java.util.Comparator;

public class Student implements Serializable {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String birthday;
    private String gender;
    private String phoneNumber;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Student(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Student() {
    }

    public static Comparator<Student> firstNameComparator = new Comparator<Student>() {

        public int compare(Student s1, Student s2) {
            String StudentName1 = s1.firstName.toUpperCase();
            String StudentName2 = s2.firstName.toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);
        }};
}
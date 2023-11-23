package com.example.midtermapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    private String StudentId;
    private String StudentName;
    private String StudentEmail;

    public Student(String studentId, String studentName, String studentEmail) {
        StudentId = studentId;
        StudentName = studentName;
        StudentEmail = studentEmail;
    }

    public Student() {
    }

    protected Student(Parcel in) {
        StudentId = in.readString();
        StudentName = in.readString();
        StudentEmail = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(StudentId);
        dest.writeString(StudentName);
        dest.writeString(StudentEmail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getStudentEmail() {
        return StudentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        StudentEmail = studentEmail;
    }

    @Override
    public String toString() {
        return "Student{" +
                "StudentId='" + StudentId + '\'' +
                ", StudentName='" + StudentName + '\'' +
                ", StudentEmail='" + StudentEmail + '\'' +
                '}';
    }
}

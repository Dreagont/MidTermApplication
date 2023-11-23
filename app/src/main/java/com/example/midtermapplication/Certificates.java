package com.example.midtermapplication;


public class Certificates {
    private String Name;
    private String Body;
    private String StudentId;

    public Certificates(String name, String body, String studentId) {
        this.Name = name;
        this.Body = body;
        this.StudentId = studentId;
    }

    public Certificates() {}

    public Certificates(String name, String body) {
        this.Name = name;
        this.Body = body;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    @Override
    public String toString() {
        return "Certificates{" +
                ", Name='" + Name + '\'' +
                ", Body='" + Body + '\'' +
                ", StudentId='" + StudentId + '\'' +
                '}';
    }
}

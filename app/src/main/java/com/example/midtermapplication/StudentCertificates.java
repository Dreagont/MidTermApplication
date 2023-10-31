package com.example.midtermapplication;


public class StudentCertificates {
    private int ID;
    private String Name;
    private String Body;
    private String Mail;

    public StudentCertificates(int ID, String name, String body, String mail) {
        this.Name = name;
        this.Body = body;
        this.ID = ID;
        this.Mail = mail;
    }

    public StudentCertificates() {}

    public StudentCertificates(String name, String body, String mail) {
        this.Name = name;
        this.Body = body;
        this.Mail = mail;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "Certificates{" +
                "ID=" + ID +
                ", Name='" + Name + '\'' +
                ", Body=" + Body +
                '}';
    }
}

package com.example.midtermapplication;


public class Certificates {
    private int ID;
    private String Name;
    private String Body;

    public Certificates(String name, String body, int ID) {
        this.Name = name;
        this.Body = body;
        this.ID = ID;
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

package com.example.midtermapplication;


public class Certificates {
    private String Id;
    private String Name;
    private String Body;

    public Certificates(String name, String body, String ID) {
        this.Name = name;
        this.Body = body;
        this.Id = ID;
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

    public String getId() {
        return Id;
    }

    public void setID(String ID) {
        this.Id = ID;
    }

    @Override
    public String toString() {
        return "Certificates{" +
                "ID=" + Id +
                ", Name='" + Name + '\'' +
                ", Body=" + Body +
                '}';
    }
}

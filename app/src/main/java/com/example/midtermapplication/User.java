package com.example.midtermapplication;

public class User {
    private String Name;
    private String Phone;
    private String Mail;
    private String Password;
    private int Age;
    private String Role;
    private boolean Lock = false;

    public User(String name, String phone, String mail, String password, int age, String role) {
        Name = name;
        Phone = phone;
        Mail = mail;
        Password = password;
        Age = age;
        Role = role;
    }

    public User(String name, String phone, String mail, String password, int age, String role, boolean lock) {
        Name = name;
        Phone = phone;
        Mail = mail;
        Password = password;
        Age = age;
        Role = role;
        Lock = lock;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public User() {
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        this.Age = age;
    }

    public boolean isLock() {
        return Lock;
    }

    public void setLock(boolean lock) {
        Lock = lock;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    @Override
    public String toString() {
        return "Student{" +
                "Name='" + Name + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Mail='" + Mail + '\'' +
                ", Password='" + Password + '\'' +
                ", Age=" + Age +
                ", Role='" + Role + '\'' +
                ", Lock=" + Lock +
                '}';
    }
}
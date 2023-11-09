package com.example.midtermapplication;

import java.io.Serializable;

public class User implements Serializable {
    private String Name;
    private String Phone;
    private String Mail;
    private String Password;
    private int Age;
    private String Role;
    private boolean Lock = false;
    private String ImageUrl = "default.jpg";

    public User(String name, String phone, String mail, String password, int age, String role) {
        Name = name;
        Phone = phone;
        Mail = mail;
        Password = password;
        Age = age;
        Role = role;
    }

    public User(String name, String mail, String role, boolean lock) {
        Name = name;
        Mail = mail;
        Role = role;
        Lock = lock;
    }

    public User(String name, String phone, String mail, int age, String role, boolean lock) {
        Name = name;
        Phone = phone;
        Mail = mail;
        Lock = lock;
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

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
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
        return "User{" +
                "Name='" + Name + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Mail='" + Mail + '\'' +
                ", Password='" + Password + '\'' +
                ", Age=" + Age +
                ", Role='" + Role + '\'' +
                ", Lock=" + Lock +
                ", ImageUrl='" + ImageUrl + '\'' +
                '}';
    }
}
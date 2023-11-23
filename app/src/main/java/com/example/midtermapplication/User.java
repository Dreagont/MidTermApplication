package com.example.midtermapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
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

    protected User(Parcel in) {
        Name = in.readString();
        Phone = in.readString();
        Mail = in.readString();
        Password = in.readString();
        Age = in.readInt();
        Role = in.readString();
        Lock = in.readByte() != 0;
        ImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Phone);
        dest.writeString(Mail);
        dest.writeString(Password);
        dest.writeInt(Age);
        dest.writeString(Role);
        dest.writeByte((byte) (Lock ? 1 : 0));
        dest.writeString(ImageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
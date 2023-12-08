package com.example.midtermapplication;


import android.os.Parcel;
import android.os.Parcelable;

public class Certificates implements Parcelable {
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

    protected Certificates(Parcel in) {
        Name = in.readString();
        Body = in.readString();
        StudentId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Body);
        dest.writeString(StudentId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Certificates> CREATOR = new Creator<Certificates>() {
        @Override
        public Certificates createFromParcel(Parcel in) {
            return new Certificates(in);
        }

        @Override
        public Certificates[] newArray(int size) {
            return new Certificates[size];
        }
    };

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

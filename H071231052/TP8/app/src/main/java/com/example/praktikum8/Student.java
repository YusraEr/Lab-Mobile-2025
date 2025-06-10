package com.example.praktikum8;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Student implements Parcelable {
    private String name;
    private String nim;
    private String createdAt;
    private String updatedAt;
    private int id;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    protected Student(Parcel in) {
        name = in.readString();
        nim = in.readString();
        id = in.readInt();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public Student() {
        this.id = 0;
    }

    public Student(int id, String name, String nim, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.nim = nim;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(nim);
        parcel.writeInt(id);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}

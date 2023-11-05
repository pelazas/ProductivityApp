package com.example.productivityapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.productivityapp.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Todo implements Parcelable {

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    private String title;
    private LocalDate limitDate;
    private Priority priority;

    public Todo(String title, LocalDate limitDate, Priority priority) {
        this.title = title;
        this.limitDate = limitDate;
        this.priority = priority;
    }

    protected Todo(Parcel in) {
        this.title = in.readString();
        this.limitDate = LocalDate.parse(in.readString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.priority = Priority.valueOf(in.readString());
    }

    public String getTitle() {
        return this.title;
    }

    public LocalDate getLimitDate() {
        return this.limitDate;
    }

    public Priority getPriority() {
        return this.priority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(limitDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dest.writeString(priority.name());
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {

        @Override
        public Todo createFromParcel(Parcel parcel) {
            return new Todo(parcel);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };
}

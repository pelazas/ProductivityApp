package com.example.productivityapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.productivityapp.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity(tableName = "todos")
public class Todo implements Parcelable {

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    @PrimaryKey(autoGenerate = true) private int id;
    @ColumnInfo(name="title") private String title;
    @ColumnInfo(name="description") private String description;
    @ColumnInfo(name="limit_date") private LocalDate limitDate;
    @ColumnInfo(name="priority") private Priority priority;
    @ColumnInfo(name="finished") private boolean finished;

    @Ignore
    public Todo(String title, LocalDate limitDate, Priority priority) {
        this(title, "", limitDate, priority, false);
    }

    public Todo(String title, String description, LocalDate limitDate, Priority priority, boolean finished) {
        this.title = title;
        this.description = description;
        this.limitDate = limitDate;
        this.priority = priority;
        this.finished = finished;
    }

    protected Todo(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.limitDate = LocalDate.parse(in.readString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.priority = Priority.valueOf(in.readString());
        this.finished = Boolean.valueOf(in.readString());
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDate getLimitDate() {
        return this.limitDate;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLimitDate(LocalDate limitDate) {
        this.limitDate = limitDate;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(limitDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dest.writeString(priority.name());
        dest.writeString(String.valueOf(finished));
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

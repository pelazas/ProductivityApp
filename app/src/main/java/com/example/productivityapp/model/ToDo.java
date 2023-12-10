package com.example.productivityapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ToDo implements Parcelable {

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public enum State {
        TO_DO, CANCEL, FINISHED
    }

    private String id;
    private String userId;
    private String title;
    private String description;
    private LocalDate limitDate;
    private Priority priority;
    private State state;

    public ToDo(String id, String userId, String title, String description, LocalDate limitDate, Priority priority, State state) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.limitDate = limitDate;
        this.priority = priority;
        this.state = state;
    }

    public ToDo(String userId, String title, LocalDate limitDate, Priority priority) {
        this(null, userId, title, "", limitDate, priority, State.TO_DO);
    }

    public ToDo(String userId, String title, String description, LocalDate limitDate, Priority priority, State state) {
        this(null, userId, title, description, limitDate, priority, state);
    }

    protected ToDo(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.limitDate = LocalDate.parse(in.readString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.priority = Priority.valueOf(in.readString());
        this.state = State.valueOf(in.readString());
    }

    public String getId() {
        return this.id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setId(String id) {
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

    public State getState() {
        return this.state;
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
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(limitDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dest.writeString(priority.name());
        dest.writeString(state.toString());
    }

    public static final Creator<ToDo> CREATOR = new Creator<ToDo>() {

        @Override
        public ToDo createFromParcel(Parcel parcel) {
            return new ToDo(parcel);
        }

        @Override
        public ToDo[] newArray(int size) {
            return new ToDo[size];
        }
    };

    public String toString(){
        return this.title;
    }
}

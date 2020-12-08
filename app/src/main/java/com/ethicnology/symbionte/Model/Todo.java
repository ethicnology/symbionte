package com.ethicnology.symbionte.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Todo implements Parcelable {

    private String title;
    private String description;
    private String id;
    private String user_first;


    public Todo() {

    }

    public Todo(String id, String title, String description, String user_first) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.user_first = user_first;
    }

    protected Todo(Parcel in) {
        title = in.readString();
        description = in.readString();
        id = in.readString();
        user_first = in.readString();
    }

    public static final Parcelable.Creator<Todo> CREATOR = new Parcelable.Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel source) {
            return new Todo(source);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.id);
        dest.writeString(this.user_first);
    }

    public String getUser_first() {
        return user_first;
    }

    public void setUser_first(String user_first) {
        this.user_first = user_first;
    }
}

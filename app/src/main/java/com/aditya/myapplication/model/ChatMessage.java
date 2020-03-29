package com.aditya.myapplication.model;

import android.app.Person;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class ChatMessage implements Parcelable {

    private Bundle extras;
    private Person sender_person;
    private String sender;
    private Object uri;
    private String text;
    private Long time;
    private String type;

    public ChatMessage() {

    }

    protected ChatMessage(Parcel in) {
        extras = in.readBundle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            sender_person = in.readParcelable(Person.class.getClassLoader());
        }
        sender = in.readString();
        text = in.readString();
        if (in.readByte() == 0) {
            time = null;
        } else {
            time = in.readLong();
        }
        type = in.readString();
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    public Bundle getExtras() {
        return extras;
    }

    public void setExtras(Bundle extras) {
        this.extras = extras;
    }

    public Person getSender_person() {
        return sender_person;
    }

    public void setSender_person(Person sender_person) {
        this.sender_person = sender_person;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Object getUri() {
        return uri;
    }

    public void setUri(Object uri) {
        this.uri = uri;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStandardDate() {
        return new Date(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(extras);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            dest.writeParcelable(sender_person, flags);
        }
        dest.writeString(sender);
        dest.writeString(text);
        if (time == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(time);
        }
        dest.writeString(type);
    }
}

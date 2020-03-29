package com.aditya.myapplication.model;

import android.app.Person;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Chat implements Parcelable {

    private String title;
    private boolean images;
    private String subText;
    private String template;
    private String conversationTitle;
    private boolean showChronometer;
    private String icon;
    private String text;
    private int progress;
    private int progressMax;
    private String selfDisplayName;
    private ApplicationInfo appInfo;
    private List<ChatMessage> messages;
    private boolean showWhen;
    private Bitmap largeIcon;
    private Person messagingUser;
    private String infoText;
    private Bundle wearableEXTENSIONS;
    private boolean progressIndeterminate;
    private String remoteInputHistory;
    private boolean isGroupConversation;

    public Chat() {

    }

    protected Chat(Parcel in) {
        title = in.readString();
        images = in.readByte() != 0;
        subText = in.readString();
        template = in.readString();
        conversationTitle = in.readString();
        showChronometer = in.readByte() != 0;
        icon = in.readString();
        text = in.readString();
        progress = in.readInt();
        progressMax = in.readInt();
        selfDisplayName = in.readString();
        appInfo = in.readParcelable(ApplicationInfo.class.getClassLoader());
        messages = in.createTypedArrayList(ChatMessage.CREATOR);
        showWhen = in.readByte() != 0;
        largeIcon = in.readParcelable(Bitmap.class.getClassLoader());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            messagingUser = in.readParcelable(Person.class.getClassLoader());
        }
        infoText = in.readString();
        wearableEXTENSIONS = in.readBundle();
        progressIndeterminate = in.readByte() != 0;
        remoteInputHistory = in.readString();
        isGroupConversation = in.readByte() != 0;
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getImages() {
        return images;
    }

    public void setImages(boolean images) {
        this.images = images;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getConversationTitle() {
        return conversationTitle;
    }

    public void setConversationTitle(String conversationTitle) {
        this.conversationTitle = conversationTitle;
    }

    public Boolean getShowChronometer() {
        return showChronometer;
    }

    public void setShowChronometer(Boolean showChronometer) {
        this.showChronometer = showChronometer;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgressMax() {
        return progressMax;
    }

    public void setProgressMax(int progressMax) {
        this.progressMax = progressMax;
    }

    public String getSelfDisplayName() {
        return selfDisplayName;
    }

    public void setSelfDisplayName(String selfDisplayName) {
        this.selfDisplayName = selfDisplayName;
    }

    public ApplicationInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(ApplicationInfo appInfo) {
        this.appInfo = appInfo;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public boolean isShowWhen() {
        return showWhen;
    }

    public void setShowWhen(boolean showWhen) {
        this.showWhen = showWhen;
    }

    public Bitmap getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(Bitmap largeIcon) {
        this.largeIcon = largeIcon;
    }

    public Person getMessagingUser() {
        return messagingUser;
    }

    public void setMessagingUser(Person messagingUser) {
        this.messagingUser = messagingUser;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    public Bundle getWearableEXTENSIONS() {
        return wearableEXTENSIONS;
    }

    public void setWearableEXTENSIONS(Bundle wearableEXTENSIONS) {
        this.wearableEXTENSIONS = wearableEXTENSIONS;
    }

    public boolean getProgressIndeterminate() {
        return progressIndeterminate;
    }

    public void setProgressIndeterminate(boolean progressIndeterminate) {
        this.progressIndeterminate = progressIndeterminate;
    }

    public String getRemoteInputHistory() {
        return remoteInputHistory;
    }

    public void setRemoteInputHistory(String remoteInputHistory) {
        this.remoteInputHistory = remoteInputHistory;
    }

    public boolean isGroupConversation() {
        return isGroupConversation;
    }

    public void setGroupConversation(boolean groupConversation) {
        isGroupConversation = groupConversation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeByte((byte) (images ? 1 : 0));
        dest.writeString(subText);
        dest.writeString(template);
        dest.writeString(conversationTitle);
        dest.writeByte((byte) (showChronometer ? 1 : 0));
        dest.writeString(icon);
        dest.writeString(text);
        dest.writeInt(progress);
        dest.writeInt(progressMax);
        dest.writeString(selfDisplayName);
        dest.writeParcelable(appInfo, flags);
        dest.writeTypedList(messages);
        dest.writeByte((byte) (showWhen ? 1 : 0));
        dest.writeParcelable(largeIcon, flags);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            dest.writeParcelable(messagingUser, flags);
        }
        dest.writeString(infoText);
        dest.writeBundle(wearableEXTENSIONS);
        dest.writeByte((byte) (progressIndeterminate ? 1 : 0));
        dest.writeString(remoteInputHistory);
        dest.writeByte((byte) (isGroupConversation ? 1 : 0));
    }
}

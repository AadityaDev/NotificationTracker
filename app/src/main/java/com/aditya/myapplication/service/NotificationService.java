package com.aditya.myapplication.service;

import android.app.Notification;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.aditya.myapplication.model.Chat;
import com.aditya.myapplication.model.ChatMessage;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NotificationService extends NotificationListenerService {

    Context context;
    private List<Chat> chatList = new ArrayList<Chat>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    private void readWhatsAppNotifications(String pack, StatusBarNotification sbn,
                                           String title, String text, String ticker) {
        try {
            Chat chat = new Chat();
            Bundle extras = sbn.getNotification().extras;
            Set<String> notificationKeySet = extras.keySet();
            List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
            for(String key: notificationKeySet) {
                switch (key) {
                    case "android.title":
                        chat.setTitle(extras.getString(key));
                        break;
                    case "android.reduced.images":
                        chat.setImages(extras.getBoolean(key));
                        break;
                    case "android.subText":
                        chat.setSubText(extras.getString(key));
                        break;
                    case "android.template":
                        chat.setTemplate(extras.getString(key));
                        break;
                    case "android.showChronometer":
                        chat.setShowChronometer(extras.getBoolean(key));
                        break;
                    case "android.conversationTitle":
                        chat.setConversationTitle(extras.getString(key));
                        break;
                    case "android.icon":
                        chat.setIcon(extras.getString(key));
                        break;
                    case "android.text":
                        chat.setText(extras.getString(key));
                        break;
                    case "android.progress":
                        chat.setProgress(extras.getInt(key));
                        break;
                    case "android.progressMax":
                        chat.setProgressMax(extras.getInt(key));
                        break;
                    case "android.selfDisplayName":
                        chat.setSelfDisplayName(extras.getString(key));
                        break;
                    case "android.appInfo":
                        chat.setAppInfo((ApplicationInfo) extras.get(key));
                        break;
                    case "android.messages":
                        for (Object parcelable: Objects.requireNonNull(extras.getParcelableArray(key))) {
                            ChatMessage chatMessage = new ChatMessage();
                            Bundle bun = (Bundle) parcelable;
                            Set<String> dada = bun.keySet();
                            Bundle extras2 = bun.getBundle("extras");
                            chatMessage.setExtras(extras2);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                                Person sender_person = (Person) bun.get("sender_person");
                                chatMessage.setSender_person(sender_person);
                            }
                            String sender = bun.getString("sender");
                            chatMessage.setSender(sender);
                            Object uri = bun.get("uri");
                            chatMessage.setUri(uri);
                            String text2 = bun.getString("text");
                            chatMessage.setText(text2);
                            Long time = bun.getLong("time");
                            chatMessage.setTime(time);
                            String type = bun.getString("type");
                            chatMessage.setType(type);

                            chatMessageList.add(chatMessage);
                        }
                        chat.setMessages(chatMessageList);
                        break;
                    case "android.showWhen":
                        chat.setShowWhen(extras.getBoolean(key));
                        break;
                    case "android.largeIcon":
                        chat.setLargeIcon((Bitmap) extras.get(key));
                        break;
                    case "android.messagingUser":
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            chat.setMessagingUser((Person) extras.get(key));
                        }
                        break;
                    case "android.infoText":
                        chat.setInfoText(extras.getString(key));
                        break;
                    case "android.wearable.EXTENSIONS":
                        chat.setWearableEXTENSIONS(extras.getBundle(key));
                        break;
                    case "android.progressIndeterminate":
                        chat.setProgressIndeterminate(extras.getBoolean(key));
                        break;
                    case "android.remoteInputHistory":
                        chat.setRemoteInputHistory(extras.getString(key));
                        break;
                    case "android.isGroupConversation":
                        chat.setGroupConversation(extras.getBoolean(key));
                        break;
                    default:
                        break;
                }
            }
            chatList.add(chat);
            Intent msgrcv = new Intent("Msg");
//            msgrcv.putExtra("package", pack);
//            msgrcv.putExtra("ticker", ticker);
//            if (chat.isGroupConversation()) {
//                msgrcv.putExtra("title", chat.getConversationTitle());
//            } else {
//                msgrcv.putExtra("title", chat.getTitle());
//            }
//            msgrcv.putExtra("text", chat.getText());
            msgrcv.putExtra("chat", chat);
//            if(chat.getLargeIcon() != null) {
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                chat.getLargeIcon().compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//                msgrcv.putExtra("icon",byteArray);
//            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
//            Collections.reverse(chatList);
        } catch (Exception ex) {
            Log.d("NotService", ex.getMessage());
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();
        String title = null;
        String text = null;
        String ticker ="";
        if(sbn.getNotification().tickerText != null) {
            ticker = sbn.getNotification().tickerText.toString();
        }
        switch (pack) {
            case "com.whatsapp":
                readWhatsAppNotifications(pack, sbn, title, text, ticker);
                break;
            case "com.facebook.katana":
                break;
            default:
                break;
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");
    }

    public List<Chat> getChatList() {
        return  chatList;
    }

}
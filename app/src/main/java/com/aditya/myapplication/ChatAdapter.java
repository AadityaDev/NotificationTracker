package com.aditya.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.myapplication.model.Chat;

import java.util.ArrayList;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatImageViewHolder> {

    private static final String TAG = "ChatAdapter";
    private Context context;
    private ArrayList<Chat> chatArrayList;

    public ChatAdapter(Context Context, ArrayList<Chat> PhotoDataArrayList) {
        chatArrayList = PhotoDataArrayList;
        context = Context;
    }

    @NonNull
    @Override
    public ChatImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: New View requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ChatImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatImageViewHolder holder, int position) {
        Chat chat = chatArrayList.get(position);
        holder.title.setText(chat.getTitle());
        if(chat.getLargeIcon() != null)
            holder.thumbnail.setImageBitmap(chat.getLargeIcon());
        holder.lastMessage.setText(chat.getText());
//        holder.lastMessagesCount.setText(Objects.requireNonNull(chat) && Objects.requireNonNull(chat.getMessages());
        holder.lastMessagesCount.setText(chat.getMessages() != null  ? chat.getMessages().size()+ " " : " ");
    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    void loadNewData(ArrayList<Chat> newphotoData){
        Log.d(TAG, "loadNewData: new data requested");
        chatArrayList = newphotoData;
        notifyDataSetChanged();
    }

    static class  ChatImageViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "ChatImageViewHolder";
        private ImageView thumbnail = null;
        private TextView title = null;
        private TextView lastMessage = null;
        private TextView lastMessagesCount = null;

        public ChatImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: start");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.userAvatar);
            this.title = (TextView) itemView.findViewById(R.id.fromTitle);
            this.lastMessage = (TextView) itemView.findViewById(R.id.lastMessage);
            this.lastMessagesCount = (TextView) itemView.findViewById(R.id.lastMessagesCount);
        }
    }

}

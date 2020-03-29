package com.aditya.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aditya.myapplication.model.Chat;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Chat> modelList;

    public CustomListAdapter(Context context, ArrayList<Chat> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.list_item, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.fromTitle);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.userAvatar);
        TextView lastMessage = (TextView) rowView.findViewById(R.id.lastMessage);

        Chat m = modelList.get(position);
        txtTitle.setText(m.getTitle());
        if(m.getLargeIcon() != null)
            imageView.setImageBitmap(m.getLargeIcon());
        lastMessage.setText(m.getText());
        return rowView;

    };
}

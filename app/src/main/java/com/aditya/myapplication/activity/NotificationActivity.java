package com.aditya.myapplication.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aditya.myapplication.ChatAdapter;
import com.aditya.myapplication.R;
import com.aditya.myapplication.model.Chat;
import com.aditya.myapplication.service.FloatingViewService;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity implements RecyclerItemClickListener.OnRecyclerClickListener {

    private RecyclerView recyclerView;
    private ChatAdapter recycleViewAdapter;
    private ArrayList<Chat> chatArrayList;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    NotificationManager notificationManager;
    Notification.Builder builder;
    NotificationChannel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        chatArrayList = new ArrayList<Chat>();
        recycleViewAdapter = new ChatAdapter(getApplicationContext(), chatArrayList);
        recyclerView =(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(this,recyclerView,this));

        recyclerView.setAdapter(recycleViewAdapter);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {


            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
//              initializeView();
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            CharSequence name = "My Channel";
            String description = "xyz";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                channel.setAllowBubbles(true);
            }
        }
    }

    private void initializeView() {
//        findViewById(R.id.notify_me).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
                startService(new Intent(NotificationActivity.this, FloatingViewService.class));
//                finish();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(
                        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
                return true;
            case  R.id.chat_bubble_settings:
                Intent target = new Intent(NotificationActivity.this, BubbleActivity.class);
                PendingIntent bubbleIntent =
                        PendingIntent.getActivity(NotificationActivity.this, 0, target, PendingIntent.FLAG_UPDATE_CURRENT /* flags */);

                Notification.BubbleMetadata bubbleData =
                        null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    bubbleData = new Notification.BubbleMetadata.Builder()
                            .setDesiredHeight(600)
                            .setIcon(Icon.createWithResource(NotificationActivity.this, R.mipmap.ic_launcher))
                            .setIntent(bubbleIntent)
                            .build();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    builder = new Notification.Builder(NotificationActivity.this, channel.getId())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setBubbleMetadata(bubbleData);
                }
                notificationManager.createNotificationChannel(channel);
                notificationManager.notify(1, builder.build());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Chat chat = intent.getParcelableExtra("chat");
            Context remotePackageContext = null;
            try {
//                byte[] byteArray =intent.getByteArrayExtra("icon");
//                Bitmap bmp = null;
//                if(byteArray !=null) {
//                    bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//                }
                if(chatArrayList != null) {
                    chatArrayList.add(chat);
//                    Collections.reverse(chatArrayList);
                    recycleViewAdapter.notifyDataSetChanged();
                }else {
                    chatArrayList = new ArrayList<Chat>();
                    chatArrayList.add(chat);
//                    Collections.reverse(chatArrayList);
                    recycleViewAdapter = new ChatAdapter(getApplicationContext(), chatArrayList);
                    recyclerView =(RecyclerView) findViewById(R.id.recyclerView);
                    recyclerView.setAdapter(recycleViewAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void OnItemClickListener(View view, int position) {

    }

    @Override
    public void OnItemLongClickListener(View view, int position) {
        Toast.makeText(NotificationActivity.this,"Long Tap "+position+" position",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,NotificationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

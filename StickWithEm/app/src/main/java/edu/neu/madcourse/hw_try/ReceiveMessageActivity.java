package edu.neu.madcourse.hw_try;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import retrofit2.http.Tag;
import retrofit2.http.Url;

public class ReceiveMessageActivity extends AppCompatActivity{
    private ArrayList<ChatMessage> itemList;

    private RecyclerView recyclerView;
    private ChatAdapter cAdapter;
    private  RecyclerView.LayoutManager rLayoutManger;
    CountDownTimer countDownTimer;
    final String TAG = "Receive";

    //database
    private DatabaseReference mDatabase;
    private ChildEventListener postListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_message);
        //addItem(0);
        itemList =  new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.e(TAG, "create database" + " "+ MainActivity.currentUser);
        createRecyclerView();
        //addItem(new ChatMessage("hhh", "hhhhh"));
//        countDownTimer = new CountDownTimer(4000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//            }
//
//            public void onFinish() {
//                finish(); // finish ActivityB
//            }
//
//
//        }.start();

        //Specify what action a specific gesture performs, in this case swiping right or left deletes the entry
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();
                itemList.remove(position);
                cAdapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void addItem(String sender, String url, String message) {
        ChatMessage chatMessage = new ChatMessage(sender, message, url);
        Log.e(TAG, "url " + url);
        itemList.add(0, chatMessage);
        cAdapter.notifyDataSetChanged();
    }


    public void createRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        rLayoutManger = new LinearLayoutManager(this);
        cAdapter = new ChatAdapter(itemList);

        recyclerView.setAdapter(cAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    public void refresh(View view){
        //refresh is onClick name given to the button
        onRestart();
    }



// D

    @Override
    protected void onStart() {
        super.onStart();
        postListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NotificationData notificationData = snapshot.getValue(NotificationData.class);
                addItem(notificationData.getSender(), notificationData.getUml(), notificationData.getMessage());
                //addItem(uml, sender);
//                String uml=snapshot.child("Content").getValue().toString();
                //String uml = snapshot.child("uml").getValue().toString();
//                addItem(uml, sender);
                Log.e("receive", "Uml: " +  notificationData.getUml());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());

            }
        };
        mDatabase.child("users").child(MainActivity.currentUser).child("ReceivedNotifications").addChildEventListener(postListener);

    }

    @Override
    public void onStop() {

        super.onStop();

        //Remove post value event listener
        if (postListener != null){
            mDatabase.removeEventListener(postListener);
        }
    }


}
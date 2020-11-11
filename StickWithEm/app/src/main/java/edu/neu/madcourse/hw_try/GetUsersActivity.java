package edu.neu.madcourse.hw_try;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class GetUsersActivity extends AppCompatActivity {

    private static final String TAG = "GetUsersActivity";

    // RecyclerView
    private List<ItemCard> itemList1;
    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    private RecyclerView.LayoutManager rLayoutManager;


    // Database
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private ChildEventListener postListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_users);

        itemList1 = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        createRecyclerView();

    }

    public void startReceiveMessageActivity(View view){
        startActivity(new Intent(GetUsersActivity.this, ReceiveMessageActivity.class));
    }
    @Override
    protected void onStart() {
        super.onStart();
        postListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User post = snapshot.getValue(User.class);
                addItem(post.username, post.deviceToken);
                Log.e("a", "haha" + post.toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User post = snapshot.getValue(User.class);
                removeItem(post.username);
                Log.e("a", "haha" + post.toString());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());

            }
        };
        mDatabase.child("users").addChildEventListener(postListener);

    }

    @Override
    public void onStop() {

        super.onStop();

        //Remove post value event listener
        if (postListener != null){
            mDatabase.removeEventListener(postListener);
        }
    }


    public void addItem(String userName, String userToken){
        itemList1.add(new ItemCard(userName, userToken));
        rvAdapter.notifyDataSetChanged();
    }

    public void removeItem(final String userName){
        itemList1 = itemList1.stream().filter(new Predicate<ItemCard>() {
            @Override
            public boolean test(ItemCard itemCard) {
                return !itemCard.getUserName().equals(userName);
            }
        }).collect(Collectors.<ItemCard>toList());
        rvAdapter.notifyDataSetChanged();
    }



    public void createRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAdapter = new RVAdapter(this, itemList1);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }


    /**
     * pop up information when click
     * show up for long length time
     * @param message
     */
    private void toastMessage(String message){
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
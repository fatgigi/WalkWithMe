package edu.neu.madcourse.hw_try;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SendMessagesActivity extends AppCompatActivity {

    private static final String TAG = "SendMessagesActivity";
    private static String UserToken;
    private static final String SERVER_KEY = "key=AAAAnrIRS8Q:APA91bFsbNZGKYZ53NX3k6gP4xagT_mcEHAVgD6r0durYLPvZgf2vdyK9mswIXvjlt1PaV42dE-5X3Wy437QG-n0AQkfTpmvMOHDyOzbD8Bho_haI9PFKVo4Uy8X3KrMsHrRjfGPNZEd";


    private DatabaseReference mDatabase;

    private static final String uml = "https://images.megapixl.com/6713/67132401.jpg";
    private static final String uml2 = "https://rlv.zcache.com/good_job_star_sticker-rbf6abdb374894fb6b5f5cf1ba59a30a5_0ugdr_8byvr_704.jpg";

    private RadioButton radioButton;
    private RadioButton rbGoodJob;
    private EditText etMessage;
    private String message;
    private int countNotification;
    private String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        // TextView userName = (TextView) findViewById(R.id.get_userName);
        // TextView userToken = (TextView) findViewById(R.id.get_userToken);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        TextView tvCurrentUser = (TextView) findViewById(R.id.tvCurrentUser);
        String welcome = "Welcome " + MainActivity.currentUser + "!";
        tvCurrentUser.setText(welcome);

        radioButton = (RadioButton) findViewById(R.id.rbCheck);
        rbGoodJob = (RadioButton) findViewById(R.id.rbGood);
        etMessage = (EditText) findViewById(R.id.etMessage);

        Intent intent = getIntent();
        UserName = intent.getExtras().getString("userName");
        this.UserToken = intent.getExtras().getString("userToken");





//        userName.setText(UserName);
//        userToken.setText(UserToken);
//        userToken.setVisibility(View.INVISIBLE);

        Button send = (Button) findViewById(R.id.btnSend);

    }



    public void sendMessageToDevice(View type) {

        if (!radioButton.isChecked() && !rbGoodJob.isChecked()) {
            Log.d(TAG, "hellp");
            Toast toast = Toast.makeText(getApplicationContext(), "Please select a sticker.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        message = etMessage.getText().toString();
        if (message.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please leave a message.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
//        final String targetToken = ((EditText)findViewById(R.id.tokenEditText)).getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessage();
            }
        }).start();
    }

    /**
     * Pushes a notification to a given device-- in particular, this device,
     * because that's what the instanceID token is defined to be.
     */
    private void sendMessage() {
        // get token to send
        String targetToken = this.UserToken;


        message = etMessage.getText().toString();

        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();

        String userChooseSticker = findUserChooseSticker();

        try {
            jNotification.put("title", message);
            jNotification.put("body", "");
            jNotification.put("sound", "default");
            jNotification.put("image", userChooseSticker);
            // jNotification.put("image", "https://images.megapixl.com/6713/67132401.jpg");
            jNotification.put("badge", "1");
            /*
            // We can add more details into the notification if we want.
            // We happen to be ignoring them for this demo.
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            */
            jData.put("title",message);
            jData.put("content",R.drawable.korgi);
            jData.put("image", userChooseSticker);

            /***
             * The Notification object is now populated.
             * Next, build the Payload that we send to the server.
             */

            // If sending to a single client
            jPayload.put("to", targetToken); // CLIENT_REGISTRATION_TOKEN);

            /*
            // If sending to multiple clients (must be more than 1 and less than 1000)
            JSONArray ja = new JSONArray();
            ja.put(CLIENT_REGISTRATION_TOKEN);
            // Add Other client tokens
            ja.put(FirebaseInstanceId.getInstance().getToken());
            jPayload.put("registration_ids", ja);
            */

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data",jData);

            NotificationData curNotification = new NotificationData(message, userChooseSticker, MainActivity.currentUser);

            writeNotificationToFB(curNotification);

            /***
             * The Payload object is now populated.
             * Send it to Firebase to send the message to the appropriate recipient.
             */
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();


            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);



            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + resp);
                    // Toast.makeText(SendMessagesActivity.this,resp,Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String findUserChooseSticker() {
        if (radioButton.isChecked()) {
            return uml;
        } else {
            return uml2;
        }
    }


    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    private void writeNotificationToFB(NotificationData notificationData) {
//        mDatabase.child("users").child(UserName).child("ReceivedNotifications").child("Notification" + countNotification).child("Content").setValue(notificationData.getMessage());
//        mDatabase.child("users").child(UserName).child("ReceivedNotifications").child("Notification" + countNotification).child("StickerUml").setValue(notificationData.getUml());
//        mDatabase.child("users").child(UserName).child("ReceivedNotifications").child("Notification" + countNotification).child("Sender").setValue(notificationData.getSender());
        mDatabase.child("users").child(UserName).child("ReceivedNotifications").child("Notification" + countNotification).setValue(notificationData);
        countNotification++;
    }

}

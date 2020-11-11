package edu.neu.madcourse.hw_try;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyMessageService extends FirebaseMessagingService {
    private static final String TAG = MyMessageService.class.getSimpleName();
    private static final String CHANNEL_ID  = "channel";
    private static final String CHANNEL_NAME  = "channel";
    private static final String CHANNEL_DESCRIPTION  = "description";

    // private static final String uml = "https://images.megapixl.com/6713/67132401.jpg";
    private Bitmap bitmap;

    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);
    }

//    private void updateToken() {
//
//    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, remoteMessage.getMessageId());

        new Thread(new Runnable() {
            @Override
            public void run() {
                showNotification(remoteMessage);
                extractPayloadDataForegroundCase(remoteMessage);
            }
        }).start();

    }
    private void showNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification;
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        }
        else {
            builder = new NotificationCompat.Builder(this);
        }

        String stickerUml = remoteMessage.getData().get("image");

        bitmap = getBitmapFromUrl(stickerUml);

        notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.ic_action_name)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(0,notification);
    }

    private void  extractPayloadDataForegroundCase(RemoteMessage remoteMessage){

        if(remoteMessage.getData() != null){
            postToastMessage(remoteMessage.getData().get("title"));
            //sendToInbox(chatMessage);
        }
    }
    private void sendToInbox(final ChatMessage chatMessage){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //ReceiveMessageActivity.itemList.add(chatMessage);
                //ReceiveMessageActivity.cAdapter.notifyDataSetChanged();
                //ReceiveMessageActivity.restartActivity(getActivity());
                Log.e(TAG,  "size of item");
            }
        }).start();

    }

    public void postToastMessage(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }


    private Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}

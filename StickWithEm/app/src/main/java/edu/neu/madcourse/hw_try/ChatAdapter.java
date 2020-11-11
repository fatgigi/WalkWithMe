package edu.neu.madcourse.hw_try;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.RviewHolder> {
    private ArrayList<ChatMessage> itemList;
    Handler imageHandler = new Handler(Looper.getMainLooper());

    public static class RviewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView message;
        public ImageView imageView;

        public RviewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            message = itemView.findViewById(R.id.message);
            imageView = itemView.findViewById(R.id.item_icon);
        }
    }

    public ChatAdapter(ArrayList<ChatMessage> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chat_message, parent, false);
        return new RviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RviewHolder holder, int position) {
        ChatMessage currentItem = itemList.get(position);
        holder.userName.setText(currentItem.getUserName());
        holder.message.setText(currentItem.getMessage());
//        holder.userName.setText("text");
//        holder.message.setText("text")
        //Bitmap bitmap = getBitmapFromUrl("https://images.megapixl.com/6713/67132401.jpg");
        //bitmap.createScaledBitmap(bitmap,  60 ,60, true);
        //Dholder.imageView.setImageResource(R.drawable.korgi);
        //holder.imageView.setImageResource(currentItem.getImageSrc());
        //Log.e("imageResource", );
        //Picasso.get().load(currentItem.getImageSrc()).resize(50, 50).into(holder.imageView);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                imageHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.get().load(currentItem.getImageSrc()).resize(50, 50).into(holder.imageView);
                    }
                });
            }
        });
        thread.start();

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

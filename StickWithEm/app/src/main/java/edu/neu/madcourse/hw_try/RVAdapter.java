package edu.neu.madcourse.hw_try;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapter extends RecyclerView. Adapter<RVAdapter.viewHolder>{
    private Context mContext;
    private List<ItemCard> itemList;

    // constructor
    public RVAdapter(Context mContext, List<ItemCard> itemList) {

        this.mContext = mContext;
        this.itemList = itemList;
    }


    // create viewHolder class
    public static class viewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView userName;
        TextView userToken;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
            userName = itemView.findViewById(R.id.userName);
            userToken = itemView.findViewById(R.id.userToken);

        }
    }


    @NonNull
    @Override
    public RVAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.viewHolder holder, final int position) {
        ItemCard currentItem = itemList.get(position);
        holder.userName.setText(currentItem.getUserName());
        holder.userToken.setText(currentItem.getUserToken());
        holder.cardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SendMessagesActivity.class);
                intent.putExtra("userName", itemList.get(position).getUserName());
                intent.putExtra("userToken", itemList.get(position).getUserToken());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}


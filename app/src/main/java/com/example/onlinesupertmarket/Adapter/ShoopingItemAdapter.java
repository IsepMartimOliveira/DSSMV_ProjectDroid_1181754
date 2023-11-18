package com.example.onlinesupertmarket.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlinesupertmarket.Model.CartItem;
import com.example.onlinesupertmarket.Utils.Utils;
import com.example.onlinesupertmarket.R;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShoopingItemAdapter extends RecyclerView.Adapter<ShoopingItemAdapter.MyViewHolder>{
    List<CartItem> cartItems;
    OnDeleteMarkClickListener onDeleteMarkClickListener;


    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view_shoppingcart, parent, false);
        return new MyViewHolder(itemView);
    }

    public interface OnDeleteMarkClickListener {
        void onDeleteMarkClick(String id);

    }
    public List<CartItem> getData() {
        return cartItems;
    }


    public ShoopingItemAdapter(List<CartItem> cartItems,OnDeleteMarkClickListener onDeleteMarkClickListener) {
        this.cartItems = cartItems;
        this.onDeleteMarkClickListener=onDeleteMarkClickListener;

    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        CartItem currentItem = cartItems.get(position);
        String name = Utils.capitalizeFirstLetter(currentItem.getName());
        holder.name.setText("Name: " + name);
        holder.cost.setText("Price: " + currentItem.getCost().toString()+" €");
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteMarkClickListener != null) {
                    onDeleteMarkClickListener.onDeleteMarkClick(currentItem.getId());
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public  TextView cost;
        public ImageView deleteButton;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameCart);
            cost=itemView.findViewById(R.id.cost);
            deleteButton=itemView.findViewById(R.id.deleteButton);

        }
    }
    public void updateData(List<CartItem> newItems) {
        cartItems.clear();
        cartItems.addAll(newItems);
        notifyDataSetChanged();
    }
    public void clear() {
        int size = cartItems.size();
        cartItems.clear();
        notifyItemRangeRemoved(0, size);
    }

}

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
import com.example.onlinesupertmarket.Model.RecipeItem;
import com.example.onlinesupertmarket.R;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShoopingItemAdapter extends RecyclerView.Adapter<ShoopingItemAdapter.MyViewHolder>{
    List<CartItem> cartItems;


    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view_shoppingcart, parent, false);
        return new MyViewHolder(itemView);
    }
    public ShoopingItemAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;

    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        CartItem currentItem = cartItems.get(position);
        holder.name.setText(currentItem.getName());
        holder.cost.setText(currentItem.getCost().toString());



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
}

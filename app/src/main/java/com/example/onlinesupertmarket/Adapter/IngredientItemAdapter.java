package com.example.onlinesupertmarket.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlinesupertmarket.Model.ExtendedIngridients;
import com.example.onlinesupertmarket.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IngredientItemAdapter extends RecyclerView.Adapter<IngredientItemAdapter.MyViewHolder>  {
    private List<ExtendedIngridients> ingredientList;
    public IngredientItemAdapter(List<ExtendedIngridients> ingredientList){
        this.ingredientList=ingredientList;

    }
    @Override
    public IngredientItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view_ingridients, parent, false);
        return new IngredientItemAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(IngredientItemAdapter.MyViewHolder holder, int position) {
        ExtendedIngridients currentItem = ingredientList.get(position);


        holder.nameTextView.setText(currentItem.getName());

        Picasso.get()
                .load("https://spoonacular.com/cdn/ingredients_100x100/"+currentItem.getImage())
                .into(holder.recipeImageView);

    }
    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;
        public TextView healthScoreTextView;
        public ImageView recipeImageView;



        public MyViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            priceTextView = itemView.findViewById(R.id.price);
            healthScoreTextView = itemView.findViewById(R.id.healthScore);
            recipeImageView = itemView.findViewById(R.id.recipeImage);


        }
    }
    public void updateData(List<ExtendedIngridients> newItems) {
        ingredientList.clear();
        ingredientList.addAll(newItems);
        notifyDataSetChanged();
    }
}

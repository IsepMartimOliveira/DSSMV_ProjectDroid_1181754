package com.example.onlinesupertmarket.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlinesupertmarket.Model.Ingredients;
import com.example.onlinesupertmarket.Network.Utils;
import com.example.onlinesupertmarket.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IngredientItemAdapter extends RecyclerView.Adapter<IngredientItemAdapter.MyViewHolder>  {
    private List<Ingredients> ingredientList;
    OnAddMarkClickListener onAddMarkClick;

    public IngredientItemAdapter(List<Ingredients> ingredientList,OnAddMarkClickListener onAddMarkClick){
        this.ingredientList=ingredientList;
        this.onAddMarkClick=onAddMarkClick;

    }
    public interface OnAddMarkClickListener {
        void onAddMarkClick(String name);

    }
    @Override
    public IngredientItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view_ingridients, parent, false);
        return new IngredientItemAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(IngredientItemAdapter.MyViewHolder holder, int position) {
        Ingredients currentItem = ingredientList.get(position);

        String name = Utils.capitalizeFirstLetter(currentItem.getName());

        holder.nameTextView.setText(name);

        Picasso.get()
                .load("https://spoonacular.com/cdn/ingredients_100x100/"+currentItem.getImage())
                .into(holder.recipeImageView);

        holder.addtoBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAddMarkClick != null) {
                    onAddMarkClick.onAddMarkClick(currentItem.getName());
                }
            }
        });


    }
    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;

        public ImageView recipeImageView;

        public ImageView addtoBasket;



        public MyViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            recipeImageView = itemView.findViewById(R.id.recipeImage);
            addtoBasket=itemView.findViewById(R.id.addToBasket);


        }
    }
    public void updateData(List<Ingredients> newItems) {
        ingredientList.clear();
        ingredientList.addAll(newItems);
        notifyDataSetChanged();
    }

    public List<Ingredients> getIngredientList() {
        return ingredientList;
    }

}

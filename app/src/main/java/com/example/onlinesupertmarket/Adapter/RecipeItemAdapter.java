package com.example.onlinesupertmarket.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlinesupertmarket.Model.RecipeItem;
import com.example.onlinesupertmarket.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeItemAdapter extends RecyclerView.Adapter<RecipeItemAdapter.MyViewHolder> {
    private List<RecipeItem> itemList;

    private OnQuestionMarkClickListener questionMarkClickListener;

    private OnAddToBasketClickListener addToBasketClickListener;

    public RecipeItemAdapter(List<RecipeItem> itemList, OnQuestionMarkClickListener questionMarkClickListener,OnAddToBasketClickListener addToBasketClickListener) {
        this.itemList = itemList;
        this.questionMarkClickListener = questionMarkClickListener;
        this.addToBasketClickListener=addToBasketClickListener;
    }
    public interface OnQuestionMarkClickListener {
        void onQuestionMarkClick(Integer id,String title);

    }

    public interface OnAddToBasketClickListener {
        void onAddToBasketClick(RecipeItem item);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;

        public ImageView recipeImageView;

        public  ImageView questionMark;

        public ImageView addToBasket;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);;
            recipeImageView = itemView.findViewById(R.id.recipeImage);
            questionMark=itemView.findViewById(R.id.questionMark);
            addToBasket=itemView.findViewById(R.id.addToBasket);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecipeItem currentItem = itemList.get(position);
        holder.nameTextView.setText(currentItem.getTitle());

        Picasso.get()
                .load(currentItem.getImage())
                .into(holder.recipeImageView);
        holder.questionMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionMarkClickListener != null) {
                    questionMarkClickListener.onQuestionMarkClick(currentItem.getId(),currentItem.getTitle());
                }
            }
        });
        holder.addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addToBasketClickListener != null) {
                addToBasketClickListener.onAddToBasketClick(currentItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public void updateData(List<RecipeItem> newItems) {
        itemList.clear();
        itemList.addAll(newItems);
        notifyDataSetChanged();
    }
}

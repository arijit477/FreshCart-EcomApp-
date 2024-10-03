package com.example.freshcart.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freshcart.R;
import com.example.freshcart.activities.Adding_Product;
import com.example.freshcart.databinding.ItemCategoriesBinding;
import com.example.freshcart.model.Category;

import java.util.ArrayList;

public class SellerCategoryAdapter extends RecyclerView.Adapter<SellerCategoryAdapter.SellerCategoryViewHolder> {
    Context context;
    ArrayList<Category> Sellercategories;

    public SellerCategoryAdapter(Context context, ArrayList<Category> Sellercategories) {
        this.context = context;
        this.Sellercategories = Sellercategories;
    }

    @NonNull
    @Override
    public SellerCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SellerCategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SellerCategoryAdapter.SellerCategoryViewHolder holder, int position) {

        Category sellercategory = Sellercategories.get(position);
        holder.binding.lable.setText(Html.fromHtml(sellercategory.getName()));
        Glide.with(context)
                .load(sellercategory.getIcon())
                .into(holder.binding.image);
        holder.binding.image.setBackgroundColor(Color.parseColor(sellercategory.getColor()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , Adding_Product.class);
                intent.putExtra("name" , sellercategory.getName() );
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Sellercategories.size();
    }

    public class SellerCategoryViewHolder extends RecyclerView.ViewHolder{
        ItemCategoriesBinding binding;
        public SellerCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoriesBinding.bind(itemView);


        }
    }

}

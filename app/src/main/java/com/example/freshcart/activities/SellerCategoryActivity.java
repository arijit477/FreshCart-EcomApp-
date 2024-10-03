package com.example.freshcart.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.freshcart.R;
import com.example.freshcart.adapters.CategoryAdapter;
import com.example.freshcart.adapters.SellerCategoryAdapter;
import com.example.freshcart.databinding.ActivitySellerCategoryBinding;
import com.example.freshcart.model.Category;

import java.util.ArrayList;

public class SellerCategoryActivity extends AppCompatActivity {

   ActivitySellerCategoryBinding binding;
    SellerCategoryAdapter sellercategoryAdapter;
    ArrayList<Category> categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_category);
           binding = ActivitySellerCategoryBinding.inflate(getLayoutInflater());
                           setContentView(binding.getRoot());

            initCategories();
    }

    void initCategories() {
        categories = new ArrayList<>();
        sellercategoryAdapter = new SellerCategoryAdapter(this, categories);
        categories.add(new Category("Vegitables","https://kidlingoo.com/wp-content/uploads/vegetables_name_in_english_50.jpg","#ffffff","vegies",1));
        categories.add(new Category("Fruits","https://blog.uvihealth.in/content/images/2022/06/Website-Blog-Cover-Images--5-.jpg","#ffffff","fruits",1));
        categories.add(new Category("Spices","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRoev5xoeCAwwuDypw_8_fH8TZWerENmKcK1A&s","#ffffff","vegies",1));
        categories.add(new Category("Dry Fruits","https://eatanytime.in/cdn/shop/articles/close-up-healthy-nuts-wooden-background-generative-ai.jpg?v=1702453505&width=1100","#ffffff","vegies",1));


//            getCategories();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        binding.sellerCategoriesList.setLayoutManager(layoutManager);
        binding.sellerCategoriesList.setAdapter(sellercategoryAdapter);
    }
}
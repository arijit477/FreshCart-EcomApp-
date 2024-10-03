package com.example.freshcart.activities;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.freshcart.R;
import com.example.freshcart.adapters.CartAdapter;
import com.example.freshcart.databinding.ActivityCartBinding;
import com.example.freshcart.model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;


    CartAdapter adapter;
    ArrayList<Product> products;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivityCartBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.cart);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;

            } else if (itemId == R.id.cart) {
                return true;
            } else if (itemId == R.id.account) {
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                return true;
            }

            return false;
        });

        products = new ArrayList<>();
       Cart cart = TinyCartHelper.getCart();

        for (Map.Entry<Item,Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int qty = item.getValue();
            product.setQuantity(qty);
            products.add(product);

        }
       adapter = new CartAdapter(this,products);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,layoutManager.getOrientation());

       binding.cartList.setLayoutManager(layoutManager);
       binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);
        String image = getIntent().getStringExtra("image");
        String name = getIntent().getStringExtra("name");
        double price = getIntent().getDoubleExtra("price",0);





       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
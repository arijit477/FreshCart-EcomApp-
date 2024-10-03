package com.example.freshcart.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.freshcart.R;
import com.example.freshcart.databinding.ActivityProductDetailBinding;
import com.example.freshcart.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding binding;
    Product currentProduct;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding =ActivityProductDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        int id = getIntent().getIntExtra("id",0);
        long price = (long) getIntent().getLongExtra("price",0);
int stock = getIntent().getIntExtra("stock",0);
currentProduct = new Product(name,image,null, price,0,stock,id,null);

        Glide.with(this)
                .load(image)
                .into(binding.productImage);

        binding.productPrice.setText("₹"+price);



        Cart cart = TinyCartHelper.getCart();



        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addItem(currentProduct,1);

                binding.addToCartBtn.setEnabled(false);
                binding.addToCartBtn.setText("Added in cart");

            }
        });



    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cart){
            startActivity(new Intent( this , CartActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
package com.example.freshcart.activities;

import static com.example.freshcart.R.id.*;



import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshcart.R;
import com.example.freshcart.adapters.CategoryAdapter;
import com.example.freshcart.adapters.ProductAdapter;
import com.example.freshcart.databinding.ActivityMainBinding;
import com.example.freshcart.model.Category;
import com.example.freshcart.model.Product;
import com.example.freshcart.model.Users;
import com.example.freshcart.prevalent.Prevalent;
import com.example.freshcart.util.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;
    private ProgressDialog loadingBar;

    //RecyclerView recyclerView;
    ProductAdapter productAdapter;
    ArrayList<Product> products;
    private RecyclerView recyclerView;
    // Create Object of the Adapter class
    DatabaseReference productDb;
@SuppressLint("RestrictedApi")
BottomNavigationView bottomNavigationView;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
       bottomNavigationView.setSelectedItemId(R.id.home);
        Paper.init(this);
        initCategories();
        initProducts();
        initSlider();



        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                return true;
            } else if (itemId == R.id.cart) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    return true;
            } else if (itemId == R.id.account) {
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
            return true;

            }

            return false;
        });

    }



        private void initSlider () {
            binding.carousel.addData(
                    new CarouselItem("https://www.spencers.in/media/catalog/category/FNV_deatils.jpg", "offer1")

            );
            binding.carousel.addData(
                    new CarouselItem("https://www.microgreens.co.in/wp-content/uploads/2024/08/chousa-mango.jpg", "offer2")

            );
            binding.carousel.addData(
                    new CarouselItem("https://bulkbarn.co.nz/cdn/shop/articles/Nutrient-Packed_Dry_Fruits_Nuts_Seeds_for_Your_Spring_Diet.jpg?v=1663069572", "offer3")

            );
        }


        void initCategories(){
            categories = new ArrayList<>();
            categoryAdapter = new CategoryAdapter(this, categories);
            categories.add(new Category("Vegitables", "https://kidlingoo.com/wp-content/uploads/vegetables_name_in_english_50.jpg", "#ffffff", "vegies", 1));
            categories.add(new Category("Fruits", "https://blog.uvihealth.in/content/images/2022/06/Website-Blog-Cover-Images--5-.jpg", "#ffffff", "fruits", 1));
            categories.add(new Category("Spices", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRoev5xoeCAwwuDypw_8_fH8TZWerENmKcK1A&s", "#ffffff", "vegies", 1));
            categories.add(new Category("Dry Fruits", "https://eatanytime.in/cdn/shop/articles/close-up-healthy-nuts-wooden-background-generative-ai.jpg?v=1702453505&width=1100", "#ffffff", "vegies", 1));


//            getCategories();

            GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
            binding.categoriesList.setLayoutManager(layoutManager);
            binding.categoriesList.setAdapter(categoryAdapter);
        }


        void initProducts () {
            products = new ArrayList<>();

            productDb = FirebaseDatabase.getInstance().getReference("Products");

            productAdapter = new ProductAdapter(this, products);

            productDb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        products.add(product);
                    }
                    try {
                         // Handle null or invalid values
                        // Use intValue here
                    } catch (NumberFormatException e) {
                        // Handle the error, e.g., log it or show a message to the user
                        Log.e("FirebaseError", "Failed to convert value to int", e);
                    }
                    productAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("FirebaseError", "Failed to read value.",
                            error.toException());
                }
            });

//        products.add(new Product("Brocoli","https://www.pngitem.com/pimgs/m/583-5838488_steamed-broccoli-transparent-background-hd-png-download.png","",100,10,20,1));
//        products.add(new Product("Cabbage","https://static.vecteezy.com/system/resources/previews/030/657/553/large_2x/cabbage-with-transparent-background-high-quality-ultra-hd-free-photo.jpg","",50,10,50,2));
//        products.add(new Product("Potato 1kg","https://images.jdmagicbox.com/quickquotes/images_main/patato-vegetable-2216996689-nvibzgar.jpg","",40,10,60,3));
//        products.add(new Product("Onion 1kg","https://m.media-amazon.com/images/I/71tV-pB1mbL.jpg","",30,10,80,5));
//        products.add(new Product("Cucumber 1kg","https://freshron.com/wp-content/uploads/2019/11/the-superb-health-benefits-of-cucumber1-scaled.jpg","",45,20,50,6));
//        products.add(new Product("Apple 1kg","https://5.imimg.com/data5/SELLER/Default/2024/5/419514617/ZL/YL/SN/1600400/fresh-apple-fruits-500x500.jpg","",100,0,20,7));
//
//        products.add(new Product("Almond 500gm","https://www.eatingwell.com/thmb/WHQTHrHyDSDmA6_IqewIcFvgO8g=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/what-happens-to-your-body-when-you-eat-nuts-every-day-090a0325cc0641159c2728496a489a4f.jpg","",200,10,20,8));
//      products.add(new Product("Banana 500gm","https://www.kroger.com/product/images/xlarge/back/0000000004011","",35,10,20,4));
//        getRecentProducts();

            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            binding.productList.setLayoutManager(layoutManager);
            binding.productList.setAdapter(productAdapter);
        }



}
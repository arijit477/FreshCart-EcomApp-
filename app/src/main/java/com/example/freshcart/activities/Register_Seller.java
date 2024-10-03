package com.example.freshcart.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.freshcart.R;
import com.example.freshcart.databinding.ActivityRegisterSellerBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register_Seller extends AppCompatActivity {

    ActivityRegisterSellerBinding binding;
    EditText SellerName , Seller_phoneNumber , Seller_password  ;
    Button createAccountButton;
    private ProgressDialog loadingBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_seller);

        binding = ActivityRegisterSellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createAccountButton = (Button) findViewById(R.id.create_seller_AccountButton);
        SellerName = (EditText) findViewById(R.id.sellerName);
        Seller_phoneNumber = (EditText) findViewById(R.id.seller_phoneNumber);
        Seller_password = (EditText) findViewById(R.id.seller_password);
        loadingBar = new ProgressDialog(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {


            String name = SellerName.getText().toString();
            String phone = Seller_phoneNumber.getText().toString();
            String InputPassword = Seller_password.getText().toString();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this,"Please Write Your Name...", Toast.LENGTH_LONG).show();

            } else if (TextUtils.isEmpty(phone)) {
                Toast.makeText(this,"Please Write Your PhoneNumber...", Toast.LENGTH_LONG).show();

            } else if (TextUtils.isEmpty(InputPassword)) {
                Toast.makeText(this,"Please Write Your Password...", Toast.LENGTH_LONG).show();

            }else {

                loadingBar.setTitle("Creating Account");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                ValidatePhoneNumber(name , phone , InputPassword);
            }




    }



        private void ValidatePhoneNumber(String name, String phone, String password) {
            final DatabaseReference Rootref ;
            Rootref = FirebaseDatabase.getInstance().getReference();
            Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!(snapshot.child("Sellers").child(phone).exists())){
                        HashMap<String , Object> user_BuyerDataMap = new HashMap<>();
                        user_BuyerDataMap.put("phone", phone);
                        user_BuyerDataMap.put("password", password);
                        user_BuyerDataMap.put("name", name);

                        Rootref.child("Sellers").child(phone).updateChildren(user_BuyerDataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Register_Seller.this,"Congratulations"+name+"Your account has been Created.", Toast.LENGTH_LONG).show();
                                            loadingBar.dismiss();
                                            Intent intent = new Intent(Register_Seller.this , Login.class);
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(Register_Seller.this,"Network Error! Plrease try again.", Toast.LENGTH_LONG).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });

                    }else {

                        Toast.makeText(Register_Seller.this,"This"+phone+" already Exists", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                        Toast.makeText(Register_Seller.this,"Please Try Again With Another Phone Number!", Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

}
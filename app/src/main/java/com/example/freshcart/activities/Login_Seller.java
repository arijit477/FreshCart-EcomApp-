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

import com.example.freshcart.R;

import com.example.freshcart.model.Sellers;
import com.example.freshcart.prevalent.Prevalent1;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class Login_Seller extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText sellermobileNumber , sellerpassword;
    Button btn_login , btn_register_seller,btn_buyerLogin;

    private ProgressDialog loadingBar;

    private final String parentDbName = "Sellers";

    //check box
    private CheckBox sellercheckBoxRememberMe;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_seller);

        sellermobileNumber = (EditText) findViewById(R.id.sellermobileNum);
        sellerpassword = (EditText) findViewById(R.id.sellerTextpassword);





        sellercheckBoxRememberMe = (CheckBox)findViewById(R.id.sellerremember_me_chkbx);
        Paper.init(this);

        ccp = (CountryCodePicker) findViewById(R.id.sellerccp);
        ccp.registerCarrierNumberEditText(sellermobileNumber);
        btn_login = (Button) findViewById(R.id.sellerLoginBtn);
        btn_register_seller = (Button)findViewById(R.id.btn_register_seller);
        btn_buyerLogin = (Button)findViewById(R.id.btn_buyerLogin);

        loadingBar = new ProgressDialog(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Login.this , MainActivity.class);


                LoginSeller();

            }
        });
        btn_buyerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Seller.this , Login.class);
                startActivity(intent);
            }
        });

        btn_register_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Seller.this , Register_Seller.class);

                startActivity(intent);
            }
        });



        String SellerPhoneKey = Paper.book().read(Prevalent1.SellerPhoneKey);
        String SellerPasswordKey = Paper.book().read(Prevalent1.SellerPasswordKey);

        if (SellerPhoneKey != "" && SellerPasswordKey != ""){
            if (!TextUtils.isEmpty(SellerPhoneKey) && !TextUtils.isEmpty(SellerPasswordKey)){

                AllowAccess(SellerPhoneKey,SellerPasswordKey);
                loadingBar.setTitle("Already Logged in ");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }
        }



    }



    private void AllowAccess( final String phone,  final String password) {
        loadingBar = new ProgressDialog(this);

        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Sellers").child(phone).exists()) {

                   Sellers sellerData = snapshot.child("Sellers").child(phone).getValue(Sellers.class);

                    if (sellerData != null && sellerData.getPhone().equals(phone)) {
                        if (sellerData.getPassword().equals(password)) {
                            Toast.makeText(Login_Seller.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();

                            loadingBar.dismiss();
                            Intent intent = new Intent(Login_Seller.this, Seller.class);
                            startActivity(intent);

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(Login_Seller.this, "Password is Incorrect", Toast.LENGTH_LONG).show();

                        }

                    }

                } else {
                    Toast.makeText(Login_Seller.this, "Account with this" + phone + " number do not Exists!", Toast.LENGTH_LONG).show();
                    Toast.makeText(Login_Seller.this, "You need to Create a new Account ", Toast.LENGTH_LONG).show();

                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoginSeller() {

        String phone = sellermobileNumber.getText().toString();
        String InputPassword = sellerpassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(Login_Seller.this,"Please Write Your PhoneNumber...", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(InputPassword)) {
            Toast.makeText(Login_Seller.this,"Please Write Your Password...", Toast.LENGTH_LONG).show();

        }else {

            loadingBar.setTitle("Loging Account");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone,InputPassword);
        }

    }

    private void AllowAccessToAccount(String phone, String password) {


        ///
        if (sellercheckBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent1.SellerPhoneKey , phone);
            Paper.book().write(Prevalent1.SellerPasswordKey , password);


        }



        final DatabaseReference Rootref ;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(phone).exists()){

                    Sellers sellerData = snapshot.child(parentDbName).child(phone).getValue(Sellers.class);

                    if (sellerData != null && sellerData.getPhone().equals(phone)) {
                        if (sellerData.getPassword().equals(password)) {

                            Toast.makeText(Login_Seller.this, "Logged in Successfully", Toast.LENGTH_LONG).show();

                            loadingBar.dismiss();
                            Intent intent = new Intent(Login_Seller.this, Seller.class);
                            startActivity(intent);


                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(Login_Seller.this, "Password is Incorrect", Toast.LENGTH_LONG).show();

                        }

                    }

                }else {
                    Toast.makeText(Login_Seller.this,"Account with this "+phone+" number do not Exists!", Toast.LENGTH_LONG).show();
                    Toast.makeText(Login_Seller.this,"You need to Create a new Account ", Toast.LENGTH_LONG).show();

                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
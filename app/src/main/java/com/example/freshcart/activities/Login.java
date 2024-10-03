package com.example.freshcart.activities;

import static com.example.freshcart.R.id.btn_seller_login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshcart.R;
import com.example.freshcart.model.Users;
import com.example.freshcart.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText mobileNumber , password;
    Button btn_login , btn_register_buyer,Btn_seller_login;

    private ProgressDialog loadingBar;

    private final String parentDbName = "Users";

    //check box
    private CheckBox checkBoxRememberMe;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_buyer_default);

        mobileNumber = (EditText) findViewById(R.id.mobileNum);
        password = (EditText) findViewById(R.id.Textpassword);





        checkBoxRememberMe = (CheckBox)findViewById(R.id.remember_me_chkbx);
        Paper.init(this);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(mobileNumber);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register_buyer = (Button)findViewById(R.id.btn_register_buyer);
        Btn_seller_login = (Button)findViewById(R.id.btn_seller_login) ;

        loadingBar = new ProgressDialog(this);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Login.this , MainActivity.class);


                LoginUser();

            }
        });

        Btn_seller_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Login_Seller.class);
                startActivity(intent);
            }
        });

        btn_register_buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this , Register.class);

                startActivity(intent);
            }
        });



        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != ""){
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){

                AllowAccess(UserPhoneKey,UserPasswordKey);
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
                if (snapshot.child("Users").child(phone).exists()) {

                    Users userData = snapshot.child("Users").child(phone).getValue(Users.class);

                    if (userData != null && userData.getPhone().equals(phone)) {
                        if (userData.getPassword().equals(password)) {
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_LONG).show();

                            loadingBar.dismiss();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(Login.this, "Password is Incorrect", Toast.LENGTH_LONG).show();

                        }

                    }

                } else {
                    Toast.makeText(Login.this, "Account with this" + phone + " number do not Exists!", Toast.LENGTH_LONG).show();
                    Toast.makeText(Login.this, "You need to Create a new Account ", Toast.LENGTH_LONG).show();

                    loadingBar.dismiss();
                         }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoginUser() {

        String phone = mobileNumber.getText().toString();
        String InputPassword = password.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this,"Please Write Your PhoneNumber...", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(InputPassword)) {
            Toast.makeText(this,"Please Write Your Password...", Toast.LENGTH_LONG).show();

        }else {

            loadingBar.setTitle("Loging Account");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone,InputPassword);
        }

    }

    private void AllowAccessToAccount(String phone, String password) {

        if (checkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey , phone);
            Paper.book().write(Prevalent.UserPasswordKey , password);


        }
        ///




        final DatabaseReference Rootref ;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(phone).exists()){

                    Users userData = snapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (userData != null && userData.getPhone().equals(phone)) {
                        if (userData.getPassword().equals(password)) {


                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_LONG).show();

                            loadingBar.dismiss();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);


                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(Login.this, "Password is Incorrect", Toast.LENGTH_LONG).show();

                        }

                    }

                }else {
                    Toast.makeText(Login.this,"Account with this "+phone+" number do not Exists!", Toast.LENGTH_LONG).show();
                    Toast.makeText(Login.this,"You need to Create a new Account ", Toast.LENGTH_LONG).show();

                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}
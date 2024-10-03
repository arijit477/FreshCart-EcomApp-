package com.example.freshcart.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class Register extends AppCompatActivity {

    EditText userName , phoneNumber , password  ;
Button createAccountButton;
private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        //initializing ids
        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        userName = (EditText) findViewById(R.id.userName);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        password = (EditText) findViewById(R.id.password);
        loadingBar = new ProgressDialog(this);


        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        String name = userName.getText().toString();
        String phone = phoneNumber.getText().toString();
        String InputPassword = password.getText().toString();

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

            if (!(snapshot.child("Users").child(phone).exists())){
                HashMap<String , Object> user_BuyerDataMap = new HashMap<>();
                user_BuyerDataMap.put("phone", phone);
                user_BuyerDataMap.put("password", password);
                user_BuyerDataMap.put("name", name);

                Rootref.child("Users").child(phone).updateChildren(user_BuyerDataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(Register.this,"Congratulations"+name+"Your account has been Created.", Toast.LENGTH_LONG).show();
                                    loadingBar.dismiss();
                                    Intent intent = new Intent(Register.this , Login.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(Register.this,"Network Error! Plrease try again.", Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                }
                            }
                        });

            }else {

                Toast.makeText(Register.this,"This"+phone+" already Exists", Toast.LENGTH_LONG).show();
                loadingBar.dismiss();
                Toast.makeText(Register.this,"Please Try Again With Another Phone Number!", Toast.LENGTH_LONG).show();


            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });


    }
}
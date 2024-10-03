package com.example.freshcart.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.freshcart.R;
import com.example.freshcart.databinding.ActivityAddingProductBinding;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Adding_Product extends AppCompatActivity {
ActivityAddingProductBinding binding;
private String categoryName , Description ,ProductName , productQuantity , saveCurrentDate ,saveCurrentTime , sellerName;
private long Price ;
ImageView select_product_image;
EditText input_productName,input_productDescription ,input_productPrice,input_productQuantity, input_sellerName;
Button btn_AddProduct;
TextView category_top_heading;
private static  final int GalleryPick = 1;
private Uri ImageUri;

private String productRandomKey , downloadImageUrl;
private ProgressDialog loadingBar;
private StorageReference ProductImagesRef;
private DatabaseReference productRef;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_adding_product);

        binding = ActivityAddingProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         categoryName = getIntent().getStringExtra("name");


        category_top_heading = (TextView)findViewById(R.id.CategoryTopHeading);

        select_product_image = (ImageView)findViewById(R.id.select_product_image) ;

        input_productName = (EditText)findViewById(R.id.input_productName);
        input_sellerName = (EditText)findViewById(R.id.input_sellerName);
        input_productDescription = (EditText)findViewById(R.id.input_productDescription);
        input_productPrice = (EditText)findViewById(R.id.input_productPrice);
        input_productQuantity = (EditText)findViewById(R.id.input_productQuantity);

        btn_AddProduct = (Button)findViewById(R.id.btn_AddProduct) ;
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product_Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        loadingBar = new ProgressDialog(this);

        category_top_heading.setText("CATEGORY : " + categoryName);

        Toast.makeText(this,"category"+categoryName , Toast.LENGTH_LONG).show();

        select_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening gallery
                // later camera option will added
                OpenGallery();
            }
        });

        btn_AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });



    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        galleryIntent.setType("image/*");
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GalleryPick);

        OpenGallery.launch(galleryIntent);
    }

ActivityResultLauncher<Intent> OpenGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                ImageUri = result.getData().getData();
                select_product_image.setImageURI(ImageUri);

            }
        });


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if ((resultCode == GalleryPick) && (resultCode == RESULT_OK) && (data != null)){
//
//            ImageUri = data.getData();
//            select_product_image.setImageURI(ImageUri);
//
//        }
//    }



    private void ValidateProductData() {
        sellerName =  input_sellerName.getText().toString();
        Description = input_productDescription.getText().toString();
        ProductName = input_productName.getText().toString();
        Price = Long.parseLong(input_productPrice.getText().toString());
        productQuantity = input_productQuantity.getText().toString();

        if (ImageUri == null){
            Toast.makeText(this,"Product image is mandatory.." , Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(sellerName)) {
            Toast.makeText(this, "Please Write Seller Name..", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(ProductName)) {
            Toast.makeText(this,"Please Write Product Name.." , Toast.LENGTH_LONG).show();

        }else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this,"Please Write Product Description.." , Toast.LENGTH_LONG).show();

        }else if (Price != 0 && Price < 0) {
            Toast.makeText(this,"Please Write Product Price.." , Toast.LENGTH_LONG).show();

        }else if (TextUtils.isEmpty(productQuantity)) {
            Toast.makeText(this,"Please Write Product Quantity.." , Toast.LENGTH_LONG).show();

        }
        else {
            StoreProductInformation();
        }


    }

    private void StoreProductInformation() {

        loadingBar.setTitle("Adding New Product ");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("MMM, dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate+saveCurrentTime;

       StorageReference filePath =  ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

       final UploadTask uploadTask = filePath.putFile(ImageUri);
       uploadTask.addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

               String message = e.toString();
               Toast.makeText(Adding_Product.this,"Error :"+ message , Toast.LENGTH_LONG).show();

               loadingBar.dismiss();
           }
       }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               Toast.makeText(Adding_Product.this,"Product image Uploaded Successfully" , Toast.LENGTH_LONG).show();

               Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                   @Override
                   public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                       if (!task.isSuccessful()){
                           throw task.getException();
                       }
                       downloadImageUrl = filePath.getDownloadUrl().toString();
                       return  filePath.getDownloadUrl();

                   }
               }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                   @Override
                   public void onComplete(@NonNull Task<Uri> task) {

                       if (task.isSuccessful()){

                           downloadImageUrl = task.getResult().toString();
                           Toast.makeText(Adding_Product.this,"Get the Product image Url Successfully" , Toast.LENGTH_LONG).show();

                           saveProductInfoToDatabase();
                       }
                   }
               });

           }
       });

    }

    private void saveProductInfoToDatabase() {

        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pid" , productRandomKey);
        productMap.put("date" , saveCurrentDate);
        productMap.put("time" , saveCurrentTime);
        productMap.put("image" , downloadImageUrl);
        productMap.put("description" , Description);
        productMap.put("quantity" , productQuantity);
        productMap.put("price" , Price);
        productMap.put("name" , ProductName);
        productMap.put("seller_name" , sellerName);
        productMap.put("category" , categoryName);

        productRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            Intent intent = new Intent(Adding_Product.this , SellerCategoryActivity.class);

                            startActivity(intent);

                            loadingBar.dismiss();

                            Toast.makeText(Adding_Product.this,"Product is added successfully" , Toast.LENGTH_LONG).show();

                        }else {
                            loadingBar.dismiss();

                            String msg = task.getException().toString();
                            Toast.makeText(Adding_Product.this,"Error :"+msg , Toast.LENGTH_LONG).show();

                        }

                    }
                });

    }



}
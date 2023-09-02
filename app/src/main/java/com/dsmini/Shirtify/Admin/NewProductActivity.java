package com.dsmini.Shirtify.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.dsmini.Shirtify.Model.Product;
import com.dsmini.Shirtify.R;
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
import com.squareup.picasso.Picasso;

public class NewProductActivity extends AppCompatActivity {
    String[] categoriesList = {"Select Category", "Shirt","T - Shirt","Mens Skinny","Frock's","Shift Dress","Midi Dress"};

    Spinner categorySpinner;
    String category = "";

    int PICK_IMAGE_REQUEST = 111;
    Uri filePath=null;
    StorageReference storageRef;
    DatabaseReference myRootRef;
    ImageView uploadPhotoBtn, productImg;
    Button addBtn;
    private String downloadImageUrl = "";
    private EditText nameEt,priceEt,stockEt,descriptionEt;
    private ProgressBar progressBar;
    Product product;
    boolean isEdit=false;

    private AutoCompleteTextView autoCompleteTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        initAll();

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_list_item_1, categoriesList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        String[] cityArray = getResources().getStringArray(R.array.city_names);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_list_item_1, cityArray);
        autoCompleteTextView.setAdapter(arrayAdapter);



        SettingClickListners();


    }

    private void SettingClickListners() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        uploadPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameEt.getText().toString().trim();
                String price=priceEt.getText().toString().trim();
                String stock=stockEt.getText().toString().trim();
                String desc=descriptionEt.getText().toString().trim();
                String loc=autoCompleteTextView.getText().toString().trim();
                if(!isEdit){
                    if(filePath==null){
                        Toast.makeText(NewProductActivity.this, "Please select product image", Toast.LENGTH_SHORT).show();
                    }
                }
                if(TextUtils.isEmpty(name)){
                    nameEt.setError("Enter product name");
                    nameEt.requestFocus();
                }
                else if(category.equals("Select Category")){
                    Toast.makeText(NewProductActivity.this, "Select category", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(price)){
                    priceEt.setError("Enter product price");
                    priceEt.requestFocus();
                }

                else if(TextUtils.isEmpty(stock)){
                    stockEt.setError("Enter product stock");
                    stockEt.requestFocus();
                }
                else if(TextUtils.isEmpty(desc)){
                    descriptionEt.setError("Enter product description");
                    descriptionEt.requestFocus();
                }
                else{

                    if(isEdit){
                        product.setName(name);
                        product.setCategory(category);
                        product.setPrice(Double.parseDouble(price));
                        product.setStock(stock);
                        product.setLocation(loc);
                        product.setDescription(desc);
                        UploadImage();
                    }
                    else{
                        product.setName(name);
                        product.setCategory(category);
                        product.setPrice(Double.parseDouble(price));
                        product.setStock(stock);
                        product.setLocation(loc);
                        product.setDescription(desc);
                        UploadImage();
                    }


                }

            }
        });
    }

    private void initAll() {
        categorySpinner = findViewById(R.id.product_category_Spinner);
        progressBar = findViewById(R.id.progress_bar);

        uploadPhotoBtn = findViewById(R.id.upload_image_btn);
        productImg = findViewById(R.id.product_image);
        addBtn = findViewById(R.id.add_btn);

        nameEt=findViewById(R.id.product_name_et);
        priceEt=findViewById(R.id.price_et);

        autoCompleteTextView = findViewById(R.id.city);

        stockEt=findViewById(R.id.stock_et);
        descriptionEt=findViewById(R.id.description_tv);

        storageRef = FirebaseStorage.getInstance().getReference();
        myRootRef = FirebaseDatabase.getInstance().getReference();
        product=new Product();



        if(getIntent().getSerializableExtra("product")!=null){
            isEdit=true;
            addBtn.setText("Update");
            product= (Product) getIntent().getSerializableExtra("product");
            nameEt.setText(product.getName());
            priceEt.setText(product.getPrice()+"");
            stockEt.setText(product.getStock()+"");
            descriptionEt.setText(product.getDescription()+"");
            autoCompleteTextView.setText(product.getLocation()+"");

            if (product.getPhotoUrl() != null) {
                if (!product.getPhotoUrl().equals("")) {
                    Picasso.get().load(product.getPhotoUrl()).placeholder(R.drawable.logo).into(productImg);
                }
            }
        }
    }

    public void goBack(View view) {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(filePath));
                productImg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void UploadImage() {
        if (filePath != null) {
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference childRef = storageRef.child("product_images").child(System.currentTimeMillis() + ".jpg");
            final UploadTask uploadTask = childRef.putFile(filePath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(NewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(NewProductActivity.this, "Photo uploaded...", Toast.LENGTH_SHORT).show();
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            downloadImageUrl = childRef.getDownloadUrl().toString();
                            return childRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadImageUrl = task.getResult().toString();
                                Log.d("imagUrl", downloadImageUrl);
                                product.setPhotoUrl(downloadImageUrl);
                                SaveInfoToDatabase();
                            }
                        }
                    });
                }
            });

        } else {
            if(isEdit){
                SaveInfoToDatabase();
            }
            else{
                Toast.makeText(NewProductActivity.this, "Select an image", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void SaveInfoToDatabase() {
        String key="";
        if(isEdit){
           key=product.getProductId();
        }
        else{
            key=myRootRef.push().getKey();
            product.setProductId(key);
        }

        myRootRef.child("Products").child(key).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                if(isEdit){
                    Toast.makeText(NewProductActivity.this, "Product updated Successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(NewProductActivity.this, "Product added Successfully", Toast.LENGTH_SHORT).show();

                }
                finish();
                Log.d("TAG", "Saved!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Log.d("test", e.toString());
            }
        });
    }
}
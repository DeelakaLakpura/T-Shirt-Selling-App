package com.dsmini.Shirtify.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsmini.Shirtify.Model.Order;
import com.dsmini.Shirtify.Model.Product;
import com.dsmini.Shirtify.R;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class ProductDetailsActivity extends AppCompatActivity {

    private CardView addToCartBtn;
    private ImageView productImg,whatsapp;
    private TextView plusBTn,minusBtn,quantityTV;
    private TextView productName,productDescription,price,txtphone,loc;
    Product product;

    CharSequence phoneNumber;



    int quantity=1;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);



        initAll();
        ClickListeners();

        product= (Product) getIntent().getSerializableExtra("product");


        if(product.getPhotoUrl()!=null){
            if(!product.getPhotoUrl().equals("")){
                Picasso.get().load(product.getPhotoUrl()).placeholder(R.drawable.logo).into(productImg);
            }
        }
        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        price.setText("Rs:"+product.getPrice());
        txtphone.setText(product.getStock());
       phoneNumber = txtphone.getText();
       loc.setText(product.getLocation());


    }

    private void ClickListeners() {
        plusBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity+=1;
                quantityTV.setText(String.valueOf(quantity));
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity>1){
                    quantity-=1;
                    quantityTV.setText(String.valueOf(quantity));
                }
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pn = phoneNumber.toString();
                String countryCode = "+94"; // Replace with the appropriate country code

                if (pn.startsWith("0")) {
                    phoneNumber = pn.substring(1); // Remove the leading 0
                    phoneNumber = countryCode + phoneNumber; // Prepend the country code
                }

                String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
                builder.setTitle("Contact Seller")
                        .setMessage("Are you sure you want to call this Seller?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null);

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });
    }

    private void initAll() {
        addToCartBtn=findViewById(R.id.add_to_cart_btn);
        productImg=findViewById(R.id.product_img);
        plusBTn=findViewById(R.id.plus_btn);
        minusBtn=findViewById(R.id.minus_btn);
        quantityTV=findViewById(R.id.quantity_tv);
        productName=findViewById(R.id.product_name);
        price=findViewById(R.id.product_price);
        txtphone=findViewById(R.id.phone);
        productDescription=findViewById(R.id.product_description);
        whatsapp = findViewById(R.id.wp);
        loc = findViewById(R.id.location);

        product=new Product();

        order=new Order();

        if(Paper.book().read("order")!=null){
            order=Paper.book().read("order");
        }

    }

    public void goBack(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Paper.book().read("order")!=null){
            order=Paper.book().read("order");
        }
    }
}
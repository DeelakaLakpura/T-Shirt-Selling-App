package com.dsmini.Shirtify.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsmini.Shirtify.Model.Order;
import com.dsmini.Shirtify.Model.Product;
import com.dsmini.Shirtify.Model.User;
import com.dsmini.Shirtify.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class OrderDetailsAdmin extends AppCompatActivity {


    private TextView totalPrice, status, date, address, nameTv, emailTv, genderTv;
    CircleImageView profile;

    private ProductsAdapter mAdapter;
    private RecyclerView recyclerView;
    private ImageView backBtn;

    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_admin);


        totalPrice = findViewById(R.id.total_price);
        status = findViewById(R.id.status);
        date = findViewById(R.id.date);
        address = findViewById(R.id.address);
        recyclerView = findViewById(R.id.product_list);
        backBtn = findViewById(R.id.checkout_back_btn);

        nameTv = findViewById(R.id.name_tv);
        emailTv = findViewById(R.id.email_tv);
        genderTv = findViewById(R.id.gender_tv);
        profile = findViewById(R.id.user_profile);


        order = (Order) getIntent().getSerializableExtra("order");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        loadUserData(order.getCustomerId());

        if (order != null) {
            totalPrice.setText("Total Price :Rm " + order.getTotalPrice());
            status.setText("Status :" + order.getStatus());
            date.setText("Date: " + order.getDateOfOrder());
            address.setText("Address: " + order.getAddress());

            mAdapter = new ProductsAdapter(order.getCartProductList(), this, false);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void loadUserData(String customerId) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(customerId);
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = new User();
                    user = dataSnapshot.getValue(User.class);

                    if (user != null) {
                        Log.d("usertest", user.getPhotoUrl());
                    } else {
                        Log.d("usertest", user.toString());
                    }
                    String image = dataSnapshot.child("photoUrl").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String eamil = dataSnapshot.child("email").getValue().toString();
                    String  gender = dataSnapshot.child("gender").getValue().toString();
                    Paper.book().write("username", name);
                    try {
                        if (image != null && !image.equals("")) {
                            Picasso.get().load(image).placeholder(R.drawable.profile).into(profile);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    nameTv.setText("Name: " + name);
                    emailTv.setText("Email: " + eamil);
                    if (gender.equals("0")) {
                        genderTv.setText("Gender : Female");
                    } else {
                        genderTv.setText("Gender : Male");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

        List<Product> myJokesList;
        Activity context;
        boolean isAdmin;
        DatabaseReference myRootRef;


        public ProductsAdapter(List<Product> usersList, Activity context, boolean isAdmin) {
            this.myJokesList = usersList;
            this.context = context;
            this.isAdmin = isAdmin;
            myRootRef = FirebaseDatabase.getInstance().getReference();
        }

        @NonNull
        @Override
        public ProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_product_layout, parent, false);

            return new ProductsAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductsAdapter.MyViewHolder holder, int position) {

            Product product = myJokesList.get(position);

            if (product.getPhotoUrl() != null) {
                if (!product.getPhotoUrl().equals("")) {
                    holder.productImg.setVisibility(View.VISIBLE);
                    Picasso.get().load(product.getPhotoUrl()).placeholder(R.drawable.logo).into(holder.productImg);
                }
            }
            holder.name.setText(product.getName());
            holder.price.setText("RM" + product.getPrice());
        }

        @Override
        public int getItemCount() {
            return myJokesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            LinearLayout layout;
            ImageView productImg;
            TextView name, price;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.layout);
                productImg = itemView.findViewById(R.id.category_image);
                name = itemView.findViewById(R.id.product_brand_name);
                price = itemView.findViewById(R.id.price_tv);
            }
        }
    }
}
package com.dsmini.Shirtify.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dsmini.Shirtify.Activity.LoginActivity;
import com.dsmini.Shirtify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class AdminHome extends AppCompatActivity {

    int counter = 0;
    private RelativeLayout logoutBtn, newProduct, viewProduct, viewCustomersOrders,viewfeedback,reports;
    private FirebaseAuth mAuth;
    DatabaseReference myRootRef;
    private TextView orderCount;
    private ProgressBar countProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Paper.init(AdminHome.this);

        initAll();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Paper.book().delete("active");
                Intent intent = new Intent(AdminHome.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHome.this, ReportsActivity.class);
                startActivity(intent);
            }
        });
        newProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHome.this, NewProductActivity.class);
                startActivity(intent);
            }
        });
        viewfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHome.this, AdminFeedbackActivity.class);
                startActivity(intent);
            }
        });
        viewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHome.this, ViewAllProductsActivity.class);
                startActivity(intent);
            }
        });
        viewCustomersOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHome.this, CustomersOrders.class);
                intent.putExtra("type", "admin");
                startActivity(intent);
            }
        });
        getOrdersCount();
    }

    public void getOrdersCount() {
        countProgressBar.setVisibility(View.VISIBLE);
        myRootRef.child("Order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("Testchildcound", dataSnapshot.getChildrenCount() + "");
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String id = child.getKey();
                        getOrders(id);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void getOrders(String id) {
        myRootRef.child("Order").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        counter++;
                        Log.d("counter", counter + "");
                        orderCount.setText(counter+"");
                    }
                    countProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    private void initAll() {
        logoutBtn = findViewById(R.id.logout_btn);
        reports = findViewById(R.id.view_monthly_reports);
        newProduct = findViewById(R.id.new_product_layout);
        viewProduct = findViewById(R.id.view_all_products);
        orderCount = findViewById(R.id.orders_count);
        viewfeedback = findViewById(R.id.view_all_feedback);
        countProgressBar = findViewById(R.id.order_count_progress);
        viewCustomersOrders = findViewById(R.id.view_customer_order);
        myRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }
}
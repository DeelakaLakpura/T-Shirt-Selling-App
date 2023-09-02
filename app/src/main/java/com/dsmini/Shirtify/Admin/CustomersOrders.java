package com.dsmini.Shirtify.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dsmini.Shirtify.Adapter.OrdersAdapter;
import com.dsmini.Shirtify.Model.Order;
import com.dsmini.Shirtify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomersOrders extends AppCompatActivity {

    private OrdersAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Order> orderArrayList;

    DatabaseReference myRootRef;
    private ProgressBar progressBar;
    private TextView noJokeText;
    private FirebaseAuth mAuth;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_orders);

        initAll();
        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        if (type.equals("user")) {
            getUserOrders();
        } else {
            getAdminOrders();
        }
    }

    public void getAdminOrders() {
        progressBar.setVisibility(View.VISIBLE);
        myRootRef.child("Order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("Testchildcound",dataSnapshot.getChildrenCount()+"");
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String id=child.getKey();
                        getOrders(id);
                    }
                } else {
                    noJokeText.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
    public void getOrders(String id) {
        final int[] counter = {0};
        myRootRef.child("Order").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Order order = new Order();
                        order = child.getValue(Order.class);
                        orderArrayList.add(order);
                        counter[0]++;
                        if (counter[0] == dataSnapshot.getChildrenCount()) {
                            setData();
                        }
                        Log.d("ShowEventInfo:", order.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    public void getUserOrders() {
        progressBar.setVisibility(View.VISIBLE);
        final int[] counter = {0};
        myRootRef.child("Order").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Order order = new Order();
                        order = child.getValue(Order.class);
                        orderArrayList.add(order);
                        counter[0]++;
                        if (counter[0] == dataSnapshot.getChildrenCount()) {
                            setData();
                            progressBar.setVisibility(View.GONE);
                        }
                        Log.d("ShowEventInfo:", order.toString());
                    }
                } else {
                    noJokeText.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void setData() {
        progressBar.setVisibility(View.GONE);
        if (orderArrayList.size() > 0) {
            mAdapter = new OrdersAdapter(CustomersOrders.this, orderArrayList);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(CustomersOrders.this));
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            
            recyclerView.setVisibility(View.VISIBLE);
            noJokeText.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noJokeText.setVisibility(View.VISIBLE);
        }
    }

    public void goBack(View view) {
        finish();
    }

    private void initAll() {
        orderArrayList = new ArrayList<Order>();
        recyclerView = findViewById(R.id.cart_recyclerview);
        progressBar = findViewById(R.id.spin_progress_bar);
        noJokeText = findViewById(R.id.no_product);
        myRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

    }
}
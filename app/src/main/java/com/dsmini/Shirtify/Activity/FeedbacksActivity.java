package com.dsmini.Shirtify.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dsmini.Shirtify.Adapter.UserFeedbackAdapter;
import com.dsmini.Shirtify.Model.Feedback;
import com.dsmini.Shirtify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedbacksActivity extends AppCompatActivity {

    private UserFeedbackAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Feedback> orderArrayList;

    DatabaseReference myRootRef;
    private ProgressBar progressBar;
    private TextView noJokeText;
    private FirebaseAuth mAuth;

    private String type;
    private CardView addFeedbackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbacks);

        initAll();

        addFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FeedbacksActivity.this,AddNewFeedbackActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        orderArrayList.clear();
        getUserFeedback();
    }

    public void getUserFeedback() {
        progressBar.setVisibility(View.VISIBLE);
        final int[] counter = {0};
        myRootRef.child("Feedback").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Feedback order = new Feedback();
                        order = child.getValue(Feedback.class);
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
            mAdapter = new UserFeedbackAdapter(FeedbacksActivity.this, orderArrayList);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(FeedbacksActivity.this));
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
        orderArrayList = new ArrayList<Feedback>();
        recyclerView = findViewById(R.id.cart_recyclerview);
        progressBar = findViewById(R.id.spin_progress_bar);
        noJokeText = findViewById(R.id.no_product);
        addFeedbackBtn = findViewById(R.id.add_new_feedback);
        myRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

    }

}
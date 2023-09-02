package com.dsmini.Shirtify.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dsmini.Shirtify.Model.Feedback;
import com.dsmini.Shirtify.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import io.paperdb.Paper;

public class AddNewFeedbackActivity extends AppCompatActivity {


    private ImageView backBtn;
    private EditText feedbackEt;
    private Button submitBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_feedback);

        initAll();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernmae = Paper.book().read("username");

                String text = feedbackEt.getText().toString().trim();
                if (text.isEmpty()) {
                    Toast.makeText(AddNewFeedbackActivity.this, "Please enter your feedback", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Feedback");
                    String key = root.push().getKey();
                    progressBar.setVisibility(View.VISIBLE);

                    Feedback feedback = new Feedback();
                    feedback.setText(text);
                    feedback.setId(key);
                    feedback.setStatus("Processing");
                    feedback.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    feedback.setUsername(usernmae);
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    feedback.setDate(currentDateTimeString);

                    root.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).setValue(feedback).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AddNewFeedbackActivity.this, "Feedback Submitted", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewFeedbackActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });


                }

            }
        });

    }

    private void initAll() {

        backBtn = findViewById(R.id.cart_back_arrow);
        feedbackEt = findViewById(R.id.feedback_et);
        submitBtn = findViewById(R.id.submit_btn);
        progressBar = findViewById(R.id.progress_bar);
    }
}
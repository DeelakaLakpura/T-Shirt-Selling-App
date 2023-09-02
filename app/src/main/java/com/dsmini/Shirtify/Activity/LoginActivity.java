package com.dsmini.Shirtify.Activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.dsmini.Shirtify.Admin.AdminLoginActivity;
import com.dsmini.Shirtify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    String userEmail, userPass;
    private EditText email, pass;
    private TextView forgetPass,loginResultTv;
    private AppCompatButton loginBtn;
    private Button didNotHaveAcc;
    private ImageView adminLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference myRootRef;

    private TextView slidingTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        slidingTextView = findViewById(R.id.slidingTextView);
        slidingTextView.setVisibility(View.VISIBLE);
        startAnimation();

        initAll();
        settingUpListners();

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void startAnimation() {
        float screenWidth = getResources().getDisplayMetrics().widthPixels;
        float textViewWidth = slidingTextView.getWidth();

        ObjectAnimator animator = ObjectAnimator.ofFloat(slidingTextView, "translationX", screenWidth, -textViewWidth);
        animator.setDuration(2000); // Adjust the duration as needed
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    private void settingUpListners() {
        didNotHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupUserActivity.class);
                startActivity(intent);
            }
        });
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = email.getText().toString().trim();
                userPass = pass.getText().toString().trim();
                if (TextUtils.isEmpty(userEmail)) {
                    email.setError("enter email");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(userPass)) {
                    pass.setError("enter pass");
                    pass.requestFocus();
                } else {
                    //call the signin funtion here
                    progressBar.setVisibility(View.VISIBLE);
                    loginBtn.setVisibility(View.GONE);

                    mAuth.signInWithEmailAndPassword(userEmail, userPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        loginResultTv.setText("pass");
                                        //sign in Success
                                        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        myRootRef.child("Users").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //place data in datasnapshot that we can show
                                                if (dataSnapshot.exists()) {
                                                    progressBar.setVisibility(View.GONE);
                                                    loginBtn.setEnabled(true);
                                                    loginBtn.setVisibility(View.VISIBLE);
                                                    Paper.book().write("active", "user");
                                                    Toast.makeText(LoginActivity.this, "Welcome..!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    mAuth.signOut();
                                                    Toast.makeText(LoginActivity.this, "This is not User login details", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    loginBtn.setEnabled(true);
                                                    loginBtn.setVisibility(View.VISIBLE);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        loginResultTv.setText("fail");
                                        progressBar.setVisibility(View.GONE);
                                        loginBtn.setEnabled(true);
                                        loginBtn.setVisibility(View.VISIBLE);
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });


        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomAlertDialog);
                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.update_password_layout, viewGroup, false);

                final EditText emailImput = (EditText) dialogView.findViewById(R.id.loginEmailEditText);
                final Button verifyBtn = (Button) dialogView.findViewById(R.id.loginBtnLinLayout);
                final ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.spin_progress_bar);

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                verifyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String email = emailImput.getText().toString().trim();

                        if (!TextUtils.isEmpty(email)) {
                            progressBar.setVisibility(View.VISIBLE);
                            verifyBtn.setVisibility(View.GONE);

                            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //when email has been successfully send
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        verifyBtn.setVisibility(View.VISIBLE);
                                        Log.d("testPassrest", "successfull");
                                        Toast.makeText(LoginActivity.this, "Email Has Been Sent", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    } else {
                                        Log.d("testPassrest", "fail");
                                        Toast.makeText(LoginActivity.this, "Email Failed, Please Try Again", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //if email failed
                                    progressBar.setVisibility(View.GONE);
                                    verifyBtn.setVisibility(View.VISIBLE);
                                    Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alertDialog.show();
            }
        });

    }


    private void initAll() {
        email = findViewById(R.id.login_email);
        forgetPass = findViewById(R.id.forget_pass);
        pass = findViewById(R.id.login_pass);
        loginBtn = findViewById(R.id.login_btn);
        adminLogin = findViewById(R.id.manager_portal_btn);
        loginResultTv = findViewById(R.id.login_results);

        didNotHaveAcc = findViewById(R.id.login_signup_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        myRootRef = FirebaseDatabase.getInstance().getReference();


    }
}
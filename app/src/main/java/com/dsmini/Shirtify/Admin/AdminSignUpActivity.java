package com.dsmini.Shirtify.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dsmini.Shirtify.Model.User;
import com.dsmini.Shirtify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class AdminSignUpActivity extends AppCompatActivity {
    private static final String TAG = "signupTag";
    User user;
    //Firebase
    FirebaseAuth mAuth;
    DatabaseReference myRootRef;
    String userName, userEmail, userPass,userAddress;
    private EditText name, email, pass,address;
    private Button SignUPBtn, GoToLoginBtn;
    private ProgressBar progressBar;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private int selectedGender=1;
    RadioGroup radioGroupGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);

        initAll();
        settingUpListners();
    }

    private void settingUpListners() {
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.male_radio:
                        selectedGender=1;
                        break;
                    case R.id.female_radio:
                        selectedGender=0;
                        break;
                }
            }
        });

        GoToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminSignUpActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });
        SignUPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = name.getText().toString().trim();
                userEmail = email.getText().toString().trim();
                userPass = pass.getText().toString().trim();
                userAddress = address.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    name.setError("Enter Full name");
                } else if (TextUtils.isEmpty(userEmail)) {
                    email.setError("Enter email");
                }
                else if (!userEmail.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Invalid email address, enter valid email id", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(userPass)) {
                    pass.setError("Enter pass");
                }
                else if (TextUtils.isEmpty(userAddress)) {
                    address.setError("Enter your address");
                }
                else {
                    //signup code goes here
                    RegisterNewAccount();
                }
            }
        });
    }

    private void RegisterNewAccount() {
        //creating new account on firebase for user
        progressBar.setVisibility(View.VISIBLE);
        SignUPBtn.setVisibility(View.GONE);

        user.setName(userName);
        user.setEmail(userEmail);
        user.setPass(userPass);
        user.setAddress(userAddress);
        user.setGender(selectedGender);
        user.setUserType("admin");
        user.setPhotoUrl("");

        //creating account
        mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            user.setUserId(currentUserId);
                            myRootRef.child("Admin").child(currentUserId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //show message
                                    Toast.makeText(AdminSignUpActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                                    Paper.book().write("admin", user);
                                    Paper.book().write("active", "admin");

                                    Intent intent=new Intent(AdminSignUpActivity.this,AdminHome.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, e.toString());
                                }
                            });
                        } else {
                            Toast.makeText(AdminSignUpActivity.this, "Failed to Create Account..!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void initAll() {
        //casting
        name = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        pass = findViewById(R.id.signp_pass);
        progressBar = findViewById(R.id.signup_progressbar);
        SignUPBtn = findViewById(R.id.signup_btn);
        GoToLoginBtn = findViewById(R.id.already_have_account_btn);
        address = findViewById(R.id.location_et);
        radioGroupGender = (RadioGroup) findViewById(R.id.radioGroup1);
        //initialize mauth
        mAuth = FirebaseAuth.getInstance();
        //getting path
        myRootRef = FirebaseDatabase.getInstance().getReference();
        //initialize function
        user = new User();

    }
}
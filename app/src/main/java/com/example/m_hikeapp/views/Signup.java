package com.example.m_hikeapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.m_hikeapp.R;
import com.example.m_hikeapp.viewmodels.SignInSignUpWithEmailViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;


public class Signup extends AppCompatActivity {
    private SignInSignUpWithEmailViewModel viewModel;
    private TextView signupToLogin;
    private Button signUpEmailBtn;
    private RelativeLayout google_btn;

    @Override
    protected void onStart() {
        super.onStart();
        viewModel = new ViewModelProvider(this).get(SignInSignUpWithEmailViewModel.class);
        if (viewModel.isUserSignedIn()){
            startActivity(new Intent(Signup.this, Home.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
    }

    private void init(){
//        method for all initialisation
        signupToLogin = findViewById(R.id.signupToLogin);
        signUpEmailBtn = findViewById(R.id.signUpEmailBtn);
        google_btn = findViewById(R.id.google_btn);

        signupToLogin.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        signUpEmailBtn.setOnClickListener(v -> {
            startActivity(new Intent(Signup.this, SignupWithEmail.class));
            finish();
        });
        google_btn.setOnClickListener(v -> {
            viewModel.startGoogleSignIn(this);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                viewModel.firebaseAuthWithGoogle(Signup.this, account);
            } catch (ApiException e) {
                if (e.getStatusCode() != 12501){
                    Snackbar.make(google_btn, R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }
}
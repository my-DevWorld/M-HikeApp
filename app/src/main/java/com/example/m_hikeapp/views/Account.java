package com.example.m_hikeapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.m_hikeapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Account extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private TextView logoutBtn;
    private FrameLayout progressContainer;

    private static FirebaseAuth mAuth;
    private GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        init();
        GoogleSignInClientConfig();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        progressContainer = findViewById(R.id.progressContainer);
        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            progressContainer.setVisibility(View.VISIBLE);
            mAuth.signOut();
            signInClient.signOut();
            startActivity(new Intent(this, Login.class));
            finish();
        });
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setItemActiveIndicatorColor(null);
        bottomNavigation.setSelectedItemId(R.id.account);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(this, Home.class));
                    overridePendingTransition(0,0);
                    finish();
                    return true;
                case R.id.saved:
                    startActivity(new Intent(this, Favourite.class));
                    overridePendingTransition(0,0);
                    finish();
                    return true;
                case R.id.account:
                    return true;
            }
            return false;
        });
    }

    private void GoogleSignInClientConfig() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestScopes(new Scope(Scopes.PROFILE))
                .build();

        signInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Home.class));
        overridePendingTransition(0,0);
        finish();
        super.onBackPressed();
    }
}
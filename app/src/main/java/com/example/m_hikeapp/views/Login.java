package com.example.m_hikeapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.m_hikeapp.R;
import com.example.m_hikeapp.utils.UISupport;
import com.example.m_hikeapp.viewmodels.SignInSignUpWithEmailViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {
    private SignInSignUpWithEmailViewModel viewModel;
    private UISupport uiSupport;

    private TextView loginToSignup, forgotPassword_label, errorMsg;
    private Button loginBtn;
    private FrameLayout progressContainer;
    private TextInputLayout emailOutline, passwordOutline;
    private TextInputEditText loginEmail, loginPassword;
    private RelativeLayout google_btn;

    boolean isReady = false;

    @Override
    protected void onStart() {
        super.onStart();
        viewModel = new ViewModelProvider(this).get(SignInSignUpWithEmailViewModel.class);
        if (viewModel.isUserSignedIn()){
            startActivity(new Intent(Login.this, Home.class));
            finish();
            return;
        }
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            startActivity(new Intent(this, Home.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidx.core.splashscreen.SplashScreen splashScreen = androidx.core.splashscreen.SplashScreen.installSplashScreen(this);
        View content = findViewById(android.R.id.content);
//        content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                if(isReady){
//                    content.getViewTreeObserver().removeOnPreDrawListener(this);
//                }
//                dismissSplashScreen();
//                return false;
//            }
//        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){
//        method for all initialisation
        uiSupport = new UISupport();
        loginToSignup = findViewById(R.id.loginToSignup);
        forgotPassword_label = findViewById(R.id.forgotPassword_label);
        loginBtn = findViewById(R.id.loginBtn);
        progressContainer = findViewById(R.id.progressContainer);
        emailOutline = findViewById(R.id.emailOutline);
        passwordOutline = findViewById(R.id.passwordOutline);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        errorMsg = findViewById(R.id.errorMsg);
        google_btn = findViewById(R.id.google_btn);

        loginToSignup.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Signup.class));
        });
        forgotPassword_label.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, PasswordReset.class));
        });
        loginBtn.setOnClickListener(v -> {
            uiSupport.hideSoftKeyboard(this, loginBtn);
            loginBtn.requestFocus();
            loginBtn.setFocusable(true);
            loginPassword.clearFocus();
            formValidation();
        });

        google_btn.setOnClickListener(v -> {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if(acct!=null){
                startActivity(new Intent(this, Home.class));
                finish();
            }else viewModel.startGoogleSignIn(this);
        });

        loginEmail.addTextChangedListener(userEmailEditTextWatcher);
        loginPassword.addTextChangedListener(userPasswordEditTextWatcher);
    }

//    private void dismissSplashScreen() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isReady = true;
//            }
//        }, 3000);
//    }

    private void formValidation(){
//        Validate input
        if(TextUtils.isEmpty(loginEmail.getText())){
            errorMsg.setText(R.string.enter_email);
            errorMsg.setVisibility(View.VISIBLE);
            loginEmail.requestFocus();
            emailOutline.setBoxStrokeColor(Color.RED);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(loginEmail.getText()).matches()){
            errorMsg.setText(R.string.enter_valid_email);
            errorMsg.setVisibility(View.VISIBLE);
            emailOutline.setBoxStrokeColor(Color.RED);
            loginEmail.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(loginPassword.getText())){
            errorMsg.setText(R.string.enter_password);
            errorMsg.setVisibility(View.VISIBLE);
            passwordOutline.setBoxStrokeColor(Color.RED);
            loginPassword.requestFocus();
            return;
        }
        loginBtn.setClickable(false);
        progressContainer.setVisibility(View.VISIBLE);
        login(loginEmail.getText().toString(), loginPassword.getText().toString());
    }

    private void login(String email, String password){
        viewModel.signIn(this, email, password);
    }

    private final TextWatcher userEmailEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if(s != null && !TextUtils.isEmpty(s.toString()) && errorMsg.getVisibility() == View.VISIBLE){
                errorMsg.setVisibility(View.INVISIBLE);
                emailOutline.setBoxStrokeColor(getResources().getColor(R.color.teal_200, getTheme()));
            }
        }
    };

    private final TextWatcher userPasswordEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if(s != null && !TextUtils.isEmpty(s.toString()) && errorMsg.getVisibility() == View.VISIBLE){
                errorMsg.setVisibility(View.INVISIBLE);
                passwordOutline.setBoxStrokeColor(getResources().getColor(R.color.teal_200, getTheme()));
            }
        }
    };

    @Override
    protected void onDestroy() {
        isReady = false;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        isReady = false;
        finish();
        super.onBackPressed();
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
                viewModel.firebaseAuthWithGoogle(Login.this, account);
            } catch (ApiException e) {
                if (e.getStatusCode() != 12501){
                    Snackbar.make(google_btn, R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }
}
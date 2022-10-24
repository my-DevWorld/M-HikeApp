package com.example.m_hikeapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.m_hikeapp.R;
import com.example.m_hikeapp.utils.UISupport;
import com.example.m_hikeapp.viewmodels.SignInSignUpWithEmailViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class SignupWithEmail extends AppCompatActivity {
    private SignInSignUpWithEmailViewModel viewModel;
    private TextView loginHere, signupErrorMsg;
    private TextInputLayout signupUserFullName,emailOutline, passwordOutline;
    private TextInputEditText userFullName,signupEmail, loginPassword;
    private Button signupBtn;
    private FrameLayout progressContainer;
    private ImageView back;

    private UISupport uiSupport;

    @Override
    protected void onStart() {
        super.onStart();
        viewModel = new ViewModelProvider(this).get(SignInSignUpWithEmailViewModel.class);
        if (viewModel.isUserSignedIn()){
            startActivity(new Intent(SignupWithEmail.this, Home.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_with_email);
        init();
    }

    private void init(){
        uiSupport = new UISupport();
        loginHere = findViewById(R.id.signupToLogin);
        back = findViewById(R.id.back);
        signupUserFullName = findViewById(R.id.signupUserFullName);
        emailOutline = findViewById(R.id.emailOutline);
        passwordOutline = findViewById(R.id.passwordOutline);
        userFullName = findViewById(R.id.userFullName);
        signupEmail = findViewById(R.id.signupEmail);
        loginPassword = findViewById(R.id.loginPassword);
        signupErrorMsg = findViewById(R.id.signupErrorMsg);
        signupBtn = findViewById(R.id.signupBtn);
        progressContainer = findViewById(R.id.progressContainer);

//      Onclick events
        loginHere.setOnClickListener(v -> {
            onBackPressed();
        });
        back.setOnClickListener(v -> {
            startActivity(new Intent(this, Signup.class));
            finish();
        });
        signupBtn.setOnClickListener(v -> {
            uiSupport.hideSoftKeyboard(this, signupBtn);
            formValidation();
        });


        //-----TextWatchers for entry fields-----//
        userFullName.addTextChangedListener(userFullNameEditTextWatcher);
        signupEmail.addTextChangedListener(userEmailEditTextWatcher);
        loginPassword.addTextChangedListener(userPasswordEditTextWatcher);
    }

    private void formValidation(){
//        Validate input
        if(TextUtils.isEmpty(userFullName.getText())){
            signupErrorMsg.setText(R.string.full_name_is_required);
            signupErrorMsg.setVisibility(View.VISIBLE);
            userFullName.requestFocus();
            signupUserFullName.setBoxStrokeColor(Color.RED);
            return;
        }

        if(TextUtils.isEmpty(signupEmail.getText())){
            signupErrorMsg.setText(R.string.email_address_is_reqiured);
            signupErrorMsg.setVisibility(View.VISIBLE);
            signupEmail.requestFocus();
            emailOutline.setBoxStrokeColor(Color.RED);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(signupEmail.getText()).matches()){
            signupErrorMsg.setText(R.string.enter_valid_email);
            signupErrorMsg.setVisibility(View.VISIBLE);
            emailOutline.setBoxStrokeColor(Color.RED);
            signupEmail.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(loginPassword.getText())){
            signupErrorMsg.setText(R.string.creat_password);
            signupErrorMsg.setVisibility(View.VISIBLE);
            passwordOutline.setBoxStrokeColor(Color.RED);
            loginPassword.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(loginPassword.getText())){
            String passwordCount = loginPassword.getText().toString().trim();

            if(passwordCount.length() < 8){
                signupErrorMsg.setText(R.string.password_lenght);
                signupErrorMsg.setVisibility(View.VISIBLE);
                passwordOutline.setBoxStrokeColor(Color.RED);
                loginPassword.requestFocus();
                return;
            }
        }
        signupBtn.setClickable(false);
        progressContainer.setVisibility(View.VISIBLE);
        registerUser(signupEmail.getText().toString(), loginPassword.getText().toString());
    }

    private void registerUser(String email, String password){
        viewModel.createNewUser(this, email, password);
    }

    private final TextWatcher userFullNameEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if(s != null && !TextUtils.isEmpty(s.toString()) && signupErrorMsg.getVisibility() == View.VISIBLE){
                signupErrorMsg.setVisibility(View.INVISIBLE);
                signupUserFullName.setBoxStrokeColor(getResources().getColor(R.color.teal_200, getTheme()));
            }
        }
    };

    private final TextWatcher userEmailEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if(s != null && !TextUtils.isEmpty(s.toString()) && signupErrorMsg.getVisibility() == View.VISIBLE){
                signupErrorMsg.setVisibility(View.INVISIBLE);
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
            if(s != null && !TextUtils.isEmpty(s.toString()) && signupErrorMsg.getVisibility() == View.VISIBLE){
                signupErrorMsg.setVisibility(View.INVISIBLE);
                passwordOutline.setBoxStrokeColor(getResources().getColor(R.color.teal_200, getTheme()));
            }
        }
    };
}
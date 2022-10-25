package com.example.m_hikeapp.viewmodels;

import static android.content.ContentValues.TAG;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.example.m_hikeapp.R;
import com.example.m_hikeapp.utils.FireBaseAuth;
import com.example.m_hikeapp.views.Home;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInSignUpWithEmailViewModel extends AndroidViewModel {
    private static FirebaseAuth mAuth;
    private String errorMsg;
    private GoogleSignInClient signInClient;
    private GoogleSignInOptions signInOptions;


    public SignInSignUpWithEmailViewModel(@NonNull Application application) {
        super(application);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean isUserSignedIn(){
        // Check if user is signed in.
        return FireBaseAuth.isUserSignedIn();
    }

    public void createNewUser(Activity activity, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Signup success
                            FirebaseUser user = mAuth.getCurrentUser();
                            activity.findViewById(R.id.progressContainer).setVisibility(View.GONE);
                            Toast.makeText(activity, "Sign up successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(activity, Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(intent);
                            activity.finish();
                        } else {
                            // Signup failed
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }

                }).addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Signup failed
                        errorMsg = e.getMessage();
                        activity.findViewById(R.id.progressContainer).setVisibility(View.GONE);
                        Snackbar.make(activity.findViewById(R.id.signupBtn), errorMsg, Snackbar.LENGTH_SHORT).show();
                        activity.findViewById(R.id.signupBtn).setClickable(true);
                    }
                });
    }

    public void signIn(Activity activity, String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(activity, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        activity.findViewById(R.id.progressContainer).setVisibility(View.GONE);
//                      Sign in success.
//                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(activity, R.string.signin_successful, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }).addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                      sign in fails
                        activity.findViewById(R.id.progressContainer).setVisibility(View.GONE);
                        Snackbar.make(activity.findViewById(R.id.loginBtn), activity.getString(R.string.invalid_email_or_password), Snackbar.LENGTH_SHORT).show();
                        activity.findViewById(R.id.loginBtn).setClickable(true);
                    }
                });

    }

    private void getGoogleSignInRequest(Activity activity){
        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.web_client_id))
                .requestEmail()
                .requestId()
                .build();
        signInClient = GoogleSignIn.getClient(activity, signInOptions);

//        Check for previously sign in account
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);
        if(acct!=null){
            activity.startActivity(new Intent(activity, Home.class));
            activity.finish();
        }
    }

    public void startGoogleSignIn(Activity activity){
        getGoogleSignInRequest(activity);
        Intent signInIntent = signInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent,1000);
    }

    public void firebaseAuthWithGoogle(Activity activity, GoogleSignInAccount acct) {
////        Login or signup to firebase with GoogleSignInAccount
        AuthCredential credential = GoogleAuthProvider
                .getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(activity, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(activity.findViewById(R.id.google_btn) , R.string.signup_failed, Snackbar.LENGTH_SHORT);
            }
        });
    }
}

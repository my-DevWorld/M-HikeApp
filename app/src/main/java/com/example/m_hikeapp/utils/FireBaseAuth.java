package com.example.m_hikeapp.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseAuth {
    private static FirebaseAuth mAuth;

    public static boolean isUserSignedIn(){
        // Check if user is signed in.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            return true;
        }
        return false;
    }
}

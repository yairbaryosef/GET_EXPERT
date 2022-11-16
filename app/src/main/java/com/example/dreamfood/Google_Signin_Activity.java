package com.example.dreamfood;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class Google_Signin_Activity extends AppCompatActivity {
    GoogleSignInAccount account;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_signin);
        textView=findViewById(R.id.text);
        account= GoogleSignIn.getLastSignedInAccount(this);
        textView.setText(account.getEmail());
    }
}
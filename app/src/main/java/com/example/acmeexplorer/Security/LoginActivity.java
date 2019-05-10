package com.example.acmeexplorer.Security;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.acmeexplorer.MainActivity;
import com.example.acmeexplorer.R;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText email, password;
    Button btn_login;
    SignInButton btn_google;
    ProgressBar pbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pbar = findViewById(R.id.progressBarLoading);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        btn_login = findViewById(R.id.btn_login);
        btn_google = findViewById(R.id.btn_google);


        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        btn_google.setOnClickListener(listener -> {
            findViewById(R.id.progressBarLoading).setVisibility(View.VISIBLE);
            //signInWithGoogle();
        });

        btn_login.setOnClickListener(listener -> {
            findViewById(R.id.progressBarLoading).setVisibility(View.VISIBLE);
            signInWithUsernamePassword(email.getText().toString(), password.getText().toString());
        });

    }

    private void signInWithUsernamePassword(final String email, final String password) {
        if (email.equals("") || password.equals("")){
            Toast.makeText(LoginActivity.this, "El usuario y la contraseña no deben estar vacíos",
                    Toast.LENGTH_SHORT).show();
            updateUI(null);
        }else {
            Toast.makeText(LoginActivity.this, "Usuario: "+email +"- PASS: "+password,
                    Toast.LENGTH_SHORT).show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(LoginActivity.this, "Autenticación fallida.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser user){
        Log.e("ERROR", "User from firebase: "+user);
        if (user== null) {
            btn_google.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.VISIBLE);
            //findViewById(R.id.btn_register).setVisibility(View.VISIBLE);
            pbar.setVisibility(View.GONE);
        } if (user != null /*&& user.isEmailVerified()*/){
            Toast.makeText(this, "Usuario autenticado correctamente", Toast.LENGTH_SHORT).show();
            //Crashlytics.setUserIdentifier(user.getUid());
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            Toast.makeText(this, "La cuenta de correo no ha sido verificada", Toast.LENGTH_SHORT).show();
            pbar.setVisibility(View.GONE);
        }
    }

}

package com.example.acmeexplorer.Security;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.acmeexplorer.MainActivity;
import com.example.acmeexplorer.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0x1241;
    private static final int PERMISSION_REQUEST_STATE = 0x5123;
    private FirebaseAuth mAuth;

    EditText email, password;
    TextView link_sign_up;
    Button btn_login;
    SignInButton btn_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        link_sign_up = findViewById(R.id.link_signup);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        btn_login = findViewById(R.id.btn_login);
        btn_google = findViewById(R.id.btn_google);


        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        btn_google.setOnClickListener(listener -> {
            signInWithGoogle();
        });

        btn_login.setOnClickListener(listener -> {
            signInWithUsernamePassword(email.getText().toString(), password.getText().toString());
        });

        link_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            updateUI(currentUser);
        } else {
            updateUI(null);
        }
    }

    private void signInWithGoogle() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autenticando...");
        progressDialog.show();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client))
                .requestEmail()
                .build();
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        progressDialog.dismiss();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("ERROR", "Code: " + requestCode + " - Result: " + resultCode + " - Data: " + data.toString());
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("", "Google sign in failed", e);
            }
        } else {
            Snackbar.make(btn_google, "Autenticación fallida.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Snackbar.make(btn_google, "Autenticación fallida.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


    private void signInWithUsernamePassword(final String email, final String password) {

        if (!validate()) {
            Toast.makeText(LoginActivity.this, "Error de validación",
                    Toast.LENGTH_SHORT).show();
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Autenticando...");
            progressDialog.show();
            if (email.equals("") || password.equals("")) {
                Toast.makeText(LoginActivity.this, "El usuario y la contraseña no deben estar vacíos",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                updateUI(null);
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                progressDialog.dismiss();
                                updateUI(user);
                            } else {
                                Toast.makeText(LoginActivity.this, "Autenticación fallida.",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                updateUI(null);
                            }
                        });
            }
        }
    }

    private void updateUI(FirebaseUser user) {
        Log.e("ERROR", "User from firebase: " + user);
        if (user == null) {
            btn_google.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.VISIBLE);
        }
        if (user != null && user.isEmailVerified()) {
            Toast.makeText(this, "Usuario autenticado correctamente", Toast.LENGTH_SHORT).show();
            Crashlytics.setUserIdentifier(user.getUid());
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            /*if(!user.isEmailVerified()){
                Toast.makeText(this, "La cuenta de correo no ha sido verificada", Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    public boolean validate() {
        boolean valid = true;

        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("Introduce un email válido");
            valid = false;
        } else {
            this.email.setError(null);
        }

        if (password.isEmpty() || password.length() < 7 || password.length() > 10) {
            this.password.setError("Entre 7 y 10 caracteres");
            valid = false;
        } else {
            this.password.setError(null);
        }

        return valid;
    }

}

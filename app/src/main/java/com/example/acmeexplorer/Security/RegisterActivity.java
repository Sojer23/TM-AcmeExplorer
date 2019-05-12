package com.example.acmeexplorer.Security;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acmeexplorer.MainActivity;
import com.example.acmeexplorer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    TextView login_link;
    Button signup_btn;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        login_link = findViewById(R.id.link_login);
        signup_btn = findViewById(R.id.btn_signup);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);

        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(l);
            }
        });

        signup_btn.setOnClickListener(listener -> {

            createUserWithEmailPass(email.getText().toString(), password.getText().toString());
        });

    }

    private void createUserWithEmailPass(final String email, final String password) {

        if (!validate()) {
            Toast.makeText(RegisterActivity.this, "Error de validación",
                    Toast.LENGTH_SHORT).show();
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creando usuario...");
            progressDialog.show();

            if (email.equals("") || password.equals("")) {
                Toast.makeText(RegisterActivity.this, "El usuario y la contraseña no deben estar vacíos",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                updateUI(null);
            } else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            Log.e("ERROR", "task: " + task);
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                sendVerificationEmail();
                                progressDialog.dismiss();
                                updateUI(user);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registro fallido.",
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
            Toast.makeText(this, "Error en el registro. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
        }
        if (user != null) {
            Toast.makeText(this, "Usuario registrado correctamente. Revisa su cuenta de correo.", Toast.LENGTH_SHORT).show();
            //Crashlytics.setUserIdentifier(user.getUid());
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        } else {
            //Toast.makeText(this, "La cuenta de correo no ha sido verificada", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                        } else {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                        }
                    }
                });
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

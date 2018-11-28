package com.lincs.mobcare.authentication.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lincs.mobcare.R;
import com.lincs.mobcare.utils.Firebase;

import static android.content.ContentValues.TAG;

public class SignUpAccountActivity extends AppCompatActivity{

    private EditText email, password, confSenha;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_account);

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etSenha);
        confSenha = findViewById(R.id.etConfSenha);

        mAuth = Firebase.getAuth();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    finish();
                    Intent intent = new Intent(SignUpAccountActivity.this, SignUpAngelActivity.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        final Button nextButton = findViewById(R.id.btn_cadastrar);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            nextButton.setEnabled(false);

            if(!email.getText().toString().equals("") &&
                    !password.getText().toString().equals("") &&
                    !confSenha.getText().toString().equals("") &&
                    password.getText().toString().equals(confSenha.getText().toString())) {

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpAccountActivity.this, "Cadastro falhou! O email deve ser válido e a senha maior que 6 caracteres.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(SignUpAccountActivity.this, "Cadastro finalizado com sucesso!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else if(email.getText().toString().equals("")){
                Toast.makeText(getBaseContext(), "Digite o email", Toast.LENGTH_SHORT).show();
            }
            else if(password.getText().toString().equals("")){
                Toast.makeText(getBaseContext(), "Digite a senha", Toast.LENGTH_SHORT).show();
            }
            else if(confSenha.getText().toString().equals("")){
                Toast.makeText(getBaseContext(), "Digite a confirmação da senha", Toast.LENGTH_SHORT).show();
            }
            else if(!confSenha.getText().toString().equals(password.getText().toString())){
                Toast.makeText(getBaseContext(), "A senha deve ser igual a confirmação", Toast.LENGTH_SHORT).show();
            }
            }
        });
        nextButton.setEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

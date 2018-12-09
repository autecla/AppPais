package com.lincs.mobcare.authentication.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.lincs.mobcare.MainActivity;
import com.lincs.mobcare.R;
import com.lincs.mobcare.authentication.signup.SignUpAccountActivity;
import com.lincs.mobcare.authentication.signup.SignUpAngelActivity;
import com.lincs.mobcare.authentication.signup.SignUpCompanionActivity;
import com.lincs.mobcare.utils.Firebase;
import com.lincs.mobcare.utils.Snapshot;
import com.lincs.mobcare.utils.TesteFirebase;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class LoginAngelActivity extends AppCompatActivity {
    private EditText email, password;
    private Button recoveryButton;

    FrameLayout progressBarHolder;
    ProgressBar progressBar;
    TextView novoCadastro;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    Context context;
    private static final int MY_PERMISSIONS = 2;
    private static final int RC_SIGN_IN = 9001;
    String nome_acc,urlPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_angel);

        mAuth=FirebaseAuth.getInstance();
        context = this;

        email = findViewById(R.id.etName);
        password = findViewById(R.id.etSenha);

        progressBarHolder = findViewById(R.id.progressBarHolder);
        progressBar = findViewById(R.id.progressBar);

        recoveryButton = findViewById(R.id.btn_recuperar);
        recoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                recoveryButton.setEnabled(false);
                if(!email.getText().toString().equals("") &&  !password.getText().toString().equals("")) {

                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                                        Toast.makeText(LoginAngelActivity.this, "Login falhou!",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.w(TAG, "signInWithEmail:succeed", task.getException());
                                        Toast.makeText(LoginAngelActivity.this, "Login efetuado!",
                                                Toast.LENGTH_SHORT).show();
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
                recoveryButton.setEnabled(true);
            }
        });

        novoCadastro = findViewById(R.id.novoCadastro);
        novoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SignUpAccountActivity.class);
                startActivity(intent);
            }
        });


        mAuth = Firebase.getAuth();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent intent = new Intent(LoginAngelActivity.this, MainActivity.class);
                    Toast.makeText(LoginAngelActivity.this, "I'M IN!",
                            Toast.LENGTH_SHORT).show();
                    startActivity(intent);

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    email.setText(user.getEmail());
                    password.setText("********");

                    //carregar dados do usuário
                    progressBarHolder.setAlpha(0.4f);
                    progressBar.setVisibility(View.VISIBLE);

                    //disable user interaction
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Firebase.recoverFromUserUid(user.getUid(), new Runnable() {
                        @Override
                        public void run() {
                            String anjo_id = Snapshot.getAnjo_id();
                            if(anjo_id != null) {
                                Firebase.recover_anjo(anjo_id, new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBarHolder.setAlpha(1f);
                                        progressBar.setVisibility(View.GONE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        if(Snapshot.getAcompanhante() != null) {
                                            Intent intent = new Intent(LoginAngelActivity.this, MainActivity.class);
                                            Toast.makeText(LoginAngelActivity.this, "Login efetuado!", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        }
                                        else {
                                            Intent intent = new Intent(LoginAngelActivity.this, SignUpCompanionActivity.class);
                                            if (nome_acc!=null) {
                                                intent.putExtra("nome_acc", nome_acc);
                                            }
                                            if (urlPhoto!=null) {
                                                intent.putExtra("url_img", urlPhoto);
                                            }
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                            else{
                                progressBarHolder.setAlpha(1f);
                                progressBar.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                Intent intent = new Intent(LoginAngelActivity.this, SignUpAngelActivity.class);
                                //Intent intent = new Intent(LoginAngelActivity.this, MainActivity.class);
                                //Intent intent = new Intent(LoginAngelActivity.this, TesteFirebase.class);

                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        int permissionCheck = ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int permissionAudio= ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.RECORD_AUDIO);
        int permissionCamera=ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.CAMERA);
        int granted=PackageManager.PERMISSION_GRANTED;
        if (permissionCheck==granted && permissionAudio==granted && permissionCamera==granted){
            Log.d("ACESS_GRANTED","Acess granted!");
        }
        else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.CAMERA},MY_PERMISSIONS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("LOG_TAG", "Permission has been granted by user");
                }
                else {
                    Log.d("LOG_TAG", "Permission has been denied!");
                }
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginAngelActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.

                GoogleSignInAccount account = result.getSignInAccount();
                assert account != null;
                nome_acc=account.getDisplayName();
                urlPhoto= Objects.requireNonNull(account.getPhotoUrl()).toString();
                firebaseAuthWithGoogle(account);
                Toast.makeText(context, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                // Signed out, show unauthenticated UI.
                Toast.makeText(context, "Login falhou!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

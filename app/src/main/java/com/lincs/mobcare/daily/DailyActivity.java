package com.lincs.mobcare.daily;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lincs.mobcare.R;
import com.lincs.mobcare.authentication.login.LoginAngelActivity;
import com.lincs.mobcare.utils.Firebase;
import com.lincs.mobcare.utils.Snapshot;

import java.util.Objects;


public class DailyActivity extends AppCompatActivity {
    private static final String TAG = "DailyActivity: ";
    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ViewGroup viewGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        context = this;
        Toolbar toolbar =findViewById(R.id.toolbar);
        viewGroup=findViewById(android.R.id.content);

        TextView title = findViewById(R.id.toolbar_title);
        String nome[] = Snapshot.getAnjo().nome.split(" ");
        String diario="Diário de "+nome[0];
        if(diario.length()>21){
            title.setText(diario.substring(0,17).concat("..."));
        }
        else{
            title.setText(diario);
        }

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new DailyFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(1);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        Objects.requireNonNull(tabLayout.getTabAt(tabLayout.getTabCount() - 1)).select();


        mAuth = Firebase.getAuth();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(DailyActivity.this, LoginAngelActivity.class);
                    startActivity(intent);
                }
            }
        };
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.miOptions) {
            View menuItemView = findViewById(R.id.miOptions); // SAME ID AS MENU ID
            //Creating the instance of PopupMenu
            android.widget.PopupMenu popup = new android.widget.PopupMenu(DailyActivity.this, menuItemView);
            //Inflating the Popup using xml file
            popup.inflate(R.menu.popup_menu);

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.one){
                        Snapshot.logout(context);
                        finish();
                        Snapshot.reset();
                        ((Activity) context).finish();
                        Firebase.logout();
                    }
                    if(item.getItemId() == R.id.two){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle("Mudar senha");

                        String providerId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getProviderId();
                        if(!providerId.equals("facebook.com") && !providerId.equals("google.com")){
                            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            alertDialogBuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                            final AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.cyan_500));
                                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.cyan_500));
                                }
                            });
                            final View v = ((LayoutInflater) Objects.requireNonNull(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
                                    .inflate(R.layout.dialog_change_password, viewGroup,false);
                            alertDialog.setView(v);//add your own xml with defied with and height of videoview
                            alertDialog.setCancelable(false);
                            alertDialog.show();

                            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final EditText pass = v.findViewById(R.id.senhaAtual);
                                    final EditText newPass = v.findViewById(R.id.novaSenha);
                                    final EditText confNewPass = v.findViewById(R.id.confNovaSenha);

                                    if(!pass.getText().toString().equals("") &&
                                            !newPass.getText().toString().equals("") &&
                                            !confNewPass.getText().toString().equals("") &&
                                            newPass.getText().toString().equals(confNewPass.getText().toString())) {
                                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        // Get auth credentials from the user for re-authentication. The example below shows
                                        // email and password credentials but there are multiple possible providers,
                                        // such as GoogleAuthProvider or FacebookAuthProvider.
                                        assert user != null;
                                        AuthCredential credential = EmailAuthProvider
                                                .getCredential(Objects.requireNonNull(user.getEmail()), pass.getText().toString());
                                        // Prompt the user to re-provide their sign-in credentials
                                        user.reauthenticate(credential)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            user.updatePassword(newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Log.d(TAG, "Password updated");
                                                                        Toast.makeText(context, "A senha foi atualizada!", Toast.LENGTH_SHORT).show();
                                                                        alertDialog.dismiss();
                                                                    } else {
                                                                        Log.d(TAG, "Error password not updated");
                                                                        Toast.makeText(context, "Atualização de senha falhou!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            Log.d(TAG, "Error auth failed");
                                                            Toast.makeText(context, "Senha atual incorreta!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                    else if(!newPass.getText().toString().equals(confNewPass.getText().toString())){
                                        Toast.makeText(getBaseContext(), "A senha deve ser igual a confirmação", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(pass.getText().toString().equals("")){
                                        Toast.makeText(getBaseContext(), "Digite a senha", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(newPass.getText().toString().equals("")){
                                        Toast.makeText(getBaseContext(), "Digite a nova senha", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(confNewPass.getText().toString().equals("")){
                                        Toast.makeText(getBaseContext(), "Digite a confirmação da senha", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            alertDialogBuilder.setMessage("Não é possível alterar a senha da sua conta do "+providerId);

                            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.setCanceledOnTouchOutside(true);
                            alertDialog.show();

                        }
                    }
                    return true;
                }
            });
            popup.show();//showing popup menu
        }

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.lincs.mobcare;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
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
import android.widget.PopupMenu;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lincs.mobcare.authentication.login.LoginAngelActivity;
import com.lincs.mobcare.utils.Firebase;
import com.lincs.mobcare.utils.MyAlarm;
import com.lincs.mobcare.utils.Snapshot;

import java.util.Objects;

import io.fabric.sdk.android.Fabric;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private int[] imageResId = {
            R.drawable.ic_anjo_new,
            R.drawable.ic_date_range_white_new,
            R.drawable.ic_hospital,
            R.drawable.ic_notifications_white_new,
            R.drawable.ic_timer_black_new};

    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ViewGroup viewGroup;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        //create alarm to verify if daily activity was executed
        MyAlarm.createAlarm(this);



        final Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewPager viewPager = findViewById(R.id.viewpager);
        MainFragmentPagerAdapter mfpa = new MainFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mfpa);
        viewPager.addOnPageChangeListener(mfpa);

        tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(viewPager.getContext(), R.color.white);
                        assert tab.getIcon() != null;
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(viewPager.getContext(), R.color.cyan_300);
                        assert tab.getIcon() != null;
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

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
                    Intent intent = new Intent(MainActivity.this, LoginAngelActivity.class);
                    startActivity(intent);
                }
            }
        };
        Fabric.with(this, new Crashlytics());
        viewGroup =findViewById(android.R.id.content);
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
    public void onResume(){
        super.onResume();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setupTabIcons() {

        int tabIconColor;

        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(imageResId[0]);
        tabIconColor = ContextCompat.getColor(this, R.color.white);
        Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(0)).getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(imageResId[1]);
        tabIconColor = ContextCompat.getColor(this, R.color.cyan_300);
        Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(1)).getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(imageResId[2]);
        tabIconColor = ContextCompat.getColor(this, R.color.cyan_300);
        Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(2)).getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void resetTabIcons(){
        int tabIconColor;
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(imageResId[0]);
        tabIconColor = ContextCompat.getColor(this, R.color.white);
        Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(0)).getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(imageResId[1]);
        tabIconColor = ContextCompat.getColor(this, R.color.cyan_300);
        Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(1)).getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(imageResId[2]);
        tabIconColor = ContextCompat.getColor(this, R.color.cyan_300);
        Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(2)).getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

    }

    private void turnOffBluetooth(){

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.miOptions) {
            View menuItemView = findViewById(R.id.miOptions); // SAME ID AS MENU ID
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(this, menuItemView);
            //Inflating the Popup using xml file
            popup.inflate(R.menu.popup_menu);

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                            final View v = ((LayoutInflater) Objects.requireNonNull(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))).
                                    inflate(R.layout.dialog_change_password, viewGroup,false);
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
                                    final EditText newPass =v.findViewById(R.id.novaSenha);
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
                                                                        Toast.makeText(getBaseContext(), "A senha foi atualizada!", Toast.LENGTH_SHORT).show();
                                                                        alertDialog.dismiss();
                                                                    } else {
                                                                        Log.d(TAG, "Error password not updated");
                                                                        Toast.makeText(getBaseContext(), "Atualização de senha faltou!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            Log.d(TAG, "Error auth failed");
                                                            Toast.makeText(getBaseContext(), "Senha atual incorreta!", Toast.LENGTH_SHORT).show();
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Snapshot.save(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resetTabIcons();
        turnOffBluetooth();
    }

}
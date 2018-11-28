package com.lincs.mobcare.authentication.signup;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.lincs.mobcare.R;
import com.lincs.mobcare.authentication.login.LoginAngelActivity;
import com.lincs.mobcare.utils.CircleTransform;
import com.lincs.mobcare.utils.Firebase;
import com.lincs.mobcare.utils.LoadImg;
import com.lincs.mobcare.utils.ValidateName;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignUpCompanionActivity extends AppCompatActivity {
    private EditText name;
    private EditText phone;
    private Spinner spinner;
    private ImageView thumbnail;
    private String picturePath;
    private static final int RESULT_TAKE_PICTURE = 123;
    private static final int RESULT_PICK_PICTURE = 283;
    private static final int MY_PERMISSIONS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_companion);
        thumbnail=findViewById(R.id.thumbnail);
        name = findViewById(R.id.etName);
        phone = findViewById(R.id.etTelefone);

        Intent i=getIntent();
        String url_img=i.getStringExtra("url_img");
        String nome_acc=i.getStringExtra("nome_acc");

        if (nome_acc!=null) {
            name.setText(nome_acc);
        }
        if (url_img!=null) {
            LoadImg.loadImage(url_img,thumbnail,this);
            picturePath=url_img;
        }

        phone.addTextChangedListener(new TextWatcher() {
            boolean editedFlag;
            boolean backspacingFlag;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                backspacingFlag = count > after;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                String newText;
                String phoneString = text.replaceAll("[\\D]","");
                int phoneLength = phoneString.length();
                int cursor = 0;
                if(!editedFlag) {
                    editedFlag = true;

                    if(!backspacingFlag && phoneLength > 11) {
                        phoneString = phoneString.substring(0,phoneLength-1);
                        phoneLength = phoneString.length();
                    }

                    String underline = "___________";
                    phoneString = phoneString + underline.substring(0,11-phoneLength);
                    newText = "(" + phoneString.substring(0,2) + ") " + phoneString.substring(2,7) + "-" + phoneString.substring(7,11);
                    phone.setText(newText);

                    cursor++;
                    if(phoneLength > 2) cursor+=2;
                    if(phoneLength > 7) cursor++;
                    cursor+=phoneLength;
                    phone.setSelection(cursor);
                }
                else {
                    editedFlag = false;
                }
            }
        });

        spinner = findViewById(R.id.spinner_family);

        Button nextButton = findViewById(R.id.btn_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateName.validate(name.getText().toString())){
                    Toast.makeText(getBaseContext(), "Digite o nome corretamente!", Toast.LENGTH_SHORT).show();
                }
                else if(phone.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "Digite o telefone", Toast.LENGTH_SHORT).show();
                }
                else if(phone.getText().length() < 14) {
                    Toast.makeText(getBaseContext(), "Telefone inválido", Toast.LENGTH_SHORT).show();
                }
                else if(picturePath == null) {
                    Toast.makeText(getBaseContext(), "Selecione uma foto", Toast.LENGTH_SHORT).show();
                }
                else if (!ValidateName.validate(name.getText().toString())&&!phone.getText().toString().equals("")
                        &&picturePath != null&&phone.getText().length() >= 14){
                    Firebase.writeNewAcompanhante(spinner.getSelectedItem().toString(), name.getText().toString(), phone.getText().toString(), picturePath);
                    finish();
                    Intent intent = new Intent(view.getContext(), LoginAngelActivity.class);
                    intent.putExtra("fav", 0);
                    startActivity(intent);
                }
            }
        });

        //Tirar foto ou selecionar da galeria
        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(SignUpCompanionActivity.this,
                        Manifest.permission.CAMERA);
                if (permissionCheck==PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
                else {
                    ActivityCompat.requestPermissions(SignUpCompanionActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                            MY_PERMISSIONS);
                }
            }
        });

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(this, "Acesso negado!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_PICK_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            int columnIndex;
            if (cursor!=null){
                cursor.moveToFirst();
                columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
            }
            Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picturePath), 100, 100, false);
            ExifInterface exif = null;
            try {
                File pictureFile = new File(picturePath);
                exif = new ExifInterface(pictureFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ExifInterface.ORIENTATION_NORMAL;
            if (exif != null)
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateBitmap(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateBitmap(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateBitmap(bitmap, 270);
                    break;
            }

            //salva bitmap no cache
            File file = new File(getCacheDir(),"thumbnail_acomp.png");
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //exibe bitmap no imageView
            Picasso.with(getBaseContext()).invalidate(file.getPath());
            Picasso.with(getBaseContext()).load(file).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(thumbnail);
        }
        else if(requestCode == RESULT_TAKE_PICTURE && resultCode == RESULT_OK && null != data){

            Bundle extras = data.getExtras();
            Bitmap bitmap=null;
            if (extras!=null){bitmap = (Bitmap) extras.get("data");}
            File file=new File(getCacheDir(),"thumbnail_acomp.png");
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream=new FileOutputStream(file);
                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.picturePath = file.getPath();
            Log.d("Path",picturePath);

            //exibe bitmap no imageView
            Picasso.with(getBaseContext()).invalidate(picturePath);
            Picasso.with(getBaseContext()).load(file).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(thumbnail);
        }
    }

    private void dispatchTakePictureIntent() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Tirar Foto", "Galeria","Cancelar"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SignUpCompanionActivity.this);
                builder.setTitle("Selecione uma Opção:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Tirar Foto")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent,RESULT_TAKE_PICTURE );
                        } else if (options[item].equals("Galeria")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, RESULT_PICK_PICTURE);
                        } else if (options[item].equals("Cancelar")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}

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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lincs.mobcare.R;
import com.lincs.mobcare.utils.CircleTransform;
import com.lincs.mobcare.utils.Firebase;
import com.lincs.mobcare.utils.LoadImg;
import com.lincs.mobcare.utils.ValidateName;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class SignUpAngelActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText dateTextView, prontuarioTextView;
    private DatePickerDialog dpd;
    private EditText name;
    private RadioButton radioButton;
    private EditText cidade;
    private ImageView img_thumbnail;
    private String picturePath;
    private ProgressBar progressBar;
    private FrameLayout progressBarHolder;
    private static final int RESULT_TAKE_PICTURE = 123;
    private static final int RESULT_PICK_PICTURE = 283;
    private static final int MY_PERMISSIONS = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_angel);
        img_thumbnail=findViewById(R.id.thumbnail_anjo);
        name = findViewById(R.id.etName);
        dateTextView =findViewById(R.id.dateTextView);
        prontuarioTextView = findViewById(R.id.prontuarioTextView);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioButton =  findViewById(radioGroup.getCheckedRadioButtonId());
        cidade=findViewById(R.id.angel_city);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            //int index = radioGroup.indexOfChild(findViewById(i));
        radioButton =  findViewById(i);
            }
        });
        progressBar=findViewById(R.id.progressBarSignIn);
        progressBarHolder=findViewById(R.id.progressBarHolderSingIn);

        Intent i=getIntent();
        final String url_img=i.getStringExtra("url_img");
        final String nome_acc=i.getStringExtra("nome_acc");

        if (nome_acc!=null) {
            name.setText(nome_acc);
        }
        if (url_img!=null) {
            LoadImg.loadImage(url_img,img_thumbnail,this);
            picturePath=url_img;
        }

        cidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cidade.getText().toString().equalsIgnoreCase("Cidade")){
                    cidade.setText("");
                }
            }
        });

        img_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(SignUpAngelActivity.this,
                        Manifest.permission.CAMERA);
                if (permissionCheck== PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
                else {
                    ActivityCompat.requestPermissions(SignUpAngelActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                            MY_PERMISSIONS);
                }
            }
        });
        Button nextButton = findViewById(R.id.btn_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final boolean[] existe = new boolean[1];
                Firebase.existe_prontuario(prontuarioTextView.getText().toString(), new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        progressBarHolder.setAlpha(0.4f);
                        progressBar.setVisibility(View.VISIBLE);
                        //disable user interaction
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        existe[0] = Firebase.getExiste_pront();

                        if (existe[0]){
                            Toast.makeText(getBaseContext(), "Número do prontuário já cadastrado!", Toast.LENGTH_LONG).show();
                            progressBarHolder.setAlpha(1f);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Firebase.setExiste_pront(false);
                        }
                        if(!existe[0] &&!ValidateName.validate(name.getText().toString()) &&!dateTextView.getText().toString().equals("") &&
                                !prontuarioTextView.getText().toString().equals("")&&!cidade.getText().toString().equals("")&&
                                picturePath!=null) {
                            Firebase.writeNewAnjo(Firebase.getUserUid(), name.getText().toString(), radioButton.getText().toString(),
                                    dateTextView.getText().toString(),cidade.getText().toString(),prontuarioTextView.getText().toString(),picturePath);

                            progressBarHolder.setAlpha(1f);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            Intent intent = new Intent(view.getContext(), SignUpCompanionActivity.class);
                            intent.putExtra("nome_acc",nome_acc);
                            intent.putExtra("url_img",url_img);
                            startActivity(intent);
                        }
                        if(ValidateName.validate(name.getText().toString())){
                            progressBarHolder.setAlpha(1f);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getBaseContext(), "Digite o nome corretamente!", Toast.LENGTH_SHORT).show();
                        }
                        else if(dateTextView.getText().toString().equals("")){
                            progressBarHolder.setAlpha(1f);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getBaseContext(), "Digite a data de nascimento!", Toast.LENGTH_SHORT).show();
                        }
                        else if(cidade.getText().toString().equals("")){
                            progressBarHolder.setAlpha(1f);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getBaseContext(), "Digite o nome da cidade!", Toast.LENGTH_SHORT).show();
                        }
                        else if(prontuarioTextView.getText().toString().equals("")){
                            progressBarHolder.setAlpha(1f);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getBaseContext(), "Digite número do prontuário!", Toast.LENGTH_SHORT).show();
                        }
                        else if(picturePath.equals("")) {
                            progressBarHolder.setAlpha(1f);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getBaseContext(), "Tire ou escolha uma foto!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loadDatePicker();
    }

    private void loadDatePicker() {
        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
            this,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        );

        dateTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
            if (b) {
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
            }
        });
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            dpd.show(getFragmentManager(), "Datepickerdialog");
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        dateTextView.setText(date);
    }

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
            android.support.media.ExifInterface exif = null;
            try {
                File pictureFile = new File(picturePath);
                exif = new android.support.media.ExifInterface(pictureFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = android.support.media.ExifInterface.ORIENTATION_NORMAL;
            if (exif != null)
                orientation = exif.getAttributeInt(android.support.media.ExifInterface.TAG_ORIENTATION, android.support.media.ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case android.support.media.ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateBitmap(bitmap, 90);
                    break;
                case android.support.media.ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateBitmap(bitmap, 180);
                    break;

                case android.support.media.ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateBitmap(bitmap, 270);
                    break;
            }

            //salva bitmap no cache
            File file = new File(getCacheDir(),"foto_anjo.png");
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
                    .networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(img_thumbnail);
        }
        else if(requestCode == RESULT_TAKE_PICTURE && resultCode == RESULT_OK && null != data){

            Bundle extras = data.getExtras();
            Bitmap bitmap=null;
            if (extras!=null){bitmap = (Bitmap) extras.get("data");}
            File file=new File(getCacheDir(),"foto_anjo.png");
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
                    .networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(img_thumbnail);
        }
    }

    private void dispatchTakePictureIntent() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Tirar Foto", "Galeria","Cancelar"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SignUpAngelActivity.this);
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

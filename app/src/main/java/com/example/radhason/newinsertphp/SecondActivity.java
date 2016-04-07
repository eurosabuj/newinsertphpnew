package com.example.radhason.newinsertphp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by radhason on 11/29/15.
 */
public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String UPLOAD_URL = "http://radhason.comxa.com/insert.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";

    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;

    private Bitmap bitmap;


    String fullName,emailAddress,description,phoneNumber,spinner;
    static int TAKE_PICTURE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
//        buttonView = (Button) findViewById(R.id.buttonViewImage);

        imageView = (ImageView) findViewById(R.id.imageView);

         buttonChoose.setOnClickListener(this);
//         buttonUpload.setOnClickListener(this);



    }

    public void upLoad(View view){
        Intent intent = getIntent();
        fullName = intent.getStringExtra("name");
        emailAddress=intent.getStringExtra("email");
        phoneNumber=intent.getStringExtra("phone");

        description=intent.getStringExtra("description");
        spinner=intent.getStringExtra("spinner");
Log.d("spinnervalue",""+spinner);

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String imageTab= Base64.encodeToString(b, Base64.DEFAULT);
        Toast.makeText(this, "Data inserting...", Toast.LENGTH_SHORT).show();
        new InsertActivity(this).execute(fullName, description, phoneNumber, emailAddress,spinner);
    }
public void cancelInsert(View view){

    Intent intent = new Intent(SecondActivity.this,WainActivity.class);
    startActivity(intent);
}
    private void showFileChooser() {
//        Intent intent = new Intent();
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // start camera activity
        startActivityForResult(intent1, TAKE_PICTURE);
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK &&  intent != null) {

            Bundle extras = intent.getExtras();

            // get bitmap
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
            }

    }





    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
//        if(v == buttonUpload){
//            uploadImage();
//        }
    }
}

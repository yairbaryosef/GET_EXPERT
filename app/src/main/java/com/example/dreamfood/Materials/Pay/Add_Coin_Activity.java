package com.example.dreamfood.Materials.Pay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Coin;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.R;
import com.example.dreamfood.Teacher_Controller.teacher_layout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

public class Add_Coin_Activity extends AppCompatActivity implements View.OnClickListener {
   Teacher teacher=new Teacher();
   Strings con=new Strings();
   Gson gson=new Gson();
   CircleImageView image;
   Button save;
    TextInputEditText price,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coin);
        String json=getIntent().getStringExtra(con.teacher);
        teacher=gson.fromJson(json,Teacher.class);
        initWidgets();
    }

    private void initWidgets() {
        name=findViewById(R.id.name);
        price=findViewById(R.id.price);
        image=findViewById(R.id.image);
        image.setOnClickListener(this);
        save=findViewById(R.id.save);
        save.setOnClickListener(this);
    }
    Uri uri;
    boolean Is_Photo_Exsist=false;
    private static final int PICK_IM=1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IM&&resultCode==RESULT_OK){
            try {
                uri=data.getData();
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                image.setImageBitmap(bitmap);
                Is_Photo_Exsist=true;

            }
            catch (Exception e){

            }
        }
    }
    @Override
    public void onClick(View v) {
        if(v==image){
            Intent gallery=new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery,"Select picture"),PICK_IM);

        }
        if(v==save){
            if(Is_Photo_Exsist){

            }
            else{
                Coin coin=new Coin();
                try {


                    coin.cost = Double.valueOf(price.getText().toString());
                    String email=getSharedPreferences("email",0).getString("email",null);
                    coin.email=email;
                    coin.url="";
                    coin.name=name.getText().toString();
                    teacher.coins.add(coin);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(con.teacher).child(email);
                    databaseReference.setValue(teacher);
           databaseReference = FirebaseDatabase.getInstance().getReference(con.coin).child(email).child(coin.name);
                    databaseReference.setValue(coin);
                    Intent intent=new Intent(this, teacher_layout.class);
                    startActivity(intent);

                }
                catch (Exception e){
                    Toast.makeText(this, "price is not legal", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void processupload(Uri filepath)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        String email=getSharedPreferences("email",0).getString("email",null);
        final StorageReference reference=storageReference.child("image/"+"coins"+"/"+email+"/"+System.currentTimeMillis()+".png");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                            }


                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        pd.setMessage("Uploaded :"+(int)percent+"%");
                    }
                });
    }
}
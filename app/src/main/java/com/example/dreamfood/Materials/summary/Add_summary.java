package com.example.dreamfood.Materials.summary;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.summary.Summary;
import com.example.dreamfood.BusinessLayer.summary.Summary_Adapter;
import com.example.dreamfood.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Add_summary extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    Button back,add_file,save;
    TextView text;
    String summary_name="";
    ListView list;
    summaries summaries=new summaries();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_summary);
        summary_name=getSharedPreferences("sum",0).getString("sum","");
     initWidgets();
     Add_Summary();
    }

    EditText name_sum;
    Button add_sum;
  TextView textView;
    public void Add_Summary(){
        d=new Dialog(this);
        d.setContentView(R.layout.add_recording);
        name_sum=d.findViewById(R.id.name);
        add_sum=d.findViewById(R.id.add);
        add_sum.setOnClickListener(this);
       textView= d.findViewById(R.id.textView);
        textView.setText(" add summary ");
        d.show();
    }
    public void initWidgets(){
        back=findViewById(R.id.back);
        save=findViewById(R.id.save);
        save.setOnClickListener(this);
        text=findViewById(R.id.text);
        list=findViewById(R.id.list);
        back.setOnClickListener(this);
       add_file=findViewById(R.id.add_file);
       add_file.setOnClickListener(this);


    }
Summary summary=new Summary();
    @Override
    public void onClick(View v) {
        if(v==save){
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(cons.summary).child(email).child(summary_name);
            databaseReference.setValue(summaries);
        }
        if(v==add_sum){
            if(name_sum.getText().length()>0){
                    d.dismiss();
                    summary_name=name_sum.getText().toString();
                SharedPreferences sp=getSharedPreferences("sum",0);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("email",summary_name);
                editor.commit();
            }
            else{
                Toast.makeText(this, "enter name", Toast.LENGTH_SHORT).show();
            }
        }
        if(v==back){
            finish();
        }
        if(v==add_file){
            Add_pdf_Dialog();
        }
        if(v==upload){
            Dexter.withContext(getApplicationContext())
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            Intent intent=new Intent();
                            intent.setType("application/pdf");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent,"Select Pdf Files"),101);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();

        }
        if(v==image){

        }
        if(v==add){
            d.dismiss();
            processupload(Uri.parse(filepath));




        }
    }
    Uri uri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101 && resultCode==RESULT_OK)
        {
             filepath=data.getData().toString();

        }
    }
    String filepath="";
    Dialog d;
    Button upload,add;
    ImageButton image;
    TextView check;
    public void Add_pdf_Dialog(){
        d=new Dialog(this);
        d.setContentView(R.layout.upload_pdf);
        upload=d.findViewById(R.id.upload);
        add=d.findViewById(R.id.add_file);
        check=d.findViewById(R.id.upload_check);
        image=d.findViewById(R.id.summary_pick);
       add.setOnClickListener(this);
       upload.setOnClickListener(this);
       image.setOnClickListener(this);
        d.show();
    }
    int i=1;
    public void processupload(Uri filepath)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();

        final StorageReference reference= FirebaseStorage.getInstance().getReference().child("summary/"+summary_name+"/"+String.valueOf(i)+".pdf");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                summary=new Summary();
                                summary.url=uri.toString();
                                email=getSharedPreferences("email",0).getString("email",null);
                                summary.email=email;
                                summaries.summaries.add(summary);
                                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(cons.summary);
                                databaseReference.child(email).child(summary_name).child(String.valueOf(i)).setValue(summary);
                                i++;
                                pd.dismiss();
                                Summary_Adapter summary_adapter=new Summary_Adapter(Add_summary.this,summaries.summaries);
                                list.setAdapter(summary_adapter);

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
    String email;
    Strings cons=new Strings();
     Summary getSummary=new Summary();
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getSummary=summaries.summaries.get(position);
        showTest(getSummary.url);
    }
    public class Retrieve extends AsyncTask<String,Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream=null;
            try{
                URL url=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode()==200){
                    inputStream=new BufferedInputStream(urlConnection.getInputStream());
                }
            }
            catch (Exception e){
                return  null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdf.fromStream(inputStream).load();
        }
    }
    PDFView pdf;
    public void showTest(String url) {
        d = new Dialog(this);
        d.setContentView(R.layout.pdf);
        pdf = (PDFView) d.findViewById(R.id.pdfView);
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        new Retrieve().execute(url);
        d.show();
    }
}
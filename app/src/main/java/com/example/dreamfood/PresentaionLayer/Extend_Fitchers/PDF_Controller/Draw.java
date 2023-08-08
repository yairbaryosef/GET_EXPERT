package com.example.dreamfood.PresentaionLayer.Extend_Fitchers.PDF_Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import com.example.dreamfood.R;
import com.kyanogen.signatureview.SignatureView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Draw extends AppCompatActivity implements View.OnClickListener {
    SignatureView signatureView;
    Button download,erase,pen;
    Bitmap bitmap;
    SeekBar seekBar;
    int defColor;
    File path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        seekBar=findViewById(R.id.seekbar);
        signatureView = findViewById(R.id.signature_view);
        download=(Button) findViewById(R.id.downloud);
        download.setOnClickListener(this);
        pen=findViewById(R.id.draw);
        erase=findViewById(R.id.erase);
        defColor=ContextCompat.getColor(this,R.color.white);
        pen.setOnClickListener(this);

        signatureView.setPenColor(defColor);
  path=new File(Environment.getExternalStorageDirectory().getAbsolutePath());

askPremission();
    }

    private void askPremission() {

    }
    private void openColorPicker(){
AmbilWarnaDialog ambilWarnaDialog=new AmbilWarnaDialog(this, defColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
    @Override
    public void onCancel(AmbilWarnaDialog dialog) {

    }

    @Override
    public void onOk(AmbilWarnaDialog dialog, int color) {
        defColor=color;
        signatureView.setPenColor(color);
    }

});
ambilWarnaDialog.show();
    }
    @Override
    public void onClick(View v) {
        if(v==pen){
            openColorPicker();
        }
       if(v==download) {
           SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

           String filename=path+"/" +format.format(new Date())+".png";
           File file=new File(filename);
          bitmap=signatureView.getSignatureBitmap();
           ByteArrayOutputStream bos=new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.PNG,0,bos);
           byte[] bytes=bos.toByteArray();
           try {
               FileOutputStream fos=new FileOutputStream(file);
               fos.write(bytes);
               fos.flush();
               fos.close();
               Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }


}
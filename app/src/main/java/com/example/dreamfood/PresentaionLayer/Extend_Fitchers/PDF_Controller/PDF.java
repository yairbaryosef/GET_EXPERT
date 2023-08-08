package com.example.dreamfood.PresentaionLayer.Extend_Fitchers.PDF_Controller;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PDF extends AppCompatActivity implements View.OnClickListener {
PDFView pdf;

Button downloud;
String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdf=(PDFView) findViewById(R.id.pdfView);
        String url=getIntent().getStringExtra("url");
        URL=url;
        downloud=findViewById(R.id.download);
        downloud.setOnClickListener(this);
        new Retrieve().execute(url);
    }

    @Override
    public void onClick(View v) {
        if(v==downloud){

           createMyPDF();
        }
    }
   public InputStream input;
    class Retrieve extends AsyncTask<String,Void, InputStream>{
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
            input=inputStream;
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdf.fromStream(inputStream).load();
        }

    }
    public void createMyPDF(){
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(URL));
        request.setTitle(URL);
        request.setMimeType("application/pdf");
        request.allowScanningByMediaScanner();
        request.setAllowedOverMetered(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,URL);
        DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
        Toast.makeText(this, "in", Toast.LENGTH_SHORT).show();

    }
}
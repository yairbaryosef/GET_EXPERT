package com.example.dreamfood.BusinessLayer.summary;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Summary_Adapter extends ArrayAdapter<Summary> {
    public Summary_Adapter(@NonNull Context context, ArrayList<Summary> arrayList) {
        super(context, R.layout.pdf_list_item_for_summary,R.id.text3, arrayList);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Summary summary=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pdf_list_item_for_summary, parent, false);
        }
        ImageButton imageButton=convertView.findViewById(R.id.image);
        TextView file=convertView.findViewById(R.id.file);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==imageButton){
                    showTest(summary.url);
                }
            }
        });



        return super.getView(position,convertView, parent);
    }
    Dialog d;
    PDFView pdf;
    public void showTest(String filepath) {
        d = new Dialog(getContext());
        d.setContentView(R.layout.pdf);
        pdf = (PDFView) d.findViewById(R.id.pdfView);
        new Retrieve().execute(filepath);
        Toast.makeText(getContext(), filepath, Toast.LENGTH_SHORT).show();
        d.show();
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
}

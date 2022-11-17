package com.example.dreamfood.Materials.Quiz;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Question;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.Dialogs.Question_Answers;
import com.example.dreamfood.Materials.Quiz.Adapters.Add_Questions_To_Quiz_Adapterextends;
import com.example.dreamfood.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Add_Quiz extends AppCompatActivity implements View.OnClickListener {
  ListView list;
  Quiz quiz=new Quiz();
  Button add,restart,save,picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);
        initViews();
    }
    Dialog d;
    ImageButton add_picture;
    Uri uri;
    int pos;
    public void choose_q(){
        d=new Dialog(this);
        d.setContentView(R.layout.list_rating_student_active);
       ListView list=d.findViewById(R.id.list);
       ArrayList<String> qs=new ArrayList<>();
       int i=0;
       for(Question q:questions){
           try {


               qs.add(q.Question+" question number"+String.valueOf(i));
           }
           catch (Exception e){
               qs.add("question number"+String.valueOf(i));
           }
           i++;
       }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,qs );
       list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                d.dismiss();
                Add_Picture_Dialog();
                pos=position;

            }
        });
       Button button=d.findViewById(R.id.save);
       button.setText("update picture");
     button.setVisibility(View.INVISIBLE);
     button.setHeight(0);

        d.show();
    }
    public  void Add_Picture_Dialog(){
        d=new Dialog(this);
        d.setContentView(R.layout.image_button);
        add_picture=d.findViewById(R.id.add_picture);
        add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==add_picture){
                    Intent gallery=new Intent();
                    gallery.setType("image/*");
                    gallery.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(gallery,1);
                }
            }
        });

        d.show();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK){
            try {
                uri=data.getData();
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                add_picture.setImageBitmap(bitmap);

                processupload(uri,questions.get(pos));
                 d.dismiss();
            }
            catch (Exception e){

            }
        }
    }
    int i=0;
    public void processupload(Uri filepath,Question question)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        String email=getSharedPreferences("email",0).getString("email",null);
        final StorageReference reference=storageReference.child("image/"+"quiz/"+email+"/"+String.valueOf(i)+"/"+System.currentTimeMillis()+".png");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                question.setImage_url(uri.toString());
                                i++;
                                SharedPreferences sharedPreferences=getSharedPreferences("delete",0);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                Strings con=new Strings();
                                editor.putString(con.Quiz+" "+quiz.email+quiz.type,"delete");
                                pd.dismiss();




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

    private void initViews() {
        add=findViewById(R.id.add);
        list=findViewById(R.id.list);
        add.setOnClickListener(this);
        restart=findViewById(R.id.restart);
        restart.setOnClickListener(this);
        picture=findViewById(R.id.picture);
        picture.setOnClickListener(this);
        save=findViewById(R.id.save);
        save.setOnClickListener(this);
    }
   ArrayList<Question> questions;
    @Override
    public void onClick(View v) {
        if(v==picture){
            choose_q();
        }
        if(v==restart){
            quiz.questions=new ArrayList<>();
            quiz.Oquestions=new ArrayList<>();
            questions=new ArrayList<>();
            Add_Questions_To_Quiz_Adapterextends add_questions_to_quiz_adapterextends=new Add_Questions_To_Quiz_Adapterextends(this,questions,quiz);
            list.setAdapter(add_questions_to_quiz_adapterextends);

        }

        if(v==add){
            Question_Answers question_answers=new Question_Answers(this);
            question_answers.Choose_Q_type(quiz);
            questions=new ArrayList<>();
            questions.addAll(quiz.questions);
            questions.addAll(quiz.Oquestions);
            Add_Questions_To_Quiz_Adapterextends add_questions_to_quiz_adapterextends=new Add_Questions_To_Quiz_Adapterextends(this,questions,quiz);
            list.setAdapter(add_questions_to_quiz_adapterextends);

        }
        if(v==save){
            Intent intent=new Intent(this,Open_Quiz.class);
            Gson gson=new Gson();
            String json=gson.toJson(quiz);
            intent.putExtra("quiz",json);
            startActivity(intent);

        }
    }
}
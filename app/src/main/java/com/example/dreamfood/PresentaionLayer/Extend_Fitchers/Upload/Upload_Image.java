package com.example.dreamfood.PresentaionLayer.Extend_Fitchers.Upload;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Question;
import com.example.dreamfood.PresentaionLayer.Materials.Quiz.Add_Quiz;

public class Upload_Image extends AppCompatActivity {
    private static final int PICK_IM=1;
    private Context context;
    ImageView imageButton;
    Question question;

    public Upload_Image(Context context, ImageView imageButton, Question question){
       this.context=context;
       this.imageButton=imageButton;
        this.question=question;
        Add_Quiz add_quiz=new Add_Quiz();

    }

    Dialog d;
    ImageButton add_picture;

    Uri uri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IM&&resultCode==RESULT_OK){
            try {
                uri=data.getData();
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imageButton.setImageBitmap(bitmap);
                question.setImage_url(uri.toString());

            }
            catch (Exception e){

            }
        }
    }
}

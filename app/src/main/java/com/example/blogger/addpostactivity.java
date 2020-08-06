package com.example.blogger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blogger.R;
import com.example.blogger.blogupload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class addpostactivity extends AppCompatActivity
{

    private static final int MAX_LENGTH = 23 ;
    ProgressBar mProgress;
    ImageView selectimage;
    EditText posttitle,postdesc;
    Button submitbtn;
    private Uri uri=null;
    private static final int GALLERY_INTENT=2;
    private StorageReference mStorage;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT&&resultCode==RESULT_OK)
        {
            uri=data.getData();
            Picasso.get().load(uri).into(selectimage);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpostactivity);

        selectimage=(ImageView) findViewById(R.id.selectimage);
        posttitle=(EditText) findViewById(R.id.posttitle);
        postdesc=(EditText) findViewById(R.id.postdesc);

        submitbtn=(Button) findViewById(R.id.submitbtn);

        mProgress= new ProgressBar(this);

        mDataBase= FirebaseDatabase.getInstance().getReference().child("blog");


        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startposting();

            }
        });

        mStorage= FirebaseStorage.getInstance().getReference();
        selectimage.setVisibility(View.VISIBLE);


        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Intent.ACTION_PICK);
                intent.setType("image/w");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });




    }


    private void startposting()
    {

        mProgress.setProgress(0);
        mProgress.setVisibility(View.VISIBLE);

        final String title_value=posttitle.getText().toString().trim();
        final String desc_value=postdesc.getText().toString().trim();
        if(!TextUtils.isEmpty(title_value)  &&  !TextUtils.isEmpty(desc_value)  &&  uri!=null)
        {
            StorageReference filepath=mStorage.child("blog_image").child(uri.getLastPathSegment());
            //adding a picture to storage

            filepath.putFile(uri);
             filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri)
                {
                    DatabaseReference newPost=mDataBase.push();
                    //newPost.child("uid").setValue(mAuth.getCurrentUser().getUid());
                    newPost.child("title").setValue(title_value);
                    newPost.child("desc").setValue(desc_value);
                    newPost.child("image").setValue(uri.toString());
                    Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), blogupload.class);
                    startActivity(intent);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

        }





    }

}
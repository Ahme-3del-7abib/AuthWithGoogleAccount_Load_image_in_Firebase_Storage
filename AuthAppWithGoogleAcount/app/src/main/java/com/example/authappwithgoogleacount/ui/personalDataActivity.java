package com.example.authappwithgoogleacount.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.authappwithgoogleacount.R;
import com.example.authappwithgoogleacount.databinding.ActivityPersonalDataBinding;
import com.example.authappwithgoogleacount.pojo.profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class personalDataActivity extends AppCompatActivity {

    private ActivityPersonalDataBinding binding;
    private Uri img_uri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        setDataView();

        binding.imgPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 200);
            }
        });

        binding.getImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageInView();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 200 && data != null) {

            Uri imagerUri = data.getData();
            binding.imgPerson.setImageURI(imagerUri);
            img_uri = imagerUri;
        }
    }

    private void setDataView() {

        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_data);

        Intent intent = getIntent();
        profile p = (profile) intent.getSerializableExtra("object");

        binding.DataTv.setText("مرحبا : " + p.getName() + "\n" + "\n" + "بريدك الالكتروني :" + p.getEmail());

        try {
            String PhotoUrl = p.getImg();
            Picasso.with(personalDataActivity.this).load(PhotoUrl).into(binding.imgPerson);
        } catch (Exception e) {
        }
    }

    private void setImageInView() {

        Calendar calendar = Calendar.getInstance();
        progressDialog.setMessage("انتظر رفع الصورة .....");
        progressDialog.show();

        StorageReference filepath = storageReference.child("AHMED2020").child("img_" + calendar.getTimeInMillis());

        filepath.putFile(img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                progressDialog.dismiss();
                Toast.makeText(personalDataActivity.this, "تم رفع الصورة", Toast.LENGTH_SHORT).show();

                Picasso.with(getApplicationContext()).load(taskSnapshot.getDownloadUrl()).into(binding.img);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(personalDataActivity.this, "يوجد خطأ لم يتم رفع الصورة", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
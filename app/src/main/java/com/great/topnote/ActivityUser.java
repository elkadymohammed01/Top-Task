package com.great.topnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.great.topnote.data.DownloadFile;
import com.great.topnote.data.myDbAdapter;
import java.io.File;
import java.io.IOException;

public class ActivityUser extends AppCompatActivity {
    TextView user_name, id_user;
    ImageView image ,background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setId();
    }
    private void setId(){
        user_name=findViewById(R.id.text_name);
        id_user=findViewById(R.id.id);
        image=findViewById(R.id.user);
        background=findViewById(R.id.imageView6);
        setUserInformation();

    }
    myDbAdapter DbAdapter;
    private void setUserInformation() {
        DbAdapter =new myDbAdapter(this);
        user_name.setText(DbAdapter.getUserData_inf()[0]);
        id_user.setText(DbAdapter.getUserData_inf()[1].substring(0,DbAdapter.getUserData_inf()[1].length()-4));

        DownloadFile file=new DownloadFile();
        if(file.getLocalFile(DbAdapter.getUserData_inf()[1]+"").exists()){
            String imageUri = file.getLocalFile(DbAdapter.getUserData_inf()[1]+"").getPath();

            Bitmap myBitmap = BitmapFactory.decodeFile(new File(imageUri).getAbsolutePath());

            image.setImageBitmap(myBitmap);
        }else{
            try {
                file.setLocalFile(DbAdapter.getUserData_inf()[1]+"",getApplicationContext(),image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(file.getLocalFile("background"+DbAdapter.getUserData_inf()[1]+"").exists()){
            String imageUri = file.getLocalFile("background"+DbAdapter.getUserData_inf()[1]+"").getPath();

            Bitmap myBitmap = BitmapFactory.decodeFile(new File(imageUri).getAbsolutePath());

            background.setImageBitmap(myBitmap);
        }else{
            try {
                file.setLocalFile("background"+DbAdapter.getUserData_inf()[1]+"",getApplicationContext(),background);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void images() {
        get_data(new String[]{"image/*"});
    }

    void get_data(String[] lop){
        try {
            String[] mimeTypes =
                    lop;

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                if (mimeTypes.length > 0) {
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }
            } else {
                String mimeTypesStr = "";
                for (String mimeType : mimeTypes) {
                    mimeTypesStr += mimeType + "|";
                }
                intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
            }
            startActivityForResult(Intent.createChooser(intent,"ChooseFile"), 100);

        }
        catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private Context getContext() {
        return this;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            if (resultCode == Activity.RESULT_OK) {

                //             textTargetUri.setText(targetUri.toString());
                Uri targetUri = data.getData();

                if(show==0){
                    im = targetUri;
                    Glide.with(getContext()).load(im).into(image);
                    upload_file(DbAdapter.getUserData_inf()[1],im);
                }
                else{
                    im = targetUri;
                    Glide.with(getContext()).load(im).into(background);
                    upload_file("background"+DbAdapter.getUserData_inf()[1],im);
                }

            }
        }
    }
    int show =0;
    Uri im;
    private void upload_file(String path, Uri file){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference riversRef = storageRef.child(path);

        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String completePath = Environment.getExternalStorageDirectory() + "/"+"Top-Task/"+ path;
                File file = new File(completePath);
                if (file.exists()) {
                    if (file.delete()) { } else { }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                String ex =exception.toString();
                Toast.makeText(getContext(), ex, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void changeBackground(View view) {
        show=1;
        view.animate().rotation(1440).setDuration(300).start();
        images();
    }

    public void ChangeProfileImage(View view) {
        show=0;
        view.animate().rotation(1440).setDuration(300).start();
        images();
    }
}

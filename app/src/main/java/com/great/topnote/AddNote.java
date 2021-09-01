package com.great.topnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.great.topnote.data.Note;
import com.great.topnote.data.myDbAdapter;
import com.varunjohn1990.audio_record_view.AttachmentOption;
import com.varunjohn1990.audio_record_view.AttachmentOptionsListener;
import com.varunjohn1990.audio_record_view.AudioRecordView;

import java.util.ArrayList;
import java.util.List;

public class AddNote extends AppCompatActivity implements AudioRecordView.RecordingListener {


    private AudioRecordView audioRecordView;

    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        setId();
    }
    TextView title,details;
    ImageView image ,file_image,mic_image;
    Integer show=0,all=0;
    private void setId(){

        audioRecordView = new AudioRecordView();
        // this is to make your layout the root of audio record view, root layout supposed to be empty..
        audioRecordView.initView((FrameLayout) findViewById(R.id.layoutMain));
        // this is to provide the container layout to the audio record view..
        audioRecordView.setRecordingListener(this);
        //To change the text SlideToCancel

        audioRecordView.getAttachmentView();

        setListener();

        title=findViewById(R.id.title);
        details=findViewById(R.id.details);

        image=findViewById(R.id.image);
        file_image=findViewById(R.id.file);
        mic_image=findViewById(R.id.mic);
    }
    String for_me="image";
    public void pdf() {
        for_me = "file";
        get_data(new String[]{"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                "text/plain",
                "application/pdf"});
    }


    public void images() {
        for_me="image";
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
    String sort;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            if (resultCode == Activity.RESULT_OK) {

                //             textTargetUri.setText(targetUri.toString());
                Uri targetUri = data.getData();

                sort=for_me;
                if(sort.charAt(0)=='i'){
                    im = targetUri;
                    Glide.with(getContext()).load(im).into(image);
                }
                else if(sort.charAt(0)=='v'){
                    voice = targetUri;
                    mic_image.setVisibility(View.VISIBLE);}
                else{
                    file = targetUri;
                    file_image.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    Uri file,im,voice;

    private void upload_file(String f,Uri file){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference riversRef = storageRef.child(f);

        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                String ex =exception.toString();
                Toast.makeText(getContext(), ex, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setListener() {

        audioRecordView.getMessageView().requestFocus();
        audioRecordView.getAttachmentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdf();
            }
        });

        audioRecordView.getEmojiView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                audioRecordView.hideAttachmentOptionView();
            }
        });

        audioRecordView.getCameraView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                images(); }
        });

        audioRecordView.getSendView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = audioRecordView.getMessageView().getText().toString().trim();
                audioRecordView.getMessageView().setText("");
                if(show==0){
                    all++;
                    details.setText(msg+"");
                    audioRecordView.getMessageView().setText("");
                    audioRecordView.getMessageView().setHint("Type Title .");}
                else{
                    all++;
                    title.setText(msg+"");
                    audioRecordView.getMessageView().setText("");
                    audioRecordView.getMessageView().setHint("Type message .");}
                show++;
                show%=2;
            }
        });
    }
    @Override
    public void onRecordingStarted() {

        for_me="voice";
        time = System.currentTimeMillis() / (1000);
        Intent intent = new Intent(
                MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, 100);

    }

    @Override
    public void onRecordingLocked() { }

    @Override
    public void onRecordingCompleted() { }

    @Override
    public void onRecordingCanceled() { voice=null; }


    myDbAdapter adapter;
    public void Post(View view) {
        adapter=new myDbAdapter(this);

        Note note = new Note();
        note.setTitle(title.getText().toString());
        note.setGroupId(adapter.getUserData_inf()[1].substring(0,adapter.getUserData_inf()[1].length()-4));
        note.setId((Long.MAX_VALUE-System.currentTimeMillis())+"");
        note.setDetails(details.getText().toString());
        note.setName("Mohammed M Elkady");
        note.setFile(file==null?0:System.currentTimeMillis());
        note.setSound(voice==null?0:(System.currentTimeMillis()+1));
        note.setImage(im==null?0:(System.currentTimeMillis()+2));
        myDbAdapter adapter = new myDbAdapter(getContext());
        if(all>=2){
        adapter.insertNoteData(note);
            FirebaseDatabase.getInstance().getReference().child("Note").child(note.getGroupId())
                    .child(note.getId()).setValue(note);
            if(note.getFile()>0)
                upload_file(note.getFile()+"",file);
            if(note.getImage()>0)
                upload_file(note.getImage()+"",im);
            if(note.getSound()>0)
                upload_file(note.getSound()+"",voice);
        }

    }
}
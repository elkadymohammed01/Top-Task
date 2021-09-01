package com.great.topnote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.great.topnote.adapter.CommentAdapter;
import com.great.topnote.adapter.TaskAdapter;
import com.great.topnote.data.Comment;
import com.great.topnote.data.Save;
import com.great.topnote.data.myDbAdapter;

import java.util.ArrayList;
import java.util.List;

public class TaskNote extends AppCompatActivity implements TextWatcher , ChildEventListener , TextView.OnEditorActionListener {

    RecyclerView CommentsList;
    TextView id,title,details;
    EditText Note;
    String  Message= "" ;
    boolean show=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_note);

        setTaskInformation();
        setTaskItemRecycler();
        setNewNote();
        setNote();
    }

    private void setTaskInformation(){

        id=findViewById(R.id.id);
        title=findViewById(R.id.title);
        details=findViewById(R.id.details);

        if(Save.task==null)
            finish();

        id.setText(Save.task.getName());
        details.setText(Save.task.getDetails());
        title.setText(Save.task.getTitle());

    }

    private void setNote(){
        Note=findViewById(R.id.note_details);
        Note.addTextChangedListener(this);
        Note.setOnEditorActionListener(this);
    }
    private void setNewNote(){
        FirebaseDatabase.getInstance().getReference().child("NoteTask").child(Save.Id).limitToFirst(52).addChildEventListener(this);
    }
    CommentAdapter adapter;
    public void setTaskItemRecycler() {
        CommentsList=findViewById(R.id.comment);
        List<Comment> listComment=new ArrayList<>();

        CommentsList.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter= new CommentAdapter(this,listComment) ;
        CommentsList.setHasFixedSize(true);
        CommentsList.setAdapter(adapter);


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().split(" ").length>0){
            show=true;
        }else show=false;
        Message=s.toString();
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        if(adapter.getItemCount()<=50)
        adapter.addComment(snapshot.getValue(Comment.class));

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    myDbAdapter Db;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Db=new myDbAdapter(getApplicationContext());
        if(show&&Message.length()>0&&adapter.getItemCount()<=50){
            show=false;
            Comment comment=new Comment();
            comment.setNote(Message);
            comment.setUserId(Db.getUserData_inf()[1]);
            comment.setUserName(Db.getUserData_inf()[0]);
            FirebaseDatabase.getInstance().getReference().child("NoteTask").child(Save.Id)
                    .push().setValue(comment);
            id.setFocusable(true);
            Note.setText("");
            FirebaseDatabase.getInstance().getReference().child("NoteMe").child(Save.Id).setValue("elpha");
        }
        return false;
    }
}

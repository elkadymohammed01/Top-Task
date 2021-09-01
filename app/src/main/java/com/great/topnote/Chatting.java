package com.great.topnote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.great.topnote.adapter.UserAdapter;
import com.great.topnote.adapter.getImage.MessageAdapter;
import com.great.topnote.data.Message;
import com.great.topnote.data.Save;
import com.great.topnote.data.myDbAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class Chatting extends AppCompatActivity implements TextWatcher , TextView.OnEditorActionListener , ChildEventListener {

    RecyclerView MessageList;
    EditText NewMessage;
    Integer sent=0;
    myDbAdapter Db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        setId();
    }

    private void setId(){
        MessageList=findViewById(R.id.messageList);
        NewMessage=findViewById(R.id.editText);

        Db = new myDbAdapter(getApplicationContext());

        NewMessage.setOnEditorActionListener(this);
        NewMessage.addTextChangedListener(this);

        getMessage();
        setMessageRecycler();
    }

    public void setMessageRecycler() {
        RecyclerView.LayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        MessageList.setLayoutManager(layoutManager);
        MessageAdapter adapter = new MessageAdapter(this,messages,Db.getUserData_inf()[1]);
        MessageList.setAdapter(adapter);


    }

    private void getMessage(){

        String []id={Save.Id,Db.getUserData_inf()[1].substring(0,Db.getUserData_inf()[1].length()-4)};
        Arrays.sort(id);

        FirebaseDatabase.getInstance().getReference().child("Chatting")
                .child(id[0]+id[1]).addChildEventListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if(s.toString().split(" ").length>0)
            sent=1;
        else
            sent=0;

    }

    ArrayList<Message>messages =new ArrayList<>();
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        String []id={Save.Id,Db.getUserData_inf()[1].substring(0,Db.getUserData_inf()[1].length()-4)};
        Arrays.sort(id);

        if(sent>0){

            Message message =new Message(Db.getUserData_inf()[1],NewMessage.getText().toString(),"30/7/2121");
            if(!NewMessage.getText().toString().equals(""))
            FirebaseDatabase.getInstance().getReference().child("Chatting")
                    .child(id[0]+id[1]).push().setValue(message);
            NewMessage.setText("");

        }
        return false;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        messages.add(snapshot.getValue(Message.class));
        setMessageRecycler();
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
}

package com.great.topnote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;
import com.great.topnote.data.myDbAdapter;

public class AddUserToList extends AppCompatActivity {

    EditText UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_to_list);
        setId();
    }
    myDbAdapter DB ;
    private void setId(){
        UserId=findViewById(R.id.editText3);
        DB=new myDbAdapter(this);
    }

    public void AddUser(View view) {
        if(UserId.getText().toString().split(" ").length!=1){
            UserId.setHint("More Space at Id ..");
            UserId.setText("");
            UserId.setBackgroundResource(R.drawable.custom_text2);
        }
        else if(UserId.getText().toString().length()<7){
            UserId.setHint("too Short Id ..");
            UserId.setText("");
            UserId.setBackgroundResource(R.drawable.custom_text2);
        }
        else{
            String id=DB.getUserData_inf()[1];
            FirebaseDatabase.getInstance().getReference().child("FriendRequest")
                    .child(UserId.getText().toString()).child(id.substring(0,id.length()-4)).setValue(id);
        }
    }
}

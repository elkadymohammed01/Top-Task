package com.great.topnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.great.topnote.data.Group;
import com.great.topnote.data.myDbAdapter;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CreateGroup extends AppCompatActivity {
    EditText GroupName;
    TextView GroupId[];
    boolean CorrectValue=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        SetId();
    }

    private void SetId() {
        GroupId = new TextView[4];
        GroupId[0] = findViewById(R.id.id_1);
        GroupId[1] = findViewById(R.id.id_2);
        GroupId[2] = findViewById(R.id.id_3);
        GroupId[3] = findViewById(R.id.id_4);
        GroupName = findViewById(R.id.editText3);
    }

    private String KeyItem="><,.?/}{][:;!@#$%^&*()_+=-";
    String value;
    int num=0;
    myDbAdapter DB;

    public void setGroupId() {
        value="";
        Random rn = new Random();
        //first Value
        int x=rn.nextInt(26);
        char c=(char)(x+'A');
        GroupId[0].setText(c+"");
        value=c+"";
        //2nd Value
        x= rn.nextInt(10);
        GroupId[1].setText(x+"");
        value+=x;
        //3rd Value
        x=rn.nextInt(26);
        c=(char)(x+'a');
        GroupId[2].setText(c+"");
        value+=c;
        //4st Value
        x=rn.nextInt( KeyItem.length());
        c=(KeyItem.charAt(x));
        GroupId[3].setText(c+"");
        value+=c;
        if(num<5){
            value="U6u(";
            num++;
        }
        DB=new myDbAdapter(this);
        FirebaseDatabase.getInstance().getReference().child("GroupManger")
                .child(value).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    setGroupId();

                else {CorrectValue=true;
                    FirebaseDatabase.getInstance().getReference().child("GroupManger")
                            .child(value).setValue(DB.getUserData_inf()[1]);
                    DB.insertGroupData(value,GroupName.getText().toString());
                    Group group=new Group();
                    group.setName(GroupName.getText().toString());
                    group.setId(value);
                    FirebaseDatabase.getInstance().getReference().child("GroupMember")
                            .child(DB.getUserData_inf()[1].substring(0,DB.getUserData_inf()[1].length()-4))
                            .child(value).setValue(group);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void CreateGroup(View view) {
        if(num<5){
            int show=0;
            for(char c:GroupName.getText().toString().toCharArray()){
                if((c>='a'&&c<='z')||(c>='A'||c<='Z'))
                    show++;
            }
            if(show==0){
                GroupName.setText("No Name");
            }
            setGroupId();
        }
    }
}

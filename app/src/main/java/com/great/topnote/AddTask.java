package com.great.topnote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.great.topnote.data.Task;
import com.great.topnote.data.myDbAdapter;

public class AddTask extends AppCompatActivity {

    TextView title , details ,Post;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        setId();
        OnCheckText();
    }
    int show=0 ;
    boolean Done=false;
    private void setId(){
        title=findViewById(R.id.title);
        details=findViewById(R.id.details);
        text=findViewById(R.id.text);
        Post=findViewById(R.id.post);
    }
    private void OnCheckText(){
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(show==0)
                    title.setText(s.toString());
                else
                    details.setText(s.toString());
                if(title.getText().toString().split(" ").length>0
                        &&details.getText().toString().split(" ").length>0){
                    Done=true;
                    Post.setTextColor(getResources().getColor(R.color.post));
                }
                else{
                    Done=false;
                    Post.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
    }

    public void Title(View view) {
        show=0;
        text.setText(title.getText());
    }

    public void Details(View view) {
        show=1;
        text.setText(details.getText());
    }

    public void Post(View view) {
        if(Done){
            myDbAdapter adapter=new myDbAdapter(this);
            Task task=new Task();
            task.setEmail(adapter.getUserData_inf()[1]);
            task.setName(adapter.getUserData_inf()[0]);
            task.setTitle(title.getText().toString()+"");
            task.setDetails(details.getText().toString()+"");
            task.setEmail_resever(task.getEmail());

            String id=Long.MAX_VALUE-System.currentTimeMillis()+"";
            adapter.insertTaskData(task,id);

            FirebaseDatabase.getInstance()
                    .getReference().child("Task")
                    .child(id)
                    .setValue(task);
        }
    }
}

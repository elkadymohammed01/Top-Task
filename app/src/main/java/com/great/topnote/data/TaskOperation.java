package com.great.topnote.data;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.great.topnote.R;

public class TaskOperation implements View.OnClickListener , TextWatcher {

    private Activity context;
    private String text="";
    private boolean show=false;
    private Dialog dialog;

    public TaskOperation(Activity context) {
        this.context = context;
    }

    public void  Note(){
        Dialog NoteTask = new Dialog((Context) context);
        NoteTask.setContentView(R.layout.edtext);
        NoteTask.show();
        TextView ok =NoteTask.findViewById(R.id.ok);
        TextView close =NoteTask.findViewById(R.id.cancel);
        EditText Note =NoteTask.findViewById(R.id.editText6);
        dialog=NoteTask;
        Note.addTextChangedListener(this);
        ok.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dialog.dismiss();
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
            show=true;
        else
            show=false;
        text=s.toString();
    }
}
class OkClick implements View.OnClickListener {

    Integer id;
    Task task;
    String Message;
    Dialog dialog;

    public OkClick(Integer id, Task task, String message, Dialog dialog) {
        this.id = id;
        this.task = task;
        Message = message;
        this.dialog = dialog;
    }

    @Override
    public void onClick(View v) {

        //Add Some Note About This Task

        if(id==1){
            task.setDetails(Message);
            FirebaseDatabase.getInstance().getReference().child("NoteTask")
                    .child(task.getEmail().substring(0,task.getEmail().length()-4))
                    .setValue(task);
        }
        dialog.dismiss();
    }

}

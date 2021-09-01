package com.great.topnote.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.great.topnote.R;
import com.great.topnote.TaskNote;
import com.great.topnote.data.DownloadFile;
import com.great.topnote.data.Note;
import com.great.topnote.data.Save;
import com.great.topnote.data.Task;
import com.great.topnote.data.TaskOperation;
import com.great.topnote.data.myDbAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.NoteHolder> {

    Activity context;
    List<Task> TaskList;
    List<String>TaskId;
    myDbAdapter adapter;
    public TaskAdapter(Activity context, List<Task> NoteList,List<String>Id) {
        this.context = context;
        this.TaskList = NoteList;
        this.TaskId=Id;
        adapter=new myDbAdapter(context);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cardtask, parent, false);
        // lets create a recyclerview row item layout file
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {

        Task task =TaskList.get(TaskList.size()-(1+position));

        holder.title.setText(task.getTitle());
        holder.details.setText(task.getDetails());
        holder.id.setText(task.getName().split(" ")[0]);
        NoteMe(position,holder.error,1);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save.task=task;
                Save.Id=TaskId.get(TaskList.size()-(1+position));
                context.startActivity(new Intent(context, TaskNote.class));
                NoteMe(position,holder.error,0);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setSpinner(holder.spinner);
                return false;
            }
        });

    }

    private void NoteMe(Integer pos,ImageView mark,Integer op){
        FirebaseDatabase.getInstance().getReference().child("NoteMe")
                .child(TaskId.get(TaskId.size()-(1+pos))).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    mark.setVisibility(View.VISIBLE);
                    if(op==0){
                        if(snapshot.getValue().toString().equals("0")){
                            FirebaseDatabase.getInstance().getReference().child("NoteMe")
                                    .child(TaskId.get(TaskId.size()-(1+pos)))
                                    .setValue(adapter.getUserData_inf()[1]);
                            mark.setVisibility(View.INVISIBLE);
                        }
                        else if(!snapshot.getValue().toString().equals(adapter.getUserData_inf()[1]+"")){
                            FirebaseDatabase.getInstance().getReference().child("NoteMe")
                                    .child(TaskId.get(TaskId.size()-(1+pos))).removeValue();
                            mark.setVisibility(View.INVISIBLE);
                        }
                    }
                    else if(snapshot.getValue().toString().equals(adapter.getUserData_inf()[1]+"")){
                        mark.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void setSpinner(Spinner spinner){

        final int[] trySelect = {0};
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.planets_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.performClick();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(trySelect[0] >0){
                    spinner.setVisibility(View.INVISIBLE);
                    OnLongClick(0);}
                else{
                    trySelect[0]++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setVisibility(View.INVISIBLE);
            }
        });

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    wait(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                spinner.setVisibility(View.VISIBLE);
            }
        };
    }
    @Override
    public int getItemCount() {
        return TaskList.size();
    }

    private void OnLongClick(Integer pos){
        TaskOperation TaskOp=new TaskOperation(context);
        if(pos==0){
            TaskOp.Note();
        }
    }

    public static final class NoteHolder extends RecyclerView.ViewHolder{


        TextView title,details,id;
        Spinner spinner ;
        View view;
        ImageView error;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            details=itemView.findViewById(R.id.details);
            id=itemView.findViewById(R.id.id);
            view=itemView;
            spinner = itemView.findViewById(R.id.spinner);
            error = itemView.findViewById(R.id.error);
        }
    }

}

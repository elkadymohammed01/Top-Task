package com.great.topnote.MainPageUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.great.topnote.ActivityUser;
import com.great.topnote.R;
import com.great.topnote.adapter.GroupCategoryAdapter;
import com.great.topnote.adapter.TaskAdapter;
import com.great.topnote.adapter.UserAdapter;
import com.great.topnote.data.DownloadFile;
import com.great.topnote.data.User;
import com.great.topnote.data.myDbAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AddUser extends Fragment {

    private myDbAdapter DbAdapter;
    TextView name;
    ImageView user;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.adduser, container, false);
        setId(v);
        return v;
    }
    private void setId(View v) {

        UserItemRecycler=v.findViewById(R.id.listUser);
        name=v.findViewById(R.id.textView5);
        user=v.findViewById(R.id.imageView3);

        DbAdapter=new myDbAdapter(getActivity());
        name.setText("Hello "+DbAdapter.getUserData_inf()[0].split(" ")[0]+"!");
        getUser();
        getUsersRequests();
    }
    ArrayList<String> Name,Email;
    RecyclerView UserItemRecycler;
    public void setUserRecycler() {
        RecyclerView.LayoutManager layoutManager
                = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        UserItemRecycler.setLayoutManager(layoutManager);
        UserAdapter adapter = new UserAdapter(getContext(),Email);
        UserItemRecycler.setAdapter(adapter);
    }

    private void getUsersRequests(){
        String mail=DbAdapter.getUserData_inf()[1];
        Email=new ArrayList<>();
        Name=new ArrayList<>();
        Email.add("55");
        FirebaseDatabase.getInstance().getReference()
                .child("FriendRequest").child(mail.substring(0,mail.length()-4))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot Snapshot : snapshot.getChildren()){
                        Email.add(Snapshot.getValue().toString());}
                        setUserRecycler();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void getUser(){

        DownloadFile file=new DownloadFile();
        if(file.getLocalFile(DbAdapter.getUserData_inf()[1]+"").exists()){
            String imageUri = file.getLocalFile(DbAdapter.getUserData_inf()[1]+"").getPath();

            Bitmap myBitmap = BitmapFactory.decodeFile(new File(imageUri).getAbsolutePath());

            user.setImageBitmap(myBitmap);
        }else{
            try {
                file.setLocalFile(DbAdapter.getUserData_inf()[1]+"",getContext(),user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ActivityUser.class));
            }
        });
    }
}
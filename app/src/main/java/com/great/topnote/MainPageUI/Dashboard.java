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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.great.topnote.ActivityUser;
import com.great.topnote.DepthPageTransformer;
import com.great.topnote.R;
import com.great.topnote.adapter.NoteAdapter;
import com.great.topnote.adapter.TaskAdapter;
import com.great.topnote.adapter.UIAdapter;
import com.great.topnote.adapter.UserFriendAdapter;
import com.great.topnote.data.DownloadFile;
import com.great.topnote.data.Task;
import com.great.topnote.data.myDbAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Fragment {

    private myDbAdapter DbAdapter;
    private TextView name;
    private ImageView user;
    private RecyclerView UserList;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.dashboard, container, false);
        setId(v);
        return v;
    }

    private void setId(View v) {

        UserList = v.findViewById(R.id.user_recycler);
        name=v.findViewById(R.id.textView5);
        user=v.findViewById(R.id.imageView3);

        DbAdapter=new myDbAdapter(getActivity());
        name.setText("Hello "+DbAdapter.getUserData_inf()[0].split(" ")[0]+"!");

        getUser();
        setTask();

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
    private List<String> Email;
    private void setTask(){
        String mail=DbAdapter.getUserData_inf()[1];
        Email=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference()
                .child("FriendList").child(mail.substring(0,mail.length()-4))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot Snapshot : snapshot.getChildren()){
                            Email.add(Snapshot.getValue().toString());}

                        setUserItemRecycler(Email);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void setUserItemRecycler(List<String> email) {
        UserList.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        UserFriendAdapter adapter =new UserFriendAdapter(getActivity(),email);
        UserList.setHasFixedSize(true);
        UserList.setAdapter(adapter);
    }
}
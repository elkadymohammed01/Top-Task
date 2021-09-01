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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.great.topnote.ActivityUser;
import com.great.topnote.MainPage;
import com.great.topnote.R;
import com.great.topnote.adapter.GroupCategoryAdapter;
import com.great.topnote.adapter.NoteAdapter;
import com.great.topnote.adapter.OnClickItem;
import com.great.topnote.data.DownloadFile;
import com.great.topnote.data.Group;
import com.great.topnote.data.myDbAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Posting extends Fragment implements OnClickItem {



    TextView name;
    ImageView user;
    myDbAdapter DbAdapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.posting, container, false);
        setId(v);
        return v;
    }

    private RecyclerView NoteItemRecycler,GroupItemRecycler;

    private void setId(View v) {

        NoteItemRecycler = v.findViewById(R.id.note_recycler);
        GroupItemRecycler=v.findViewById(R.id.group_recycler);
        name=v.findViewById(R.id.textView5);
        user=v.findViewById(R.id.imageView3);

        DbAdapter=new myDbAdapter(getActivity());
        setGroupItemRecycler();
        setNoteItemRecycler(0);
        name.setText("Hello "+DbAdapter.getUserData_inf()[0].split(" ")[0]+"!");
        getUser();
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
    List<Group> GroupList;
    public void setGroupItemRecycler() {
        GroupList = DbAdapter.getGroup();
        RecyclerView.LayoutManager layoutManager
                = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        GroupItemRecycler.setLayoutManager(layoutManager);
        GroupCategoryAdapter adapter = new GroupCategoryAdapter(getActivity(), GroupList,this);
        GroupItemRecycler.setAdapter(adapter);
    }

    public void setNoteItemRecycler(Integer pos) {
        NoteItemRecycler.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        NoteAdapter adapter =new NoteAdapter(getActivity(),DbAdapter.getNotes(GroupList.get(pos).getId()));
        NoteItemRecycler.setHasFixedSize(true);
        NoteItemRecycler.setAdapter(adapter);
    }


    @Override
    public void ClickItem(Integer pos) {
     setNoteItemRecycler(pos);
    }
}
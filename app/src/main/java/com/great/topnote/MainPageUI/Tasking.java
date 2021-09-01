package com.great.topnote.MainPageUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.great.topnote.MainPage;
import com.great.topnote.R;
import com.great.topnote.adapter.GroupCategoryAdapter;
import com.great.topnote.adapter.NoteAdapter;
import com.great.topnote.adapter.TaskAdapter;
import com.great.topnote.data.GroupCategory;
import com.great.topnote.data.myDbAdapter;

import java.util.ArrayList;
import java.util.List;

public class Tasking extends Fragment {

    private myDbAdapter DbAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.tasking, container, false);
        setId(v);
        return v;
    }

    RecyclerView TaskItemRecycler,GroupItemRecycler;

    private void setId(View v) {
        TaskItemRecycler = v.findViewById(R.id.task_recycler);
        DbAdapter =new myDbAdapter(getContext());
        setTaskItemRecycler();
    }
    List<String>task;
    public void setTaskItemRecycler() {
        TaskItemRecycler.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        TaskAdapter adapter =new TaskAdapter(getActivity(),DbAdapter.getTask(),DbAdapter.getTaskId());
        TaskItemRecycler.setHasFixedSize(true);
        TaskItemRecycler.setAdapter(adapter);


    }

}
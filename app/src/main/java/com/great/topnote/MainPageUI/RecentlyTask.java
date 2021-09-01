package com.great.topnote.MainPageUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.great.topnote.R;
import com.great.topnote.adapter.TaskAdapter;
import com.great.topnote.data.myDbAdapter;

public class RecentlyTask extends Fragment {

    private myDbAdapter DbAdapter;

    ConstraintLayout background ;
    Integer resBackground;

    public RecentlyTask(Integer pos){
        resBackground=pos;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.recently_task, container, false);
        setId(v);
        return v;
    }

    private void setId(View v) {

        DbAdapter =new myDbAdapter(getContext());
        background=v.findViewById(R.id.taskManger);
        background.setBackgroundResource(resBackground);

    }


}
package com.great.topnote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.great.topnote.MainPageUI.AddUser;
import com.great.topnote.MainPageUI.Dashboard;
import com.great.topnote.MainPageUI.Posting;
import com.great.topnote.MainPageUI.Tasking;
import com.great.topnote.adapter.UIAdapter;
import com.great.topnote.data.Group;
import com.great.topnote.data.Note;
import com.great.topnote.data.myDbAdapter;

public class MainPage extends AppCompatActivity {

    static MeowBottomNavigation NavBottom;
    static ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_page);

        getNewNotes();
        setViewPager();
        setNavBottom();
    }

    UIAdapter adapter;

    public void setViewPager() {
        viewPager=findViewById(R.id.pager);
        adapter = new UIAdapter(getSupportFragmentManager());
        adapter.addFragment(new Posting());
        adapter.addFragment(new Tasking());
        adapter.addFragment(new AddUser());
        adapter.addFragment(new Dashboard());
        adapter.addFragment(new Posting());
        viewPager.setAdapter(adapter);
        OnPageSelected();
    }
    private void OnPageSelected(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getIcon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void setNavBottom() {

        //icon of Nav Bottom
        NavBottom =findViewById(R.id.meowBottomNavigation);
        NavBottom.add(new MeowBottomNavigation.Model(1, R.drawable.notebook));
        NavBottom.add(new MeowBottomNavigation.Model(2, R.drawable.clipboard));
        NavBottom.add(new MeowBottomNavigation.Model(3, R.drawable.adduser));
        NavBottom.add(new MeowBottomNavigation.Model(4, R.drawable.inbox));
        NavBottom.add(new MeowBottomNavigation.Model(5, R.drawable.ic_search_black_24dp));
        NavBottom.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                viewPager.setCurrentItem(item.getId()-1);
            }
        });
        NavBottom.setOnShowListener(item -> {

        });

        NavBottom.setOnReselectListener(item -> {
            // your codes
        });
        NavBottom.show(1, true);
    }

    public void AddPost(View view) {
        startActivity(new Intent(getApplicationContext(), AddTask.class)); }

    private void getNewNotes() {

        myDbAdapter DbAdapter=new myDbAdapter(this);
        for (Group group : DbAdapter.getGroup()) {
            FirebaseDatabase.getInstance().getReference().child("Note")
                    .child(group.getId()).limitToFirst(50)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { ;
                            DbAdapter.insertNoteData(snapshot.getValue(Note.class));
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            DbAdapter.updateNote(snapshot.getValue(Note.class));
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    public static void getIcon(Integer pos){ NavBottom.show(viewPager.getCurrentItem()+1,false); }
}

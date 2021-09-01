package com.great.topnote.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.great.topnote.MainPage;

import java.util.ArrayList;
import java.util.List;

public class UIAdapter  extends FragmentStatePagerAdapter {
    private final List<Fragment> mList = new ArrayList<>();

    public UIAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }
    int pos=-1,show=0;
    @Override
    public Fragment getItem(int i) {
        return mList.get(i);
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    public void addFragment(Fragment fragment) {

        mList.add(fragment);
    }
}

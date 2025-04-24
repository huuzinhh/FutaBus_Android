package com.example.futasbus.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.futasbus.Activity.AccountFragment;
import com.example.futasbus.Activity.HistoryFragment;
import com.example.futasbus.Activity.HomeFragment;
import com.example.futasbus.Activity.NotificationsFragment;

public class ViewPageAdapter extends FragmentStatePagerAdapter {
    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior){
        super(fm,behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new HistoryFragment();
            case 2:
                return new NotificationsFragment();
            case 3:
                return new AccountFragment();
        }
        return null;
    }
    @Override
    public int getCount(){
        return 4;
    }
}

package com.example.futasbus.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.futasbus.Activity.AdminAccountFragment;
import com.example.futasbus.Activity.AdminHomeFragment;


public class ViewPageAdapterAdmin extends FragmentStatePagerAdapter {
    public ViewPageAdapterAdmin(@NonNull FragmentManager fm, int behavior){
        super(fm,behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new AdminHomeFragment();
            case 1:
                return new AdminAccountFragment();
        }
        return null;
    }
    @Override
    public int getCount(){
        return 2;
    }
}

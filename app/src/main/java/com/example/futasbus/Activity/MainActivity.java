package com.example.futasbus.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.futasbus.Adapter.ViewPageAdapter;
import com.example.futasbus.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private TextView name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("MODE", MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        AppCompatDelegate.setDefaultNightMode(
                nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.nav_history).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.nav_notifications).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.nav_account).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_history:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.nav_notifications:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.nav_account:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }
    public void navigateToAccountTab() {
        viewPager.setCurrentItem(3);
    }

}

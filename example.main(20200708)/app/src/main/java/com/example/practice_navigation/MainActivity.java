package com.example.practice_navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.practice_navigation.ui.home.HomeFragment;
import com.example.practice_navigation.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    FragmentManager fm = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navagation);
        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frame,new HomeFragment())
                .commit();
        BottomNavigationView bnw = findViewById(R.id.nav_view);
        bnw.setOnNavigationItemSelectedListener(this);




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_home:fm.beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
            break;
            default: fm.beginTransaction().replace(R.id.frame, new NotificationsFragment()).commit();
        }
        return true;
    }
}
package com.example.practice_navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.practice_navigation.ui.dashboard.DashboardFragment;
import com.example.practice_navigation.ui.home.HomeFragment;
import com.example.practice_navigation.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    FragmentManager fm = getSupportFragmentManager();
    private String[] myDataset;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frame,new HomeFragment())
                .commit();
        BottomNavigationView bnw = findViewById(R.id.nav_view);
        bnw.setOnNavigationItemSelectedListener(this);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView recyclerView=findViewById(R.id.my_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_home:fm.beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
            break;
            case R.id.navigation_dashboard:fm.beginTransaction().replace(R.id.frame, new DashboardFragment()).commit();
            break;
            default: fm.beginTransaction().replace(R.id.frame, new NotificationsFragment()).commit();
        }
        return true;
    }
}
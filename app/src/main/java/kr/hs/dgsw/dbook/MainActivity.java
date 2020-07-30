package kr.hs.dgsw.dbook;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import kr.hs.dgsw.dbook.ui.dashboard.DashboardFragment;
import kr.hs.dgsw.dbook.ui.home.HomeFragment;
import kr.hs.dgsw.dbook.ui.notifications.NotificationsFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    FragmentManager fm = getSupportFragmentManager();


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
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();
            transaction.replace(R.id.text_home, fragment);
            transaction.commit();
        }
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
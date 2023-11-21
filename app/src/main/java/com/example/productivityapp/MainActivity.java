package com.example.productivityapp;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.bottom_nav_view);
        fragmentManager = new FragmentManager(this);

        if (savedInstanceState == null) {
            // Set the default fragment
            fragmentManager.replaceFragment(new TimerFragment(), false);
        }

        navView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_timer) {
            fragmentManager.replaceFragment(new TimerFragment(), false);
            return true;
        } else if (itemId == R.id.nav_toDo) {
            fragmentManager.replaceFragment(new ToDoFragment(),false);
            return true;
        } else if (itemId == R.id.nav_social) {
            // fragmentManager.replaceFragment(new SocialFragment(), false);
            return true;
        } else if (itemId == R.id.nav_configuration) {
            fragmentManager.replaceFragment(new SettingsFragment(), false);
            return true;
        } else {
            throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
    };
}
package com.example.productivityapp.presentacion.social;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.example.productivityapp.FragmentManager;
import com.example.productivityapp.R;
import com.example.productivityapp.presentacion.settings.SettingsFragment;
import com.example.productivityapp.presentacion.social.requests.RequestsFragment;
import com.example.productivityapp.presentacion.social.searchFriends.SearchFriendsFragment;
import com.example.productivityapp.presentacion.timer.TimerFragment;
import com.example.productivityapp.presentacion.toDo.ToDoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchFriendsActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        // Configura el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Habilita la flecha hacia atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navView = findViewById(R.id.bottom_nav_view);
        fragmentManager = new FragmentManager(this);

        if (savedInstanceState == null) {
            // Set the default fragment
            fragmentManager.replaceFragment(new RequestsFragment(), false);
        }

        navView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_solicitudes) {
            fragmentManager.replaceFragment(new RequestsFragment(), false);
            return true;
        } else if (itemId == R.id.nav_buscar) {
            fragmentManager.replaceFragment(new SearchFriendsFragment(),false);
            return true;
        } else {
            throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
package com.example.hatsnative;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("Hats_ML");
    }

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.it_home);
        setupNavigationHandler();
        navigationView.getMenu().performIdentifierAction(R.id.it_home, 0);
    }

    private void setupNavigationHandler() {
        navigationView.setNavigationItemSelectedListener((MenuItem item) -> {
            int id = item.getItemId();
            Fragment fragment = null;
            if (id == R.id.it_wifi) {
                fragment = new WifiSetupFragment();
            } else if (id == R.id.it_about) {
                fragment = new AboutFragment();
            } else if (id == R.id.it_file) {
                fragment = new DatasetFragment();
            } else if (id == R.id.it_settings) {
                fragment = new SettingsFragment();
            } else if (id == R.id.it_home) {
                fragment = new HomeFragment();
            }
            loadFragment(fragment);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment).commit();
            drawerLayout.closeDrawer(GravityCompat.START);
            fragmentTransaction.addToBackStack(null);
        }
    }


}
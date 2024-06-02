package com.example.leaflens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.leaflens.BottomMenu.HistoryFragment;
import com.example.leaflens.MenuOptions.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView menuProfile;
    ImageView homepageButton;
    ImageView historyButton;
    DrawerLayout navigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dynamic_container, HomepageFragment.newInstance("open", "open"))
                    .commit();
        }

        NavigationView navigationView = findViewById(R.id.navigation_View);
        navigationView.setNavigationItemSelectedListener(this);

        // initializing bottom buttons

        homepageButton = findViewById(R.id.app_footer_home);
        historyButton = findViewById(R.id.app_footer_history);

        // bottom button listeners
        initialiseClickListeners(homepageButton);
        initialiseClickListeners(historyButton);

        menuProfile = findViewById(R.id.app_header_profileImage);
        navigationDrawer = findViewById(R.id.navigation_menu_drawerLayout);
        setMenuClickListener(menuProfile);

    }

    private void setMenuClickListener(ImageView imageButton)
    {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navigationDrawer.isDrawerOpen(GravityCompat.START))
                {
                    navigationDrawer.closeDrawer(GravityCompat.START);
                }
                else
                {
                    navigationDrawer.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.dynamic_container);

        if(itemId == R.id.menu_settings)
        {
            if(!(currentFragment instanceof SettingsFragment))
                openFragment(new SettingsFragment());
        }
        navigationDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.dynamic_container, fragment).addToBackStack(null).commit();
    }

    private void initialiseClickListeners(View view)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.dynamic_container);
                if(view.getId() == R.id.app_footer_home)
                {
                    if(!(currentFragment instanceof HomepageFragment) && currentFragment!=null)
                    {
                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                }
                else if(view.getId() == R.id.app_footer_history && !(currentFragment instanceof HistoryFragment))
                {
                    openFragment(new HistoryFragment());
                }
            }
        });
    }
}
package com.example.leaflens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leaflens.BottomMenu.HistoryFragment;
import com.example.leaflens.MenuOptions.SettingsFragment;
import com.example.leaflens.entity.Profile;
import com.example.leaflens.homepage.HomepageFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView menuProfile;
    ImageView homepageButton;
    ImageView historyButton;
    ImageView scanButton;
    DrawerLayout navigationDrawer;

    Profile userProfile;    // user profile

    private String deviceID;
    private FirebaseManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dynamic_container, HomepageFragment.newInstance("open", "open"))
                    .commit();
        }

        // getting deviceID
        deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        dbManager = new FirebaseManager();

        // initializing navigation view
        NavigationView navigationView = findViewById(R.id.navigation_View);
        navigationView.setNavigationItemSelectedListener(this);

        // getting user details and setting fields
        View headerMenu = navigationView.getHeaderView(0);
        TextView nameView = headerMenu.findViewById(R.id.menu_nameView);
        fetchUserDetails(nameView);

        // initializing bottom buttons
        homepageButton = findViewById(R.id.app_footer_home);
        historyButton = findViewById(R.id.app_footer_history);
        scanButton = findViewById(R.id.app_footer_scan);

        // bottom button listeners
        initialiseClickListeners(homepageButton);
        initialiseClickListeners(historyButton);
        initialiseClickListeners(scanButton);

        // initializing menu functionality
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
                openFragment(new SettingsFragment(deviceID, userProfile));
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
                else if(view.getId() == R.id.app_footer_scan)
                {
                    Toast.makeText(getApplicationContext(), "In development", Toast.LENGTH_SHORT).show();
                }
                else if(view.getId() == R.id.app_footer_history && !(currentFragment instanceof HistoryFragment))
                {
                    openFragment(new HistoryFragment());
                }
            }
        });
    }


    private void fetchUserDetails(TextView nameView)
    {
        dbManager.fetchProfile(deviceID, new FirebaseManager.ProfileFetchListener() {
            @Override
            public void onProfileFetched(Profile profile) {
                userProfile = profile;
                if(profile != null)
                    nameView.setText(profile.getName());
                else
                    nameView.setText("Guest");
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
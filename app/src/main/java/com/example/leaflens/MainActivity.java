package com.example.leaflens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView menuProfile;
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
}
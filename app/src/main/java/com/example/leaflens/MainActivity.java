package com.example.leaflens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leaflens.BottomMenu.HistoryFragment;
import com.example.leaflens.Entity.Profile;
import com.example.leaflens.MenuOptions.SettingsActivity;
import com.example.leaflens.homepage.HomepageFragment;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSIONS_REQUEST = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 102;
    private static final int REQUEST_IMAGE_CROP = 2;
    private String currentPhotoPath;

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
        dbManager = FirebaseManager.getInstance();

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

    private void setMenuClickListener(ImageView imageButton) {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigationDrawer.isDrawerOpen(GravityCompat.START)) {
                    navigationDrawer.closeDrawer(GravityCompat.START);
                } else {
                    navigationDrawer.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.dynamic_container);

        if (itemId == R.id.menu_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsIntent.putExtra("profile", userProfile);
            startActivity(settingsIntent);
        }
        navigationDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.dynamic_container, fragment).addToBackStack(null).commit();
    }

    private void initialiseClickListeners(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.dynamic_container);
                if (view.getId() == R.id.app_footer_home) {
                    if (!(currentFragment instanceof HomepageFragment) && currentFragment != null) {
                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                } else if (view.getId() == R.id.app_footer_scan) {
                    //Toast.makeText(getApplicationContext(), "In development", Toast.LENGTH_SHORT).show();
                    launchCamera();

                } else if (view.getId() == R.id.app_footer_history && !(currentFragment instanceof HistoryFragment)) {
                    openFragment(new HistoryFragment());
                }
            }
        });
    }


    private void fetchUserDetails(TextView nameView) {
        dbManager.fetchProfile(deviceID, new FirebaseManager.ProfileFetchListener() {
            @Override
            public void onProfileFetched(Profile profile) {
                userProfile = profile;
                if (profile != null)
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

    private void launchCamera() {
        // checking for permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // asking for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST);
        } else {
            startClickImageIntent();
        }
    }

    private void startClickImageIntent() {
        Intent clickImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (clickImageIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createPhotoFile();
            Uri photoUri = FileProvider.getUriForFile(this, "com.example.leaflens.provider", photoFile);
            clickImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            clickImageIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(clickImageIntent, REQUEST_IMAGE_CAPTURE);

        }
    }


    private File createPhotoFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException ex) {
            // Handle the error
            ex.printStackTrace();
        }
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, "com.example.leaflens.provider", new File(currentPhotoPath));
            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
            // saving uncropped Image and cropping the image
            //saveImage(imageBitmap);
            cropImage(photoUri);

        } else if (requestCode == REQUEST_IMAGE_CROP && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = extras.getParcelable("data");
            Toast.makeText(getApplicationContext(), "Image Cropped", Toast.LENGTH_SHORT).show();
            // pass data to ML model
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, launch camera intent
                startClickImageIntent();
            } else {
                // Permission denied, handle accordingly (e.g., show a message or request again)
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cropImage(Uri imageURI) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setDataAndType(imageURI, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }

    private void saveImage(Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            File file = new File(currentPhotoPath);
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}


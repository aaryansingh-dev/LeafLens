package com.example.leaflens.MenuOptions;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leaflens.FirebaseManager;
import com.example.leaflens.R;
import com.example.leaflens.Entity.Profile;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "deviceId";
    private static final int PERMISSIONS_REQUEST = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 102;
    private static final int REQUEST_IMAGE_CROP = 2;
    private static final int REQUEST_IMAGE_PICK = 101;

    private String deviceID;
    private FirebaseManager dbManager;

    private Profile profile;

    EditText nameEdit;
    EditText emailEdit;
    TextView dobEdit;
    EditText phoneEdit;

    Button cancelButton;
    Button saveButton;

    private RoundedImageView profilePictureView;


    public SettingsFragment(String deviceID, Profile profile) {
        this.deviceID = deviceID;
        this.profile = profile;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deviceID = getArguments().getString(ARG_PARAM1);
        }
        dbManager = FirebaseManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        nameEdit = view.findViewById(R.id.settings_NameEditText);
        emailEdit = view.findViewById(R.id.settings_EmailEditText);
        dobEdit = view.findViewById(R.id.settings_DOBEditText);
        phoneEdit = view.findViewById(R.id.settings_phoneEditText);
        cancelButton = view.findViewById(R.id.settings_cancelButton);
        saveButton = view.findViewById(R.id.settings_saveButton);
        profilePictureView = view.findViewById(R.id.settings_profilePicture);


        fetchDetails();
        initDatePickerClick(dobEdit);
        initializeButtons(cancelButton);
        initializeButtons(saveButton);
        initializeImageClick(profilePictureView);

        return view;
    }

    public void initDatePickerClick(TextView textView)
    {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker(textView);
            }
        });
    }
    private void initDatePicker(TextView editText)
    {
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        DatePickerDialog datePicker = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String monthWord = monthNames[month];
                String selectedDate = monthWord +" " + dayOfMonth + " " + year;
                editText.setText(selectedDate);
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void fetchDetails()
    {
        if(profile != null)
        {
            if(profile.getName() != null){
                Log.e("User Details", profile.getName());
                nameEdit.setText(profile.getName());
            }
            if(profile.getEmail() != null)
                emailEdit.setText(profile.getEmail());
            if(profile.getDOB() != null)
                dobEdit.setText(profile.getDOB());
            if(profile.getPhoneNumber() != null)
                phoneEdit.setText(profile.getPhoneNumber());
        }
    }

    private void saveDetails()
    {
        boolean flag = true;

        String name = nameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String dob = dobEdit.getText().toString();
        String phone = phoneEdit.getText().toString();

        if(name.isEmpty())
        {
            setEditTextBackgroundTint(nameEdit, getResources().getColor(R.color.error_red));
            flag = false;
        }


        if(email.isEmpty())
        {
            setEditTextBackgroundTint(emailEdit, getResources().getColor(R.color.error_red));
            flag = false;
        }

        if(dob.isEmpty())
        {
            dobEdit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.error_red)));
            flag = false;
        }

        if(phone.isEmpty())
        {
            setEditTextBackgroundTint(phoneEdit, getResources().getColor(R.color.error_red));
            flag = false;
        }

        if(flag)
        {
            Profile profile = new Profile(deviceID, name, dob, email, phone, null);
            dbManager.addProfile(profile);
            requireActivity().getSupportFragmentManager().popBackStack();
        }
        return;
    }

    public void setEditTextBackgroundTint(EditText editText, int color)
    {
        editText.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    private void initializeButtons(Button button)
    {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        if(button.getId() == R.id.settings_cancelButton) {
           button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   fragmentManager.popBackStack();
               }
           });
        }
        else
        {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveDetails();
                }
            });
        }
    }

    private void initializeImageClick(RoundedImageView imageView)
    {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // start camera method
                showImagePickerOptions();
            }
        });
    }

    private void showImagePickerOptions()
    {
        checkPermissions();
        CharSequence[] options = {"Choose from Gallery", "Take Photo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Change Profile Photo");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
            } else if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            Uri imageUri = null;
            if(requestCode == REQUEST_IMAGE_PICK && data != null)
            {
                imageUri = data.getData();
            }
            else if(requestCode == REQUEST_IMAGE_CAPTURE && data != null)
            {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imageUri = getImageUri(requireContext(), imageBitmap);
                cropImageCircle(imageUri);
            }
        }
    }

    private void checkPermissions()
    {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // asking for permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, launch camera intent
                showImagePickerOptions();
            } else {
                // Permission denied, handle accordingly (e.g., show a message or request again)
                Toast.makeText(getContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "profile_picture", null);
        return Uri.parse(path);
    }

    public void cropImageCircle(Uri uri)
    {
        Uri destinationUri = Uri.fromFile(new File(requireContext().getCacheDir(), "cropped_profile_pic.jpg"));
        UCrop uCrop = UCrop.of(uri, destinationUri);
        uCrop.withAspectRatio(1,1);
        uCrop.withOptions(getCropOptions());
        uCrop.start(requireActivity(), this, REQUEST_IMAGE_CROP);
    }

    private UCrop.Options getCropOptions()
    {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setShowCropGrid(false);
        return options;
    }
}
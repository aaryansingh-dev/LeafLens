package com.example.leaflens.MenuOptions;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.leaflens.Entity.Profile;
import com.example.leaflens.FirebaseManager;
import com.example.leaflens.R;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {


    private String deviceID;
    private FirebaseManager dbManager;

    private Profile profile;

    EditText nameEdit;
    EditText emailEdit;
    TextView dobEdit;
    EditText phoneEdit;

    Button cancelButton;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nameEdit = findViewById(R.id.settings_NameEditText);
        emailEdit = findViewById(R.id.settings_EmailEditText);
        dobEdit = findViewById(R.id.settings_DOBEditText);
        phoneEdit = findViewById(R.id.settings_phoneEditText);
        cancelButton = findViewById(R.id.settings_cancelButton);
        saveButton = findViewById(R.id.settings_saveButton);

        dbManager = FirebaseManager.getInstance();
        deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        profile = (Profile) getIntent().getSerializableExtra("profile");

        fetchDetails();
        initDatePickerClick(dobEdit);
        initializeButtons(cancelButton);
        initializeButtons(saveButton);

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
        DatePickerDialog datePicker = new DatePickerDialog(getBaseContext(), new DatePickerDialog.OnDateSetListener() {
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
            Toast.makeText(this, "Profile saved", Toast.LENGTH_LONG).show();
            Log.d("Settings Activity", "Profile saved");
        }
        return;
    }

    public void setEditTextBackgroundTint(EditText editText, int color)
    {
        editText.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    private void initializeButtons(Button button)
    {

        if(button.getId() == R.id.settings_cancelButton) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("Settings Activity", "Ending settings activity");
                    finish();
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
}

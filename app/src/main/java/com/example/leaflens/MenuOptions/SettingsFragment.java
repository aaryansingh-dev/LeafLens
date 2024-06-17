package com.example.leaflens.MenuOptions;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.Settings;
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
import com.example.leaflens.entity.Profile;

import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "deviceId";

    private String deviceID;
    private FirebaseManager dbManager;

    private Profile profile;

    EditText nameEdit;
    EditText emailEdit;
    TextView dobEdit;
    EditText phoneEdit;

    Button cancelButton;
    Button saveButton;

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

        fetchDetails();
        initDatePickerClick(dobEdit);
        initializeButtons(cancelButton);
        initializeButtons(saveButton);

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
}
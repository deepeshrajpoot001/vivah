package com.example.vivah.activities;


import static com.example.vivah.activities.CreateProfile1.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.vivah.R;
import com.example.vivah.databinding.ActivityCreateProfile2Binding;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;

public class CreateProfile2 extends AppCompatActivity {
    ActivityCreateProfile2Binding binding;
    private int currentYear;
    private  PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfile2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());


        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            binding.firstNameEditText.setText(preferenceManager.getString(Constants.KEY_FIRST_NAME));
            binding.lastNameEditText.setText(preferenceManager.getString(Constants.KEY_LAST_NAME));

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection(Constants.KEY_COLLECTION_USER)
                        .document(preferenceManager.getString(Constants.KEY_USER_ID))
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                binding.date.setText(String.valueOf(documentSnapshot.get(Constants.KEY_DATE_OF_DOB)));
                                binding.month.setText(String.valueOf(documentSnapshot.get(Constants.KEY_MONTH_OF_DOB)));
                                binding.year.setText(String.valueOf(documentSnapshot.get(Constants.KEY_YEAR_OF_DOB)));
                            }
                        });
        }




        Integer[] date = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,26,27,29,30,31};
        Integer[] month = {1,2,3,4,5,6,7,8,9,10,11,12};
         LocalDate localDate = LocalDate.now();
         currentYear = localDate.getYear();
         Integer[] year  = new Integer[20];
        for(int i=0;i<20;i++){
            year[i]=currentYear-39+i;
        }


        ArrayAdapter<Integer> adapterDate = new ArrayAdapter<>(getApplicationContext(),R.layout.drop_down_item,date);
        ArrayAdapter<Integer> adapterMonth = new ArrayAdapter<>(getApplicationContext(),R.layout.drop_down_item,month);
        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<>(getApplicationContext(),R.layout.drop_down_item,year);

        binding.date.setAdapter(adapterDate);
        binding.month.setAdapter(adapterMonth);
        binding.year.setAdapter(adapterYear);

        binding.backButton.setOnClickListener(v-> onBackPressed());

        binding.continueButton.setOnClickListener(v -> {
            if(isAbleToContinue()){
                profile.firstName = binding.firstNameEditText.getText().toString();
                profile.lastName = binding.lastNameEditText.getText().toString();
                startActivity(new Intent(getApplicationContext(),CreateProfile3.class));

            }else{
                Toast.makeText(getApplicationContext(),"fill valid information",Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void showMessage(String message,Integer Id,Integer layoutId){
        AutoCompleteTextView editText = findViewById(Id);
        editText.setError(message);
        editText.requestFocus();
        TextInputLayout layout = findViewById(layoutId);
        layout.setBoxStrokeColor(getColor(R.color.accent));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setError(null);
                layout.setBoxStrokeColor(getColor(com.google.android.material.R.color.design_default_color_primary));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isAbleToContinue() {
        if(binding.date.getText().toString().trim().isEmpty()){
            showMessage("enter date",binding.date.getId(),binding.dateLayout.getId());
            return false;
        }else if(binding.month.getText().toString().trim().isEmpty()){
            showMessage("enter month",binding.month.getId(),binding.layoutMonth.getId());
            return false;
        }else if(binding.year.getText().toString().trim().isEmpty()){
            showMessage("enter year",binding.year.getId(),binding.layoutYear.getId());
            return false;

        }

        profile.dd = Integer.parseInt(binding.date.getText().toString());
        profile.mm = Integer.parseInt(binding.month.getText().toString());
        profile.yyyy = Integer.parseInt(binding.year.getText().toString());

        if(binding.firstNameEditText.getText().toString().trim().isEmpty()){
            showMessage("enter first name",binding.firstNameEditText.getId(),binding.layoutFirstName.getId());
            return false;
        }if(binding.lastNameEditText.getText().toString().trim().isEmpty()){
            showMessage("enter last name",binding.lastNameEditText.getId(),binding.layoutLastName.getId());
            return false;
        } if (profile.yyyy > (currentYear - 20) || profile.yyyy < (currentYear - 39)) {
            return false;
        } else if (profile.mm < 1 || profile.mm > 12) {
            binding.layoutDate.setBoxStrokeColor(500007);
            return false;
        } else if (profile.dd < 1 || profile.dd > 31) {
            return false;
        } else if (profile.yyyy % 4 == 0 && profile.mm == 2 && profile.dd > 29) {
            return false;
        } else if (profile.mm == 2 && profile.dd > 28) {
            return false;
        } else if ((profile.mm == 4 || profile.mm == 6 || profile.mm == 9 || profile.mm == 11) && profile.dd > 30){
            return false;
        }
        else{return true;
        }





    }

    @Override
    protected void onStart() {
        super.onStart();


        binding.date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(binding.date.getText().toString().length()==2){
                    binding.month.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        binding.month.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(binding.month.getText().toString().length()==2){
                    binding.year.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }
}
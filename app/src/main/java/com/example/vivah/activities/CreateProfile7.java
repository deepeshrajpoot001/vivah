package com.example.vivah.activities;

import static com.example.vivah.activities.CreateProfile1.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.vivah.R;
import com.example.vivah.databinding.ActivityCreateProfile7Binding;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Objects;

public class CreateProfile7 extends AppCompatActivity {
    ActivityCreateProfile7Binding binding;
    PreferenceManager preferenceManager;
    int incomeType; //  1 - monthly    2 - yearly

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfile7Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){

            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_USER)
                    .document(preferenceManager.getString(Constants.KEY_USER_ID))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                           binding.highestQualification.setText(documentSnapshot.getString(Constants.KEY_HIGHEST_QUALIFICATION));
                           binding.job.setText(documentSnapshot.getString(Constants.KEY_JOB_DESCRIPTION));
                           binding.income.setText(documentSnapshot.getString(Constants.KEY_INCOME));
                        }
                    });
        }


        ArrayAdapter<String> adapterIncomeMonthly = new ArrayAdapter<>(getApplicationContext(),R.layout.drop_down_item, Constants.monthlyIncome);
        ArrayAdapter<String> adapterIncomeYearly = new ArrayAdapter<>(getApplicationContext(),R.layout.drop_down_item,Constants.yearlyIncome);

        binding.income.setAdapter(adapterIncomeYearly);
        binding.income.setKeyListener(null);

        binding.incomeSelection.setOnCheckedStateChangeListener((group, checkedIds) -> {
            Chip chip = findViewById(group.getCheckedChipId());

            String str;

            if(chip!=null){
                str = chip.getText().toString();
            }else{
                str="";
            }

            Toast.makeText(getApplicationContext(), str,Toast.LENGTH_SHORT).show();



            switch (str) {

                case "":
                    binding.income.setVisibility(View.INVISIBLE);
                    binding.income.setText("");
                      incomeType = 0;
                    break;
                case "Yearly":
                    binding.income.setAdapter(adapterIncomeYearly);
                    binding.income.setVisibility(View.VISIBLE);
                    binding.income.setText("");
                   incomeType = 2;
                    break;
                case "Monthly":
                    binding.income.setAdapter(adapterIncomeMonthly);
                    binding.income.setVisibility(View.VISIBLE);
                    binding.income.setText("");
                    incomeType = 1;
                    break;
            }
        });



        binding.backButton.setOnClickListener(v-> onBackPressed());

        binding.continueButton.setOnClickListener(v -> {



            if(isValidDetail()){
                profile.highestQualification = binding.highestQualification.getText().toString();
                profile.jobDescription = binding.job.getText().toString();
                if(incomeType == 1) {
                    String str = binding.income.getText().toString();
                    for(int i=0;i<Constants.monthlyIncome.length;i++){
                        if(str.equals(Constants.monthlyIncome[i])){
                            profile.income = Constants.yearlyIncome[i];
                            break;
                        }
                    }
                }else{
                    profile.income = binding.income.getText().toString();

                }


                if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {

                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    DocumentReference documentReference =
                            database.collection(Constants.KEY_COLLECTION_USER).document(
                                    preferenceManager.getString(Constants.KEY_USER_ID)
                            );
                    HashMap<String,Object> user = new HashMap<>();
                    user.put(Constants.KEY_CALL_WORD, profile.callWord);
                    user.put(Constants.KEY_CREATED_BY, profile.createBy);
                    user.put(Constants.KEY_GENDER, profile.gender);
                    String name = profile.firstName + " " + profile.lastName;
                    user.put(Constants.KEY_NAME, name );
                    user.put(Constants.KEY_SEARCH,name.toLowerCase());
                    user.put(Constants.KEY_FIRST_NAME, profile.firstName);
                    user.put(Constants.KEY_LAST_NAME, profile.lastName);
                    user.put(Constants.KEY_DATE_OF_DOB, profile.dd);
                    user.put(Constants.KEY_MONTH_OF_DOB, profile.mm);
                    user.put(Constants.KEY_YEAR_OF_DOB, profile.yyyy);
                    user.put(Constants.KEY_STATE, profile.state);
                    user.put(Constants.KEY_DISTRICT, profile.district);
                    user.put(Constants.KEY_FULL_ADDRESS, profile.fullAddress);
                    user.put(Constants.KEY_LIVE_WITH_FAMILY, profile.liveWithFamily);
                    user.put(Constants.KEY_WHERE_FAMILY_LIVE,profile.whereFamilyLive);
                    user.put(Constants.KEY_MARITAL_STATUS, profile.maritalStatus);
                    user.put(Constants.KEY_DIET, profile.diet);
                    user.put(Constants.KEY_HEIGHT, profile.height);
                    user.put(Constants.KEY_WEIGHT, profile.weight);
                    user.put(Constants.KEY_MOTHER_TONGUE, profile.motherTongue);
                    user.put(Constants.KEY_HIGHEST_QUALIFICATION, profile.highestQualification);
                    user.put(Constants.KEY_JOB_DESCRIPTION, profile.jobDescription);
                    user.put(Constants.KEY_INCOME, profile.income);
                    user.put(Constants.KEY_CASTE, profile.caste);
                    user.put(Constants.KEY_SUB_CASTE, profile.subCast);
                    user.put(Constants.KEY_GOTRA, profile.gotra);
                    user.put(Constants.KEY_FATHER_NAME, profile.fatherName);
                    user.put(Constants.KEY_FATHER_OCCUPATION, profile.fatherOccupation);
                    user.put(Constants.KEY_MOTHER_NAME, profile.motherName);
                    user.put(Constants.KEY_MOTHER_OCCUPATION, profile.motherOccupation);
                    documentReference.update(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    startActivity(new Intent(CreateProfile7.this, AddImageActivity.class));

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateProfile7.this,"update Successfully",Toast.LENGTH_SHORT).show();

                                }
                            });

                }else{
                    Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
                    intent.putExtra("type", "setPassword");
                    startActivity(intent);
                }
            }
        });




    }



    private void showMessage(String message,Integer Id,Integer layoutId){
        TextInputEditText editText = findViewById(Id);
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

    public boolean isValidDetail(){
        if(Objects.requireNonNull(binding.highestQualification.getText()).toString().trim().isEmpty()){
            showMessage("enter highest Qualification",binding.highestQualification.getId(),binding.highestQualificationLayout.getId());
            return  false;
        }else if(Objects.requireNonNull(binding.job.getText()).toString().trim().isEmpty()){
            showMessage("enter jobDescription",binding.job.getId(),binding.jobLayout.getId());
            return false;
        }else if(binding.income.getText().toString().trim().isEmpty()){
            showMessage("enter income carefully",binding.income.getId(),binding.incomeLayout.getId());
            return false;
        }else{
            return true;
        }
    }
}
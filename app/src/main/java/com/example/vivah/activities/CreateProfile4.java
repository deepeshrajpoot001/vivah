package com.example.vivah.activities;

import static com.example.vivah.activities.CreateProfile1.profile;

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
import com.example.vivah.databinding.ActivityCreateProfile4Binding;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class CreateProfile4 extends AppCompatActivity {
    ActivityCreateProfile4Binding binding;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfile4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        profile.liveWithFamily = "YES";
        binding.whereFamilyLive.setText("live with family");
        preferenceManager = new PreferenceManager(getApplicationContext());

        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){

            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_USER)
                    .document(preferenceManager.getString(Constants.KEY_USER_ID))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                           binding.maritalStatus.setText(documentSnapshot.getString(Constants.KEY_MARITAL_STATUS));
                           binding.diet.setText(documentSnapshot.getString(Constants.KEY_DIET));
                           binding.height.setText(documentSnapshot.getString(Constants.KEY_HEIGHT));
                           binding.weight.setText(documentSnapshot.getString(Constants.KEY_WEIGHT));
                           binding.motherTongue.setText(documentSnapshot.getString(Constants.KEY_MOTHER_TONGUE));
                           if(Objects.equals(documentSnapshot.getString(Constants.KEY_LIVE_WITH_FAMILY), "NO")){
                               binding.no.setChecked(true);
                               binding.whereFamilyLive.setText(documentSnapshot.getString(Constants.KEY_WHERE_FAMILY_LIVE));
                           }
                        }
                    });
        }


        String[] motherTongue = getResources().getStringArray(R.array.languages);




        binding.liveWithFamilySelection.setOnCheckedStateChangeListener((group, checkedIds) -> {

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
                    profile.liveWithFamily = "";
                    break;
                case "NO":
                    profile.liveWithFamily = "NO";
                    binding.whereFamilyLive.setText("");
                    binding.whereFamilyLiveLayout.setVisibility(View.VISIBLE);
                    binding.whereFamilyLive.setVisibility(View.VISIBLE);
                    break;
                case "YES":
                    profile.liveWithFamily = "YES";
                    binding.whereFamilyLiveLayout.setVisibility(View.GONE);
                    binding.whereFamilyLive.setVisibility(View.GONE);
                    String address = "live with family";
                    binding.whereFamilyLive.setText(address);

                    break;
            }


        });

        ArrayAdapter<String> adapterMaritalStatus = new ArrayAdapter<>(getApplicationContext(),R.layout.drop_down_item, Constants.maritalStatus);
        ArrayAdapter<String> adapterDiet = new ArrayAdapter<>(getApplicationContext(),R.layout.drop_down_item, Constants.diet);
        ArrayAdapter<String> adapterHeight = new ArrayAdapter<>(getApplicationContext(),R.layout.drop_down_item, Constants.height);
        ArrayAdapter<String>  adapterWeight = new ArrayAdapter<>(getApplicationContext(),R.layout.drop_down_item, Constants.weight);
        ArrayAdapter<String>   adapterMotherTongue = new ArrayAdapter<>(getApplicationContext(),R.layout.drop_down_item, motherTongue);

        binding.maritalStatus.setAdapter(adapterMaritalStatus);
        binding.diet.setAdapter(adapterDiet);
        binding.height.setAdapter(adapterHeight);
        binding.weight.setAdapter(adapterWeight);
        binding.motherTongue.setAdapter(adapterMotherTongue);


        binding.maritalStatus.setOnClickListener(view -> {
            if(!binding.maritalStatus.getText().toString().isEmpty())
            binding.maritalStatus.setText("");
        });

        binding.diet.setOnClickListener(view -> {
            if(!binding.diet.getText().toString().isEmpty())
                binding.diet.setText("");
        });

        binding.height.setOnClickListener(view -> {
            if(!binding.height.getText().toString().isEmpty())
                binding.height.setText("");
        });

        binding.weight.setOnClickListener(view -> {
            if(!binding.weight.getText().toString().isEmpty())
                binding.weight.setText("");
        });

        binding.motherTongue.setOnClickListener(view -> {
            if(!binding.motherTongue.getText().toString().isEmpty())
                binding.motherTongue.setText("");
        });


        binding.maritalStatus.setKeyListener(null);
        binding.diet.setKeyListener(null);
        binding.height.setKeyListener(null);
        binding.weight.setKeyListener(null);
        binding.motherTongue.setKeyListener(null);

        binding.backButton.setOnClickListener(v-> onBackPressed());

        binding.continueButton.setOnClickListener(v -> {
            if(isValidDetail()){
                profile.maritalStatus = binding.maritalStatus.getText().toString();
                profile.diet= binding.diet.getText().toString();
                profile.height = binding.height.getText().toString();
                profile.weight = binding.weight.getText().toString();
                profile.motherTongue = binding.motherTongue.getText().toString();
                profile.whereFamilyLive = binding.whereFamilyLive.getText().toString();
                startActivity(new Intent(getApplicationContext(),CreateProfile5.class));
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

    public boolean isValidDetail() {

        if (binding.maritalStatus.getText().toString().trim().isEmpty()) {
            showMessage("enter marital status",binding.maritalStatus.getId(),binding.maritalStatusLayout.getId());
            return false;
        } else if (binding.diet.getText().toString().trim().isEmpty()) {
            showMessage("enter diet",binding.diet.getId(),binding.dietLayout.getId());
            return false;
        } else if (binding.height.getText().toString().trim().isEmpty()) {
            showMessage("enter height",binding.height.getId(),binding.heightLayout.getId());
            return false;
        } else if (binding.weight.getText().toString().trim().isEmpty()) {
            showMessage("enter weight",binding.weight.getId(),binding.weightLayout.getId());
            return false;
        } else if (binding.motherTongue.getText().toString().trim().isEmpty()) {
            showMessage("enter mother tongue",binding.motherTongue.getId(),binding.motherTongueLayout.getId());
            return false;
        }else if(Objects.requireNonNull(binding.whereFamilyLive.getText()).toString().trim().isEmpty()){
            showMessage("enter where family lives",binding.whereFamilyLive.getId(),binding.whereFamilyLiveLayout.getId());
            return  false;
        }
        else {
            return true;
        }
    }

}
package com.example.vivah.activities;

import static com.example.vivah.activities.CreateProfile1.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vivah.R;
import com.example.vivah.databinding.ActivityCreateProfile5Binding;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class CreateProfile5 extends AppCompatActivity {

    ActivityCreateProfile5Binding  binding;
    private  PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityCreateProfile5Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        preferenceManager = new PreferenceManager(getApplicationContext());

        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){

            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_USER)
                    .document(preferenceManager.getString(Constants.KEY_USER_ID))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            binding.caste.setText(documentSnapshot.getString(Constants.KEY_CASTE));
                            binding.subCaste.setText(documentSnapshot.getString(Constants.KEY_SUB_CASTE));
                            binding.gotra.setText(documentSnapshot.getString(Constants.KEY_GOTRA));
                            binding.fatherName.setText(documentSnapshot.getString(Constants.KEY_FATHER_NAME));
                            binding.fatherOccupation.setText(documentSnapshot.getString(Constants.KEY_FATHER_OCCUPATION));
                            binding.motherName.setText(documentSnapshot.getString(Constants.KEY_MOTHER_NAME));
                            binding.motherOccupation.setText(documentSnapshot.getString(Constants.KEY_MOTHER_OCCUPATION));
                        }
                    });
        }




        binding.backButton.setOnClickListener(v-> onBackPressed());





        binding.finishButton.setOnClickListener(v -> {
            if(isValidDetail()){
                loading(true);
                profile.caste = binding.caste.getText().toString();
                profile.subCast = binding.subCaste.getText().toString();
                profile.gotra = binding.gotra.getText().toString();
                profile.fatherName = binding.fatherName.getText().toString();
                profile.fatherOccupation = binding.fatherOccupation.getText().toString();
                profile.motherName = binding.motherName.getText().toString();
                profile.motherOccupation = binding.motherOccupation.getText().toString();
                Intent intent = new Intent(getApplicationContext(), CreateProfile7.class);
                startActivity(intent);
                loading(false);
            }
        });

    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.finishButton.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.finishButton.setVisibility(View.VISIBLE);
        }
    }


    private void showMessage(String message,Integer Id){
        TextInputEditText editText = findViewById(Id);
        editText.setError(message);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setError(null);
                editText.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isValidDetail(){
        if(binding.caste.getText().toString().trim().isEmpty()){
            showMessage("enter caste",binding.caste.getId());
            return false;
        }else if(binding.subCaste.getText().toString().trim().isEmpty()){
            showMessage("enter subCaste",binding.subCaste.getId());
            return false;
        }else if(binding.gotra.getText().toString().trim().isEmpty()){

            showMessage("enter gotra",binding.gotra.getId());
            return false;
        }else if(binding.fatherName.getText().toString().trim().isEmpty()){
            showMessage("enter father name",binding.fatherName.getId());
              return false;
        }else if(binding.fatherOccupation.getText().toString().trim().isEmpty()){
            showMessage("enter father occupation",binding.fatherOccupation.getId());
            return false;
        }else if(binding.motherName.getText().toString().trim().isEmpty()){
            showMessage("enter mother name",binding.motherName.getId());
            return false;
        }else if(binding.motherOccupation.getText().toString().trim().isEmpty()){
            showMessage("enter mother occupation",binding.motherOccupation.getId());
            return false;
        }else{
            return true;
        }


    }





}
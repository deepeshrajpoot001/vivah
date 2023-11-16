package com.example.vivah.activities;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import com.example.vivah.databinding.ActivitySignInBinding;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Year;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        preferenceManager = new PreferenceManager(getApplicationContext());
       if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }



        setListener();
    }
    private void setListener(){
        binding.textCreateNewAccount.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this,CreateProfile1.class);
            startActivity(intent);
        });
        binding.ForgetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),VerificationActivity.class);
            intent.putExtra("type","forgetPassword");
            startActivity(intent);

        });
        binding.buttonSignIn.setOnClickListener(v->{
            if(isValidSignInDetails()){
                signIn();
            }
        });
    }

    private void signIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USER)
                .whereEqualTo(Constants.KEY_MOBILE_NO,binding.inputMobile.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD,binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocumentChanges().size()>0){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                        preferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME,documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_FIRST_NAME,documentSnapshot.getString(Constants.KEY_FIRST_NAME) );
                        preferenceManager.putString(Constants.KEY_LAST_NAME, documentSnapshot.getString(Constants.KEY_LAST_NAME));
                        preferenceManager.putString(Constants.KEY_MOBILE_NO,documentSnapshot.getString(Constants.KEY_MOBILE_NO));
                        preferenceManager.putString(Constants.KEY_CALL_WORD,documentSnapshot.getString(Constants.KEY_CALL_WORD));
                        preferenceManager.putString(Constants.KEY_GENDER,documentSnapshot.getString(Constants.KEY_GENDER));
                        preferenceManager.putString(Constants.KEY_PLACE_OF_BIRTH,documentSnapshot.getString(Constants.KEY_DISTRICT)+","+documentSnapshot.getString(Constants.KEY_STATE));
                        preferenceManager.putString(Constants.KEY_AGE,  (Year.now().getValue() - Integer.parseInt(String.valueOf(documentSnapshot.get(Constants.KEY_YEAR_OF_DOB)))) +" year old");
                        preferenceManager.putString(Constants.KEY_JOB_DESCRIPTION,documentSnapshot.getString(Constants.KEY_JOB_DESCRIPTION));

                        if(Integer.parseInt(String.valueOf(documentSnapshot.get(Constants.KEY_PROFILE_PROGRESS)))<3){
                            Intent intent = new Intent(getApplicationContext(),AddImageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            preferenceManager.putString(Constants.KEY_PROFILE_IMAGE, documentSnapshot.getString(Constants.KEY_PROFILE_IMAGE));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                    }else{
                        loading(false);
                        showToast("Unable to sign in");
                    }
                });

    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails(){
        if(binding.inputMobile.getText().toString().trim().isEmpty()){
            showToast("enter mobile no");
            return false;
        }else if(binding.inputMobile.getText().toString().length()>10){
                showToast("enter valid mobile no");
            return false;
        }else if(binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        }else{
            return true;
        }
    }






























}
package com.example.vivah.activities;


import static com.example.vivah.activities.CreateProfile1.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.example.vivah.databinding.ActivitySetPasswordBinding;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Year;
import java.util.Objects;

public class SetPassword extends AppCompatActivity {
    ActivitySetPasswordBinding binding;
    private String activityWork;
    PreferenceManager preferenceManager;
    private String mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        activityWork = getIntent().getStringExtra("type");
        if(Objects.equals(activityWork, "forgetPassword")){
            binding.layoutSetPassword.setHint("Set New Password");
            binding.layoutReTypePassword.setHint("Re-type New Password");
        }

        binding.backButton.setOnClickListener(v-> onBackPressed());

        binding.doneButton.setOnClickListener(v -> {
            if(isAbleToDone()){

                if(Objects.equals(activityWork, "setPassword")) {
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    DocumentReference documentReference =
                            database.collection(Constants.KEY_COLLECTION_USER).document(preferenceManager.getString(Constants.KEY_USER_ID));
                    documentReference.update(
                            Constants.KEY_PASSWORD, binding.setPasswordText.getText().toString(),
                            Constants.KEY_PROFILE_PROGRESS,2
                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            startActivity(new Intent(getApplicationContext(), AddImageActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("failed to set password");
                        }
                    });


                }else if(Objects.equals(activityWork, "forgetPassword")){

                   mobileNo = getIntent().getStringExtra("mobileNo");

                    FirebaseFirestore database2 = FirebaseFirestore.getInstance();
                    database2.collection(Constants.KEY_COLLECTION_USER)
                            .whereEqualTo(Constants.KEY_MOBILE_NO,mobileNo)
                            .get()
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful() && task.getResult() != null
                                        && task.getResult().getDocumentChanges().size()>0){

                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                                    preferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.getId());
                                    preferenceManager.putString(Constants.KEY_FIRST_NAME,documentSnapshot.getString(Constants.KEY_FIRST_NAME) );
                                    preferenceManager.putString(Constants.KEY_NAME,profile.firstName+" "+Constants.KEY_LAST_NAME);
                                    preferenceManager.putString(Constants.KEY_LAST_NAME, documentSnapshot.getString(Constants.KEY_LAST_NAME));
                                    preferenceManager.putString(Constants.KEY_MOBILE_NO,documentSnapshot.getString(Constants.KEY_MOBILE_NO));
                                    preferenceManager.putString(Constants.KEY_GENDER,documentSnapshot.getString(Constants.KEY_GENDER));
                                    preferenceManager.putString(Constants.KEY_CALL_WORD,documentSnapshot.getString(Constants.KEY_CALL_WORD));
                                    preferenceManager.putString(Constants.KEY_PLACE_OF_BIRTH,documentSnapshot.getString(Constants.KEY_DISTRICT)+","+documentSnapshot.getString(Constants.KEY_STATE));
                                    preferenceManager.putString(Constants.KEY_AGE,  (Year.now().getValue() - Integer.parseInt(String.valueOf(documentSnapshot.get(Constants.KEY_YEAR_OF_DOB)))) +" year old");
                                    preferenceManager.putString(Constants.KEY_JOB_DESCRIPTION,documentSnapshot.getString(Constants.KEY_JOB_DESCRIPTION));





                                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                                    DocumentReference documentReference =
                                            database.collection(Constants.KEY_COLLECTION_USER).document(preferenceManager.getString(Constants.KEY_USER_ID));
                                    documentReference.update(
                                            Constants.KEY_PASSWORD, binding.setPasswordText.getText().toString()

                                    );

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

                                    Toast.makeText(getApplicationContext(),"unable to verify",Toast.LENGTH_SHORT).show();
                                }
                            });


                }

            }else{
                Toast.makeText(getApplicationContext(),"fill valid information",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();

    }

    private boolean isAbleToDone(){
        if(binding.setPasswordText.getText().toString().trim().isEmpty()){
            showMessage("input password");
            return false;
        }else if(binding.reTypePasswordText.getText().toString().trim().isEmpty()){
            showMessage("retype password");
            return  false;
        }else if(!binding.setPasswordText.getText().toString().equals(binding.reTypePasswordText.getText().toString())){
            showMessage("password is not match");
            return false;
        }else{
            return true;
        }

    }
}
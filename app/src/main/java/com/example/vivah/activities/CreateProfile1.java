package com.example.vivah.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.vivah.databinding.ActivityCreateProfile1Binding;
import com.example.vivah.models.Profile;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class CreateProfile1 extends AppCompatActivity {

     ActivityCreateProfile1Binding binding;
     public static Profile profile;
     private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfile1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        profile = new Profile();
         profile.callWord = "His";
         profile.gender = "Male";
        profile.liveWithFamily = "yes";
        profile.createBy = "Self";
        preferenceManager = new PreferenceManager(getApplicationContext());


       if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
           FirebaseFirestore database = FirebaseFirestore.getInstance();
           database.collection(Constants.KEY_COLLECTION_USER)
                   .document(preferenceManager.getString(Constants.KEY_USER_ID))
                   .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                       @Override
                       public void onSuccess(DocumentSnapshot documentSnapshot) {

                           profile.callWord = documentSnapshot.getString(Constants.KEY_CALL_WORD);
                           profile.gender = documentSnapshot.getString(Constants.KEY_GENDER);
                           profile.createBy = documentSnapshot.getString(Constants.KEY_CREATED_BY);
                           if (profile.gender.equals("Female")) {
                               binding.female.setChecked(true);
                           }
                           assert profile.createBy != null;
                           switch (profile.createBy) {
                               case "Parent":
                                   if (profile.gender.equals("Female")) {
                                       binding.myDaughter.setChecked(true);
                                   } else {
                                       binding.mySon.setChecked(true);
                                   }
                                   break;
                               case "Sibling":
                                   if (profile.gender.equals("Female")) {
                                       binding.mySister.setChecked(true);
                                   } else {
                                       binding.myBrother.setChecked(true);
                                   }
                                   break;
                               case "Relative":
                                   binding.myRelative.setChecked(true);
                                   break;
                               case "Friend":
                                   binding.myFriend.setChecked(true);
                                   break;
                           }

                       }
                   });
       }



        chooseGender();
         callingWordSelection();

        binding.backButton.setOnClickListener(v-> onBackPressed());

       binding.continueButton.setOnClickListener(v -> {
           if (!(profile.callWord.isEmpty() || profile.gender.isEmpty())) {


                   startActivity(new Intent(getApplicationContext(), CreateProfile2.class));




           }else{
               Toast.makeText(getApplicationContext(),"fill details carefully",Toast.LENGTH_LONG).show();
           }
       });
    }


private void callingWordSelection(){
    binding.profileSelection.setOnCheckedStateChangeListener((group, checkedId) -> {

        Chip chip = findViewById(group.getCheckedChipId());

        String str;

        if(chip!=null){
            str = chip.getText().toString();
        }else{
            str="";
        }

        Toast.makeText(getApplicationContext(), str,Toast.LENGTH_SHORT).show();



        switch (str){
            case "My Son" :      {
                profile.createBy ="Parent";
                profile.callWord = "His"; profile.gender= "Male";
                binding.titleText2.setVisibility(View.INVISIBLE);
                binding.genderSelection.setVisibility(View.INVISIBLE);
                break;

            }
            case "My Brother" : {
                profile.createBy ="Sibling";
                profile.callWord = "His"; profile.gender= "Male";
                binding.titleText2.setVisibility(View.INVISIBLE);
                binding.genderSelection.setVisibility(View.INVISIBLE);
                break;
            }
            case "My Daughter" :{
                profile.createBy="Parent";
                profile.callWord = "Her"; profile.gender = "Female";
                binding.titleText2.setVisibility(View.INVISIBLE);
                binding.genderSelection.setVisibility(View.INVISIBLE);
                break;}
            case "My Sister" : {
                profile.createBy="Sibling";
                profile.callWord = "Her"; profile.gender = "Female";
                binding.titleText2.setVisibility(View.INVISIBLE);
                binding.genderSelection.setVisibility(View.INVISIBLE);
                break;}
            case "My Friend" : {
                profile.createBy = "Friend";
                if(profile.callWord.isEmpty()) {
                    profile.callWord = "";

                }
                binding.titleText2.setVisibility(View.VISIBLE);
                binding.genderSelection.setVisibility(View.VISIBLE);
                break;

            }
            case "My Relative" : {
                profile.createBy="Relative";
                if(profile.callWord.isEmpty()) {
                    profile.callWord = "";

                }
                binding.titleText2.setVisibility(View.VISIBLE);
                binding.genderSelection.setVisibility(View.VISIBLE);
                break;

            }
            case "My Self" : {
                profile.createBy= "Self";
                profile.callWord = "Your";
                binding.titleText2.setVisibility(View.VISIBLE);
                binding.genderSelection.setVisibility(View.VISIBLE);
                break;}
            case "" : {
                profile.createBy = "";
                profile.callWord = "";
                binding.titleText.setVisibility(View.INVISIBLE);
                binding.genderSelection.setVisibility(View.INVISIBLE);
            }

        }


    });
    }


    private void chooseGender(){

        binding.genderSelection.setOnCheckedStateChangeListener((group, checkedIds) -> {

            Chip chip = findViewById(group.getCheckedChipId());

            String str;

            if(chip!=null){
                str = chip.getText().toString();
            }else{
                str="";
            }

            Toast.makeText(getApplicationContext(), str,Toast.LENGTH_SHORT).show();

            switch (str){

                case "" : profile.gender = ""; break;
                case "Male" : profile.gender ="Male"; profile.callWord = "His"; break;
                case "Female": profile.gender = "Female"; profile.callWord = "Her"; break;
            }


        });
    }
}
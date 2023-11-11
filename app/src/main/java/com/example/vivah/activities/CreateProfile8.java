package com.example.vivah.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.vivah.R;
import com.example.vivah.databinding.ActivityCreateProfile8Binding;
import com.example.vivah.models.FamilyMember;
import com.example.vivah.models.Profile;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class CreateProfile8 extends AppCompatActivity {

    private ActivityCreateProfile8Binding binding;

    int i;
    private FirebaseFirestore database;
    private PreferenceManager preferenceManager;
    private ArrayList<String> hobbies;
    private ArrayList<String> pHobbies;
    private   int progress = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfile8Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hobbies = new ArrayList<>();
        preferenceManager = new PreferenceManager(getApplicationContext());

     i=0;

        for(int i=0;i< Constants.hobbies.length;i++) {

            Chip chip = new Chip(CreateProfile8.this);
            chip.setId(i);
            chip.setId(ViewCompat.generateViewId());
            chip.setCheckable(true);
            chip.setText(Constants.hobbies[i]);
            chip.setTextSize(20);
            chip.setTextColor(getColor(R.color.green));
            chip.setChipBackgroundColor(getColorStateList(R.color.chipBackgroundColor));
            binding.hobbiesGroup.addView(chip);
        }



     binding.nextButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(i<5){
                 Toast.makeText(getApplicationContext(),"please add 5 hobbies",Toast.LENGTH_SHORT).show();
             }else{

                 database = FirebaseFirestore.getInstance();
                 DocumentReference documentReference =
                         database.collection(Constants.KEY_COLLECTION_USER).document(preferenceManager.getString(Constants.KEY_USER_ID));
                 documentReference.update(
                         Constants.KEY_HOBBIES,hobbies,
                         Constants.KEY_PROFILE_PROGRESS,progress
                 );

                 Intent intent = new Intent(getApplicationContext(), CreateProfile9.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 startActivity(intent);
             }
         }
     });






        binding.selectedHobbiesGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {


            Chip chip = findViewById(group.getCheckedChipId());
            if(chip!=null){
                binding.selectedHobbiesGroup.removeView(chip);
                binding.hobbiesGroup.addView(chip);
                chip.setChipIcon(null);
                chip.setChipStrokeWidth(0);
                binding.hobbiesGroup.clearCheck();
                i--;

                for(int i=0;i<hobbies.size();i++){
                    if(hobbies.get(i) ==chip.getText()){
                        hobbies.remove(i);
                        break;
                    }
                }



            }
            Toast.makeText(getApplicationContext(),hobbies.size()+"",Toast.LENGTH_SHORT).show();

        });

       binding.hobbiesGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {

           if(i>=5){
               binding.hobbiesGroup.clearCheck();
               return;
           }

           Chip chip = findViewById(group.getCheckedChipId());


           if(chip!=null){
               binding.hobbiesGroup.removeView(chip);
               binding.selectedHobbiesGroup.addView(chip);
               chip.setChipIcon(AppCompatResources.getDrawable(getApplicationContext(),R.drawable.ic_circle));
               chip.setChipIconTintResource(R.color.green);
               chip.setChipStrokeWidth(5);
               chip.setChipStrokeColorResource(R.color.green);
               hobbies.add(chip.getText().toString());
               binding.selectedHobbiesGroup.clearCheck();
               i++;
           }



       });
     }


    @Override
    protected void onStart() {
        super.onStart();
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){

            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_USER)
                    .document(preferenceManager.getString(Constants.KEY_USER_ID))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.get(Constants.KEY_PROFILE_PROGRESS).toString().equals("6")) {
                                progress = 6;
                                Profile pr = documentSnapshot.toObject(Profile.class);
                                assert pr != null;
                                ArrayList<String> h = pr.hobbies;
                                for(int k=0;k<Constants.hobbies.length;k++){
                                    for(int m=0;m<h.size();m++){
                                        if(Constants.hobbies[k].equals(h.get(m))){
                                            Chip chip = findViewById(k);
                                            chip.setChecked(true);
                                        }
                                    }
                                }

                            }
                        }
                    });
        }


    }
}
package com.example.vivah.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.vivah.databinding.ActivityCreateProfile9Binding;
import com.example.vivah.models.Profile;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.Random;


public class CreateProfile9 extends AppCompatActivity {

    ActivityCreateProfile9Binding binding;
    PreferenceManager preferenceManager;
    String about;
    private FirebaseFirestore database1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfile9Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USER)
                .document(currentUserId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot documentSnapshot = task.getResult();
                        Profile pr = documentSnapshot.toObject(Profile.class);

                        String callWord = pr.callWord;

                        String jobDescription =pr.jobDescription;
                        String hobbiesSentence="ZZZ hobbies are ";


                        if(pr.hobbies!=null){
                            for(String str : pr.hobbies){
                                hobbiesSentence = hobbiesSentence+ str +", ";

                            }
                        }



                        Random random = new Random();
                        int x = random.nextInt(11);

                        int y;

                        do {
                            y = random.nextInt(11);

                        } while (y == x);


                        about = Constants.aboutSentence[x] +" XXX working as "+jobDescription+ ". "+ hobbiesSentence+Constants.aboutSentence[y];



                        if(callWord.equals("Your")) {
                          about =  about.replaceAll("XXX","I am");
                            about =  about.replaceAll("YYY","me");
                            about =  about.replaceAll("ZZZ","My");
                            about =  about.replaceAll("VVV","I");
                            about =  about.replaceAll("WWW","I have");
                        }else if(callWord.equals("His")){
                            about = about.replaceAll("XXX","He is");
                            about = about.replaceAll("YYY","his");
                            about = about.replaceAll("ZZZ","his");
                            about = about.replaceAll("VVV","He");
                            about = about.replaceAll("WWW","He Has");
                        }else if(callWord.equals("Her")){
                            about = about.replaceAll("XXX","She is");
                            about = about.replaceAll("YYY","her");
                            about =  about.replaceAll("ZZZ","her");
                            about =  about.replaceAll("VVV","She");
                            about =  about.replaceAll("WWW","She has");

                        }




                        if(documentSnapshot.get(Constants.KEY_PROFILE_PROGRESS).toString().equals("6")) {
                            binding.aboutYourSelfEditText.setText(documentSnapshot.getString(Constants.KEY_ABOUT_YOURSELF));

                        }else {
                            binding.aboutYourSelfEditText.setText(about);
                        }



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"network Error",Toast.LENGTH_SHORT).show();
                    }
                });

        binding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.aboutYourSelfEditText.getText().toString().isEmpty()) {
                    about = binding.aboutYourSelfEditText.getText().toString();
                    database1 = FirebaseFirestore.getInstance();
                    DocumentReference documentReference =
                            database1.collection(Constants.KEY_COLLECTION_USER).document(preferenceManager.getString(Constants.KEY_USER_ID));
                    documentReference.update(
                            Constants.KEY_ABOUT_YOURSELF,about,
                            Constants.KEY_PROFILE_PROGRESS,6
                    );
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);


                }else{
                    Toast.makeText(getApplicationContext(),"write about yourself",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


}
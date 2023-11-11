package com.example.vivah.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.vivah.adapters.FamilyMemberAdapter;

import com.example.vivah.databinding.ActivityCreateProfile6Binding;
import com.example.vivah.databinding.DialogueLayoutAddFamilyMemberBinding;
import com.example.vivah.models.FamilyMember;
import com.example.vivah.models.Profile;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CreateProfile6 extends AppCompatActivity {
    ActivityCreateProfile6Binding binding;

    private FamilyMemberAdapter familyMemberAdapter;
    private PreferenceManager preferenceManager;
   private  List<FamilyMember> familyMembers;
    private   int progress = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfile6Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        familyMembers = new ArrayList<>();
        familyMemberAdapter = new FamilyMemberAdapter(familyMembers);
        binding.familyMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.familyMemberRecyclerView.setAdapter(familyMemberAdapter);




        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(familyMembers.size()>0){
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    DocumentReference documentReference =
                            database.collection(Constants.KEY_COLLECTION_USER).document(preferenceManager.getString(Constants.KEY_USER_ID));
                    documentReference.update(
                            Constants.KEY_FAMILY_MEMBER,familyMembers,
                            Constants.KEY_PROFILE_PROGRESS,progress
                    );
                    Intent intent = new Intent(getApplicationContext(), CreateProfile8.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"at least add one Family Member",Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(CreateProfile6.this);
                DialogueLayoutAddFamilyMemberBinding dbinding = DialogueLayoutAddFamilyMemberBinding.inflate(getLayoutInflater());
                dialog.setContentView(dbinding.getRoot());

                dbinding.AddButtonInDialogue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dbinding.memberNameEditText.getText().toString().trim().isEmpty()||
                                dbinding.memberRelationEditText.getText().toString().trim().isEmpty()||
                                dbinding.memberOccupationEditText.getText().toString().trim().isEmpty()){
                            Toast.makeText(getApplicationContext(),"fill all detail carefully",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        FamilyMember fm = new FamilyMember(dbinding.memberNameEditText.getText().toString(),
                                dbinding.memberRelationEditText.getText().toString(),
                                dbinding.memberOccupationEditText.getText().toString());

                        familyMembers.add(fm);
                        familyMemberAdapter.notifyDataSetChanged();
                        binding.familyMemberRecyclerView.setVisibility(View.VISIBLE);
                        familyMemberAdapter.notifyItemInserted(familyMembers.size()-1 );
                        binding.familyMemberRecyclerView.scrollToPosition(familyMembers.size()-1);


                        dialog.dismiss();

                    }
                });

                dialog.show();

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
                                List<FamilyMember>  s = pr.familyMember;
                                familyMembers.addAll(s);
                                familyMemberAdapter.notifyDataSetChanged();
                                binding.familyMemberRecyclerView.setVisibility(View.VISIBLE);
                                familyMemberAdapter.notifyItemInserted(familyMembers.size()-1 );
                                binding.familyMemberRecyclerView.scrollToPosition(familyMembers.size()-1);




                            }
                        }
                    });
        }
    }
}
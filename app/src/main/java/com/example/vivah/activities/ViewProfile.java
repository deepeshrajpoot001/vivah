package com.example.vivah.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.bumptech.glide.Glide;
import com.example.vivah.R;
import com.example.vivah.adapters.FamilyMemberAdapter;
import com.example.vivah.databinding.ActivityViewProfileBinding;
import com.example.vivah.models.FamilyMember;
import com.example.vivah.models.Profile;
import com.example.vivah.models.ProfileMatchesFrag;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ViewProfile extends AppCompatActivity {

    ActivityViewProfileBinding binding;
    String requiredProfileId,requiredProfileName;
    PreferenceManager preferenceManager;

    private FamilyMemberAdapter familyMemberAdapter;
    private List<FamilyMember> familyMembers;
    private ProfileMatchesFrag profileMatchesFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        profileMatchesFrag= new ProfileMatchesFrag();




        if(getIntent().getStringExtra("type").equals("requestSent")){
            binding.connectNowButton.setVisibility(View.GONE);
        }
       preferenceManager = new PreferenceManager(getApplicationContext());

       requiredProfileId = getIntent().getStringExtra("requiredProfileId");
        requiredProfileName = getIntent().getStringExtra("requiredProfileName");

        familyMembers = new ArrayList<>();
        familyMemberAdapter = new FamilyMemberAdapter(familyMembers);
        binding.familyMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.familyMemberRecyclerView.setAdapter(familyMemberAdapter);






       setUserData();


        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(requiredProfileName);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }








        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.image_for_matching_fragment);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                if(palette!=null){
                    binding.collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(androidx.appcompat.R.attr.colorPrimary));

                    int h = binding.profileImage.getHeight();
                    ShapeDrawable mDrawable = new ShapeDrawable(new RectShape());
                    mDrawable.getPaint().setShader(new LinearGradient(0, 0, 0, h, palette.getMutedColor(androidx.appcompat.R.attr.colorPrimary), Color.parseColor("#FFFFFF"), Shader.TileMode.REPEAT));
                    binding.profileImage.setBackground(mDrawable);

                }
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private  void setUserData(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USER)
                .document(requiredProfileId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String str;

                        Profile pr = documentSnapshot.toObject(Profile.class);

                        Glide.with(binding.circleImageView).load(Uri.parse(documentSnapshot.getString(Constants.KEY_PROFILE_IMAGE))).into(binding.circleImageView);

                        str = "Created By: "+documentSnapshot.getString(Constants.KEY_CREATED_BY);
                        binding.createdByText.setText(str);

                        str = "Profile ID: "+requiredProfileId;
                        binding.profileIdText.setText(str);

                        profileMatchesFrag.age = String.valueOf(Year.now().getValue() - Integer.parseInt(String.valueOf(documentSnapshot.get(Constants.KEY_YEAR_OF_DOB))));

                        str =  profileMatchesFrag.age +" year old";
                        binding.ageText.setText(str);


                        str = documentSnapshot.getString(Constants.KEY_HEIGHT);

                        String height = "Height "+str.charAt(0)+ "'" +str.charAt(4);
                        if(Character.isDigit(str.charAt(5))){
                            height = height + str.charAt(5);
                        }
                        height = height +"\"";
                        binding.heightText.setText(height);

                        str = "Weight: "+documentSnapshot.getString(Constants.KEY_WEIGHT);
                        binding.weightText.setText(str);

                        str = documentSnapshot.get(Constants.KEY_DATE_OF_DOB)+"/"+
                                documentSnapshot.get(Constants.KEY_MONTH_OF_DOB)+"/"+
                                documentSnapshot.get(Constants.KEY_YEAR_OF_DOB);
                        binding.dateOfBirth.setText(str);

                        binding.meritalStatus.setText(documentSnapshot.getString(Constants.KEY_MARITAL_STATUS));

                        str = documentSnapshot.getString(Constants.KEY_DISTRICT)+","+documentSnapshot.getString(Constants.KEY_STATE);
                        profileMatchesFrag.placeOfBirth = str;
                        binding.livesIn.setText(str);

                        str = "Hindu,"+documentSnapshot.getString(Constants.KEY_MOTHER_TONGUE);
                        binding.relegionAndMotherTongue.setText(str);

                        str = documentSnapshot.getString(Constants.KEY_SUB_CASTE)+","+documentSnapshot.getString(Constants.KEY_GOTRA);
                        binding.subCaste.setText(str);

                        binding.diet.setText(documentSnapshot.getString(Constants.KEY_DIET));
                        binding.mobileNo.setText(documentSnapshot.getString(Constants.KEY_MOBILE_NO));
                        binding.highestQualification.setText(documentSnapshot.getString(Constants.KEY_HIGHEST_QUALIFICATION));
                        binding.jobDescription.setText(documentSnapshot.getString(Constants.KEY_JOB_DESCRIPTION));
                        binding.income.setText(documentSnapshot.getString(Constants.KEY_INCOME));
                        binding.aboutText.setText(documentSnapshot.getString(Constants.KEY_ABOUT_YOURSELF));


                        FamilyMember fm = new FamilyMember(documentSnapshot.getString(Constants.KEY_FATHER_NAME),"Father",documentSnapshot.getString(Constants.KEY_FATHER_OCCUPATION));
                        familyMembers.add(fm);
                        fm = new FamilyMember(documentSnapshot.getString(Constants.KEY_MOTHER_NAME),"Mother",documentSnapshot.getString(Constants.KEY_MOTHER_OCCUPATION));
                        familyMembers.add(fm);


                       // assert pr != null;
                        familyMembers.addAll(pr.familyMember);

                        binding.viewProfileCoordinatorLayout.setVisibility(View.VISIBLE);


                        binding.connectNowButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HashMap<String,Object> inbox = new HashMap<>();
                                inbox.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID));
                                inbox.put(Constants.KEY_SENDER_NAME,preferenceManager.getString(Constants.KEY_FIRST_NAME)+" "+preferenceManager.getString(Constants.KEY_LAST_NAME));
                                inbox.put(Constants.KEY_SENDER_IMAGE,preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
                                inbox.put(Constants.KEY_SENDER_AGE,preferenceManager.getString(Constants.KEY_AGE));
                                inbox.put(Constants.KEY_SENDER_PROFESSION,preferenceManager.getString(Constants.KEY_JOB_DESCRIPTION));
                                inbox.put(Constants.KEY_SENDER_PLACE_OF_BIRTH,preferenceManager.getString(Constants.KEY_PLACE_OF_BIRTH));
                                inbox.put(Constants.KEY_RECEIVER_ID,requiredProfileId);
                                inbox.put(Constants.KEY_RECEIVER_NAME,requiredProfileName);
                                inbox.put(Constants.KEY_RECEIVER_IMAGE,documentSnapshot.getString(Constants.KEY_PROFILE_IMAGE));
                                inbox.put(Constants.KEY_RECEIVER_AGE,profileMatchesFrag.age);
                                inbox.put(Constants.KEY_RECEIVER_PROFESSION,documentSnapshot.getString(Constants.KEY_JOB_DESCRIPTION));
                                inbox.put(Constants.KEY_RECEIVER_PLACE_OF_BIRTH,profileMatchesFrag.placeOfBirth);
                                inbox.put(Constants.KEY_STATUS,"send");
                                inbox.put(Constants.KEY_TIMESTAMP,new Date());
                                database.collection(Constants.KEY_COLLECTION_INBOX)
                                        .add(inbox)
                                        .addOnSuccessListener(documentReference -> {
                                            binding.connectNowButton.setText("Request sent");
                                            binding.connectNowButton.setClickable(false);

                                        });


                            }
                        });






                        familyMemberAdapter.notifyDataSetChanged();
                        familyMemberAdapter.notifyItemInserted(familyMembers.size()-1 );





                    }
                });


    }
}
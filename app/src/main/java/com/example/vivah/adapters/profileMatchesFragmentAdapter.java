package com.example.vivah.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vivah.activities.ViewProfile;
import com.example.vivah.databinding.ItemContainerProfileForMatchesFragmentBinding;
import com.example.vivah.models.ProfileMatchesFrag;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class profileMatchesFragmentAdapter extends RecyclerView.Adapter<profileMatchesFragmentAdapter.ViewHolder> {
    private final List<ProfileMatchesFrag> profileMatchesFrags;
    Context context;



    public profileMatchesFragmentAdapter(List<ProfileMatchesFrag> profileMatchesFrags,Context context) {
        this.profileMatchesFrags = profileMatchesFrags;
        this.context = context;
    }

    @NonNull
    @Override
    public profileMatchesFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerProfileForMatchesFragmentBinding itemContainerProfileForMatchesFragmentBinding = ItemContainerProfileForMatchesFragmentBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return  new ViewHolder(itemContainerProfileForMatchesFragmentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull profileMatchesFragmentAdapter.ViewHolder holder, int position) {
        holder.setProfileMatchesFragData(profileMatchesFrags.get(position));


    }

    @Override
    public int getItemCount() {
        return profileMatchesFrags.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemContainerProfileForMatchesFragmentBinding binding;
        PreferenceManager preferenceManager;
        FirebaseFirestore database;

        public ViewHolder(ItemContainerProfileForMatchesFragmentBinding itemContainerProfileForMatchesFragmentBinding) {
            super(itemContainerProfileForMatchesFragmentBinding.getRoot());
            binding = itemContainerProfileForMatchesFragmentBinding;
            preferenceManager = new PreferenceManager(context);
            database = FirebaseFirestore.getInstance();
        }

        void setProfileMatchesFragData(ProfileMatchesFrag profileMatchesFrag){
            String name = profileMatchesFrag.name;
            binding.textName.setText(name);
            String id = profileMatchesFrag.id;
            String str = profileMatchesFrag.height;
            String height = str.charAt(0)+ "'" +str.charAt(4);
            if(Character.isDigit(str.charAt(5))){
                height = height + str.charAt(5);
            }
            height = height +"\"";

            String ageAndYearText = profileMatchesFrag.age + "yrs, "+ height;
            binding.ageAndYear.setText(ageAndYearText);
            binding.profession.setText(profileMatchesFrag.profession);
            String languageAndCaste = profileMatchesFrag.language +","+ profileMatchesFrag.subCaste;
            binding.languageAndCaste.setText(languageAndCaste);
            binding.placeOfBirth.setText(profileMatchesFrag.placeOfBirth);
            Glide.with(binding.profileImage).load(profileMatchesFrag.profileImage).into(binding.profileImage);
            binding.aboutText.setText(profileMatchesFrag.aboutMe);

            binding.cardViewOFMatchFrag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewProfile.class);
                    intent.putExtra("requiredProfileId",id);
                    intent.putExtra("requiredProfileName",name);
                    intent.putExtra("type","connectNow");
                    context.startActivity(intent);

                }
            });


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
                    inbox.put(Constants.KEY_RECEIVER_ID,profileMatchesFrag.id);
                    inbox.put(Constants.KEY_RECEIVER_NAME,profileMatchesFrag.name);
                    inbox.put(Constants.KEY_RECEIVER_IMAGE,profileMatchesFrag.profileImage);
                    inbox.put(Constants.KEY_RECEIVER_AGE,profileMatchesFrag.age);
                    inbox.put(Constants.KEY_RECEIVER_PROFESSION,profileMatchesFrag.profession);
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



        }
    }
}

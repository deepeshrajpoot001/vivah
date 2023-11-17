package com.example.vivah.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vivah.activities.ViewProfile;
import com.example.vivah.databinding.ItemContainerProfileOfHomeFragmentBinding;
import com.example.vivah.models.ProfileHomeFrag;

import java.util.List;

public class profileHomeFragAdapter extends RecyclerView.Adapter<profileHomeFragAdapter.ViewHolder> {
    private final List<ProfileHomeFrag> profileHomeFrags;
    Context context;

    public profileHomeFragAdapter(List<ProfileHomeFrag> profileHomeFrags, Context context) {
        this.profileHomeFrags = profileHomeFrags;
        this.context = context;
    }

    @NonNull
    @Override
    public profileHomeFragAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerProfileOfHomeFragmentBinding itemContainerProfileOfHomeFragmentBinding = ItemContainerProfileOfHomeFragmentBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(itemContainerProfileOfHomeFragmentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull profileHomeFragAdapter.ViewHolder holder, int position) {
        holder.setProfileHomeFragData(profileHomeFrags.get(position));

    }

    @Override
    public int getItemCount() {
        return profileHomeFrags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemContainerProfileOfHomeFragmentBinding binding;

        public ViewHolder(ItemContainerProfileOfHomeFragmentBinding itemContainerProfileOfHomeFragmentBinding) {
            super(itemContainerProfileOfHomeFragmentBinding.getRoot());
            binding = itemContainerProfileOfHomeFragmentBinding;
        }
        void setProfileHomeFragData(ProfileHomeFrag profileHomeFragdata){
            String name = profileHomeFragdata.name;
            String id = profileHomeFragdata.id;
            binding.name.setText(profileHomeFragdata.name);
            binding.age.setText(profileHomeFragdata.age);
            String str = profileHomeFragdata.height;
            String height;
            int indexOfn = str.indexOf('n');
            if(indexOfn==-1){
                height = str.substring(0,3);
            }else{
                height = str.substring(0,indexOfn+1);
            }
            binding.height.setText(height);
            binding.profession.setText(profileHomeFragdata.profession);
            binding.placeOfBirth.setText(profileHomeFragdata.birthOfPlace);

            Glide.with(binding.image).load(profileHomeFragdata.profileImage).into(binding.image);

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

        }
    }

}

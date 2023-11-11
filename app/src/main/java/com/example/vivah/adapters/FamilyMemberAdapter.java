package com.example.vivah.adapters;

import android.view.LayoutInflater;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vivah.databinding.ItemContainerFamilyMemberBinding;
import com.example.vivah.models.FamilyMember;

import java.util.List;

public class FamilyMemberAdapter extends RecyclerView.Adapter<FamilyMemberAdapter.FamilyMemberViewHolder> {
    private final List<FamilyMember> familyMembers;

    public FamilyMemberAdapter(List<FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
    }

    @NonNull
    @Override
    public FamilyMemberAdapter.FamilyMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerFamilyMemberBinding itemContainerFamilyMemberBinding = ItemContainerFamilyMemberBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new FamilyMemberViewHolder(itemContainerFamilyMemberBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyMemberAdapter.FamilyMemberViewHolder holder, int position) {
        holder.setUserData(familyMembers.get(position));
    }

    @Override
    public int getItemCount() {
        return familyMembers.size();
    }

    public class FamilyMemberViewHolder extends RecyclerView.ViewHolder {
        ItemContainerFamilyMemberBinding binding;

        public FamilyMemberViewHolder(ItemContainerFamilyMemberBinding itemContainerFamilyMemberBinding) {
            super(itemContainerFamilyMemberBinding.getRoot());
            binding = itemContainerFamilyMemberBinding;
        }
        void setUserData(FamilyMember familyMember){
            binding.textName.setText(familyMember.name);
            binding.textRelation.setText(familyMember.relation);
            binding.textOccupation.setText(familyMember.occupation);

        }
    }
}

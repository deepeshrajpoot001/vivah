package com.example.vivah.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vivah.activities.ChatActivity;
import com.example.vivah.databinding.ItemContainerInboxFragmentBinding;
import com.example.vivah.models.Inbox;
import com.example.vivah.models.User;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder>{
    private final List<Inbox> inboxes;
    Context context;

    public InboxAdapter(List<Inbox> inboxes,Context context) {
        this.inboxes = inboxes;
        this.context = context;
    }


    @NonNull
    @Override
    public InboxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerInboxFragmentBinding itemContainerInboxFragmentBinding = ItemContainerInboxFragmentBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
        );
        return  new ViewHolder(itemContainerInboxFragmentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxAdapter.ViewHolder holder, int position) {
        holder.setInboxFragData(inboxes.get(position));

    }

    @Override
    public int getItemCount() {
       return inboxes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemContainerInboxFragmentBinding binding;
        PreferenceManager preferenceManager;
        FirebaseFirestore database;


        public ViewHolder(ItemContainerInboxFragmentBinding itemContainerInboxFragmentBinding) {
            super(itemContainerInboxFragmentBinding.getRoot());
            binding = itemContainerInboxFragmentBinding;
            preferenceManager = new PreferenceManager(context);
            database = FirebaseFirestore.getInstance();
        }
        void setInboxFragData(Inbox inbox){
            binding.name.setText(inbox.inboxName);
            binding.age.setText(inbox.inboxAge);
            binding.placeOfBirth.setText(inbox.inboxPlaceOfBirth);
            binding.profession.setText(inbox.inboxProfession);
            Glide.with(binding.image).load(inbox.inboxImage).into(binding.image);

            if(inbox.status.equals("received")){
                binding.acceptButton.setVisibility(View.VISIBLE);
                binding.deleteButton.setVisibility(View.VISIBLE);

                binding.acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(context,inbox.docId,Toast.LENGTH_SHORT).show();

                            DocumentReference documentReference =
                                    database.collection(Constants.KEY_COLLECTION_INBOX).document(inbox.docId);
                            documentReference.update(
                                    Constants.KEY_STATUS,"done"
                            );

                        HashMap<String,Object> conversion = new HashMap<>();
                        conversion.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID));
                        conversion.put(Constants.KEY_SENDER_NAME,preferenceManager.getString(Constants.KEY_FIRST_NAME)+" "+preferenceManager.getString(Constants.KEY_LAST_NAME));
                        conversion.put(Constants.KEY_SENDER_IMAGE,preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
                        conversion.put(Constants.KEY_RECEIVER_ID,inbox.inboxId);
                        conversion.put(Constants.KEY_RECEIVER_NAME,inbox.inboxName);
                        conversion.put(Constants.KEY_RECEIVER_IMAGE,inbox.inboxImage);
                        conversion.put(Constants.KEY_LAST_MESSAGE,"send Hi to start conversion");
                        conversion.put(Constants.KEY_TIMESTAMP,new Date());

                        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                                    .add(conversion)
                                    .addOnSuccessListener(documentReference1 -> {
                                        Toast.makeText(context,"now your can chat",Toast.LENGTH_SHORT).show();
                                    });

                    }
                });

                binding.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.acceptButton.setVisibility(View.GONE);
                        binding.deleteButton.setClickable(false);
                        binding.deleteButton.setText("deleted");

                        DocumentReference documentReference =
                                database.collection(Constants.KEY_COLLECTION_INBOX).document(inbox.docId);
                        documentReference.update(
                                Constants.KEY_STATUS,"deleted"
                        );

                    }
                });


            }else if( inbox.status.equals("accepted")){
                binding.acceptButton.setVisibility(View.VISIBLE);
                binding.acceptButton.setText("Message");
                binding.acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            User user = new User();
                            user.id = inbox.inboxId;
                            user.name = inbox.inboxName;
                            user.image = inbox.inboxImage;
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra(Constants.KEY_USER,user);
                            context.startActivity(intent);


                    }
                });



            }else if(inbox.status.equals("requests")){
                binding.acceptButton.setVisibility(View.VISIBLE);
                binding.acceptButton.setText("pending");
                binding.acceptButton.setClickable(false);
                binding.deleteButton.setVisibility(View.VISIBLE);
                binding.deleteButton.setText("Cancel");
                binding.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                                database.collection(Constants.KEY_COLLECTION_INBOX).document(inbox.docId)
                                        .delete();

                    }
                });


            }else if(inbox.status.equals("deleted")){
                binding.acceptButton.setVisibility(View.VISIBLE);
                binding.acceptButton.setText("Connect");
                binding.acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DocumentReference documentReference =
                                database.collection(Constants.KEY_COLLECTION_INBOX).document(inbox.docId);
                        documentReference.update(
                                Constants.KEY_STATUS,"done"
                        );

                        HashMap<String,Object> conversion = new HashMap<>();
                        conversion.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID));
                        conversion.put(Constants.KEY_SENDER_NAME,preferenceManager.getString(Constants.KEY_NAME));
                        conversion.put(Constants.KEY_SENDER_IMAGE,preferenceManager.getString(Constants.KEY_IMAGE));
                        conversion.put(Constants.KEY_RECEIVER_ID,inbox.inboxId);
                        conversion.put(Constants.KEY_RECEIVER_NAME,inbox.inboxName);
                        conversion.put(Constants.KEY_RECEIVER_IMAGE,inbox.inboxImage);
                        conversion.put(Constants.KEY_LAST_MESSAGE,"send Hi to start conversion");
                        conversion.put(Constants.KEY_TIMESTAMP,new Date());

                        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                                .add(conversion)
                                .addOnSuccessListener(documentReference1 -> {
                                    Toast.makeText(context,"now your can chat",Toast.LENGTH_SHORT).show();
                                });

                    }
                });




            }

        }
    }
}

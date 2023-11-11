package com.example.vivah.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vivah.activities.ViewImage;
import com.example.vivah.databinding.ItemContainerReceivedMessageBinding;
import com.example.vivah.databinding.ItemContainerSentMessageBinding;
import com.example.vivah.models.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {

    private final List<ChatMessage> chatMessage;
    private String receiverProfileImage;
     private Context context;
    private final String senderId;

    public static final int VIEW_TYPE_SENT=1;
    public static final int VIEW_TYPE_RECEIVED=2;

    public void setReceiverProfileImage(String image){
        receiverProfileImage = image;
    }

    public ChatAdapter(List<ChatMessage> chatMessage, String receiverProfileImage, String senderId,Context context) {
        this.chatMessage = chatMessage;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_SENT){
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }else{
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).setData(chatMessage.get(position),context);
        }else{
            ((ReceivedMessageViewHolder) holder).setData(chatMessage.get(position),receiverProfileImage,context);
        }

    }

    @Override
    public int getItemCount() {
        return chatMessage.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessage.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT;
        }else{
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {


        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(ChatMessage chatMessage,Context context) {
            if(chatMessage.messageType.equals("image")){
                Glide.with(binding.imageMessage).load(chatMessage.image).into(binding.imageMessage);
                binding.imageMessage.setVisibility(View.VISIBLE);
                binding.imageMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ViewImage.class);
                        intent.putExtra("image",chatMessage.image);
                        context.startActivity(intent);


                    }
                });
                binding.textMessage.setVisibility(View.GONE);
            }else{
                binding.textMessage.setText(chatMessage.message);
                binding.textMessage.setVisibility(View.VISIBLE);
                binding.imageMessage.setVisibility(View.GONE);
            }
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }



    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerReceivedMessageBinding binding;

            ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding){
                super(itemContainerReceivedMessageBinding.getRoot());
                binding = itemContainerReceivedMessageBinding;
            }

            void setData(ChatMessage chatMessage, String receivedProfileImage,Context context) {
                if (chatMessage.messageType.equals("image")) {
                    Glide.with(binding.imageMessage).load(chatMessage.image).into(binding.imageMessage);
                    binding.imageMessage.setVisibility(View.VISIBLE);
                    binding.textMessage.setVisibility(View.GONE);
                    binding.imageMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ViewImage.class);
                            intent.putExtra("image",chatMessage.image);
                            context.startActivity(intent);


                        }
                    });
                } else {
                    binding.textMessage.setText(chatMessage.message);
                    binding.textMessage.setVisibility(View.VISIBLE);
                    binding.imageMessage.setVisibility(View.GONE);
                }
                binding.textDateTime.setText(chatMessage.dateTime);
                if (receivedProfileImage != null) {
                    Glide.with(binding.imageProfile).load(receivedProfileImage).into(binding.imageProfile);
                }
            }
    }






















}

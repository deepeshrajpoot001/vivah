package com.example.vivah.fragment;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.vivah.OnSwipeTouchListener;
import com.example.vivah.R;
import com.example.vivah.adapters.InboxAdapter;
import com.example.vivah.databinding.FragmentInboxBinding;
import com.example.vivah.models.Inbox;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class InboxFragment extends Fragment {

    FragmentInboxBinding binding;
    private FirebaseFirestore database;
    private PreferenceManager preferenceManager;
    private List<Inbox> receivedList;
    private List<Inbox> requestsList;
    private List<Inbox> acceptedList;
    private List<Inbox> deletedList;
    private InboxAdapter receivedListAdapter;
    private InboxAdapter requestsListAdapter;
    private InboxAdapter acceptedListAdapter;
    private InboxAdapter deleteListAdapter;

    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInboxBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        init();
        preferenceManager = new PreferenceManager(container.getContext());
        listenerInbox();
        setListener();


        return  view;
    }


    private void init(){
        receivedList = new ArrayList<>();
        acceptedList = new ArrayList<>();
        requestsList = new ArrayList<>();
        deletedList = new ArrayList<>();

        receivedListAdapter = new InboxAdapter(receivedList,this.getContext());
        acceptedListAdapter = new InboxAdapter(acceptedList,this.getContext());
        requestsListAdapter = new InboxAdapter(requestsList,this.getContext());
        deleteListAdapter = new InboxAdapter(deletedList,this.getContext());

        database = FirebaseFirestore.getInstance();

        if(receivedList.size()>0) {
            binding.textErrorMessage.setVisibility(View.GONE);
            binding.inboxRecyclerView.setAdapter(receivedListAdapter);
        }else{
            binding.inboxRecyclerView.setAdapter(null);
            showErrorMessage();
        }
    }
    private void setListener(){

        binding.receivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.receivedButton.setStrokeWidth(2);
                binding.acceptedButton.setStrokeWidth(0);
                binding.requestButton.setStrokeWidth(0);
                binding.deleteButton.setStrokeWidth(0);
                binding.receivedButton.setBackgroundColor(requireContext().getColor(R.color.inboxSelectIconBackground));
                binding.acceptedButton.setBackgroundColor(requireContext().getColor(R.color.inboxUnselectedIconBackground));
                binding.requestButton.setBackgroundColor(requireContext().getColor(R.color.inboxUnselectedIconBackground));
                binding.deleteButton.setBackgroundColor(requireContext().getColor(R.color.inboxUnselectedIconBackground));

                if(receivedList.size()>0) {
                    binding.textErrorMessage.setVisibility(View.GONE);
                    binding.inboxRecyclerView.setAdapter(receivedListAdapter);
                }else{
                    binding.inboxRecyclerView.setAdapter(null);
                    showErrorMessage();
                }
            }
        });

        binding.acceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.receivedButton.setStrokeWidth(0);
                binding.acceptedButton.setStrokeWidth(2);
                binding.requestButton.setStrokeWidth(0);
                binding.deleteButton.setStrokeWidth(0);
                binding.receivedButton.setBackgroundColor(requireContext().getColor(R.color.inboxUnselectedIconBackground));
                binding.acceptedButton.setBackgroundColor(requireContext().getColor(R.color.inboxSelectIconBackground));
                binding.requestButton.setBackgroundColor(requireContext().getColor(R.color.inboxUnselectedIconBackground));
                binding.deleteButton.setBackgroundColor(requireContext().getColor(R.color.inboxUnselectedIconBackground));

                if(acceptedList.size()>0) {
                    binding.textErrorMessage.setVisibility(View.GONE);
                    binding.inboxRecyclerView.setAdapter(acceptedListAdapter);
                }else{
                    binding.inboxRecyclerView.setAdapter(null);
                    showErrorMessage();
                }
            }
        });


        binding.requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.receivedButton.setStrokeWidth(0);
                binding.acceptedButton.setStrokeWidth(0);
                binding.requestButton.setStrokeWidth(2);
                binding.deleteButton.setStrokeWidth(0);
                binding.receivedButton.setBackgroundColor(requireContext().getColor(R.color.inboxUnselectedIconBackground));
                binding.acceptedButton.setBackgroundColor(requireContext().getColor(R.color.inboxUnselectedIconBackground));
                binding.requestButton.setBackgroundColor(requireContext().getColor(R.color.inboxSelectIconBackground));
                binding.deleteButton.setBackgroundColor(requireContext().getColor(R.color.inboxUnselectedIconBackground));


                if(requestsList.size()>0) {
                    binding.textErrorMessage.setVisibility(View.GONE);
                    binding.inboxRecyclerView.setAdapter(requestsListAdapter);
                }else{
                    binding.inboxRecyclerView.setAdapter(null);
                    showErrorMessage();
                }
            }
        });


        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.receivedButton.setStrokeWidth(0);
                binding.acceptedButton.setStrokeWidth(0);
                binding.requestButton.setStrokeWidth(0);
                binding.deleteButton.setStrokeWidth(2);
                binding.receivedButton.setBackgroundColor(requireContext().getColor(R.color.inboxUnselectedIconBackground));
                binding.acceptedButton.setBackgroundColor(requireContext().getColor(R.color.inboxUnselectedIconBackground));
                binding.requestButton.setBackgroundColor(requireContext().getColor(R.color.inboxUnselectedIconBackground));
                binding.deleteButton.setBackgroundColor(requireContext().getColor(R.color.inboxSelectIconBackground));

                if(deletedList.size()>0) {
                    binding.inboxRecyclerView.setAdapter(deleteListAdapter);
                    binding.textErrorMessage.setVisibility(View.GONE);
                }else{
                    binding.inboxRecyclerView.setAdapter(null);
                    showErrorMessage();
                }
            }
        });
    }
    private void showErrorMessage(){
        binding.textErrorMessage.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "no request found", Toast.LENGTH_SHORT).show();

    }

    private void listenerInbox(){
        database.collection(Constants.KEY_COLLECTION_INBOX)
                .whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_INBOX)
                .whereEqualTo(Constants.KEY_RECEIVER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }



    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error!=null){
            return;
        }
        if(value != null){
            for(DocumentChange documentChange: value.getDocumentChanges()){
                if(documentChange.getType()==DocumentChange.Type.ADDED){

                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    Inbox inbox = new Inbox();
                    inbox.senderId = senderId;
                    inbox.receiverId = receiverId;
                    if(preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)){
                            inbox.docId = documentChange.getDocument().getId();
                            inbox.inboxId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                            inbox.inboxName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                            inbox.inboxImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                            inbox.inboxAge = documentChange.getDocument().getString(Constants.KEY_RECEIVER_AGE);
                            inbox.inboxProfession = documentChange.getDocument().getString(Constants.KEY_RECEIVER_PROFESSION);
                            inbox.inboxPlaceOfBirth = documentChange.getDocument().getString(Constants.KEY_RECEIVER_PLACE_OF_BIRTH);
                            inbox.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                        if(documentChange.getDocument().getString(Constants.KEY_STATUS).equals("send")) {
                            inbox.status = "requests";
                            requestsList.add(inbox);
                            Collections.sort(requestsList, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
                        }else if(documentChange.getDocument().getString(Constants.KEY_STATUS).equals("done")){
                            inbox.status = "accepted";
                            acceptedList.add(inbox);
                            Collections.sort(acceptedList, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));

                        }

                    }else{

                            inbox.docId = documentChange.getDocument().getId();
                            inbox.inboxId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                            inbox.inboxName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                            inbox.inboxImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                            inbox.inboxAge = documentChange.getDocument().getString(Constants.KEY_SENDER_AGE);
                            inbox.inboxProfession = documentChange.getDocument().getString(Constants.KEY_SENDER_PROFESSION);
                            inbox.inboxPlaceOfBirth = documentChange.getDocument().getString(Constants.KEY_SENDER_PLACE_OF_BIRTH);
                            inbox.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                        if(documentChange.getDocument().getString(Constants.KEY_STATUS).equals("send")) {
                            inbox.status = "received";
                            receivedList.add(inbox);
                            Collections.sort(receivedList, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
                        }else if(documentChange.getDocument().getString(Constants.KEY_STATUS).equals("deleted")){
                            inbox.status = "deleted";
                            deletedList.add(inbox);
                            Collections.sort(deletedList, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
                        }else if(documentChange.getDocument().getString(Constants.KEY_STATUS).equals("done")){
                            inbox.status = "accepted";
                            acceptedList.add(inbox);
                            Collections.sort(acceptedList, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
                        }
                    }
                }else if(documentChange.getType()== DocumentChange.Type.MODIFIED){

                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    Inbox inbox = new Inbox();
                    inbox.senderId = senderId;
                    inbox.receiverId = receiverId;

                    for (int i = 0; i < receivedList.size(); i++) {

                        if (receivedList.get(i).senderId.equals(senderId) && receivedList.get(i).receiverId.equals(receiverId)) {
                            receivedList.remove(i);
                            break;
                        }
                    }
                    for (int i = 0; i < deletedList.size(); i++) {
                        if (deletedList.get(i).senderId.equals(senderId) && deletedList.get(i).receiverId.equals(receiverId)) {
                            deletedList.remove(i);
                            break;
                        }
                    }
                    for (int i = 0; i < requestsList.size(); i++) {
                        if (requestsList.get(i).senderId.equals(senderId) && requestsList.get(i).receiverId.equals(receiverId)) {
                            requestsList.remove(i);
                            break;
                        }
                    }


                    if(preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)){
                        inbox.docId = documentChange.getDocument().getId();
                        inbox.inboxId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        inbox.inboxName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        inbox.inboxImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                        inbox.inboxAge = documentChange.getDocument().getString(Constants.KEY_RECEIVER_AGE);
                        inbox.inboxProfession = documentChange.getDocument().getString(Constants.KEY_RECEIVER_PROFESSION);
                        inbox.inboxPlaceOfBirth = documentChange.getDocument().getString(Constants.KEY_RECEIVER_PLACE_OF_BIRTH);
                        inbox.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                        if(documentChange.getDocument().getString(Constants.KEY_STATUS).equals("send")) {
                            inbox.status = "requests";
                            requestsList.add(inbox);
                            Collections.sort(requestsList, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
                        }else if(documentChange.getDocument().getString(Constants.KEY_STATUS).equals("done")){
                            inbox.status = "accepted";
                            acceptedList.add(inbox);
                            Collections.sort(acceptedList, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
                        }else if(documentChange.getDocument().getString(Constants.KEY_STATUS).equals("deleted")){
                            inbox.status = "requests";
                            requestsList.add(inbox);
                            Collections.sort(requestsList, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));

                        }

                    }else{
                        inbox.docId = documentChange.getDocument().getId();
                        inbox.inboxId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        inbox.inboxName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        inbox.inboxImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                        inbox.inboxAge = documentChange.getDocument().getString(Constants.KEY_SENDER_AGE);
                        inbox.inboxProfession = documentChange.getDocument().getString(Constants.KEY_SENDER_PROFESSION);
                        inbox.inboxPlaceOfBirth = documentChange.getDocument().getString(Constants.KEY_SENDER_PLACE_OF_BIRTH);
                        inbox.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                        if(documentChange.getDocument().getString(Constants.KEY_STATUS).equals("send")) {
                            inbox.status = "received";
                            receivedList.add(inbox);
                            Collections.sort(receivedList, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
                        }else if(documentChange.getDocument().getString(Constants.KEY_STATUS).equals("deleted")){
                            inbox.status = "deleted";
                            deletedList.add(inbox);
                            Collections.sort(deletedList, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
                        }else if(documentChange.getDocument().getString(Constants.KEY_STATUS).equals("done")){
                            inbox.status = "accepted";
                            acceptedList.add(inbox);
                            Collections.sort(acceptedList, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
                        }
                    }

                    }else if(documentChange.getType() == DocumentChange.Type.REMOVED){

                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    Inbox inbox = new Inbox();
                    inbox.senderId = senderId;
                    inbox.receiverId = receiverId;

                    for (int i = 0; i < receivedList.size(); i++) {

                        if (receivedList.get(i).senderId.equals(senderId) && receivedList.get(i).receiverId.equals(receiverId)) {
                            receivedList.remove(i);
                            break;
                        }
                    }
                    for (int i = 0; i < deletedList.size(); i++) {
                        if (deletedList.get(i).senderId.equals(senderId) && deletedList.get(i).receiverId.equals(receiverId)) {
                            deletedList.remove(i);
                            break;
                        }
                    }
                    for (int i = 0; i < requestsList.size(); i++) {
                        if (requestsList.get(i).senderId.equals(senderId) && requestsList.get(i).receiverId.equals(receiverId)) {
                            requestsList.remove(i);
                            break;
                        }
                    }




                }

            receivedListAdapter.notifyDataSetChanged();
            acceptedListAdapter.notifyDataSetChanged();
            requestsListAdapter.notifyDataSetChanged();
            deleteListAdapter.notifyDataSetChanged();
            binding.inboxRecyclerView.smoothScrollToPosition(0);
            binding.inboxRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);


            }
        }
    };
}
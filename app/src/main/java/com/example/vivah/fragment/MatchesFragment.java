package com.example.vivah.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vivah.adapters.profileMatchesFragmentAdapter;
import com.example.vivah.databinding.FragmentMatchesBinding;


import com.example.vivah.models.Inbox;
import com.example.vivah.models.ProfileMatchesFrag;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;


import org.checkerframework.checker.units.qual.A;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MatchesFragment extends Fragment {


    public MatchesFragment() {
        // Required empty public constructor
    }
    private FragmentMatchesBinding binding;


    private PreferenceManager preferenceManager;
    private FirebaseStorage firebaseStorage;
    private List<ProfileMatchesFrag> profileMatchesFrags;
    private profileMatchesFragmentAdapter profileMatchesFragmentAdapter;
    private ArrayList<String> connected;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMatchesBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        preferenceManager = new PreferenceManager(container.getContext());
        firebaseStorage = FirebaseStorage.getInstance();
        profileMatchesFrags = new ArrayList<>();
        connected = new ArrayList<>();
        showErrorMessage();

        setUpViewPager();



        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(Constants.KEY_COLLECTION_INBOX)
                .whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_INBOX)
                .whereEqualTo(Constants.KEY_RECEIVER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);


        return view;


    }

    @Override
    public void onStart() {
        super.onStart();
        getUser();
    }

    private void setUpViewPager(){
        binding.profileViewPager.setClipToPadding(false);
        binding.profileViewPager.setClipChildren(false);
        binding.profileViewPager.setOffscreenPageLimit(3);
        binding.profileViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1- Math.abs(position);
            page.setScaleY(0.85f+r*0.15f);
        });
        binding.profileViewPager.setPageTransformer(compositePageTransformer);

        profileMatchesFragmentAdapter = new profileMatchesFragmentAdapter(profileMatchesFrags,getActivity());
        binding.profileViewPager.setAdapter(profileMatchesFragmentAdapter);

    }



    private void getUser(){
        String find;
        if(preferenceManager.getString(Constants.KEY_GENDER).equals("Male")){
            find = "Female";
        }else{
           find = "Male";
        }

        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USER)
                .whereEqualTo(Constants.KEY_GENDER,find)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null){

                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            boolean Flag=false;
                            for(int i=0;i<connected.size();i++){
                                if(connected.get(i).equals(queryDocumentSnapshot.getId())){
                                    Flag = true;
                                    break;
                                }
                            }
                            if(Flag){
                                continue;
                            }

                            ProfileMatchesFrag profileMatchesFrag = new ProfileMatchesFrag();
                            profileMatchesFrag.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            profileMatchesFrag.age=  String.valueOf(Year.now().getValue() - Integer.parseInt(String.valueOf(queryDocumentSnapshot.get(Constants.KEY_YEAR_OF_DOB))));
                            profileMatchesFrag.height = queryDocumentSnapshot.getString(Constants.KEY_HEIGHT);
                            profileMatchesFrag.profession = queryDocumentSnapshot.getString(Constants.KEY_JOB_DESCRIPTION);
                            profileMatchesFrag.language = queryDocumentSnapshot.getString(Constants.KEY_MOTHER_TONGUE);
                            profileMatchesFrag.subCaste = queryDocumentSnapshot.getString(Constants.KEY_SUB_CASTE);
                            profileMatchesFrag.placeOfBirth = queryDocumentSnapshot.getString(Constants.KEY_DISTRICT)+","+queryDocumentSnapshot.getString(Constants.KEY_STATE);
                            profileMatchesFrag.id= queryDocumentSnapshot.getId();
                            String profileImageUri = queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE);
                            profileMatchesFrag.profileImage = Uri.parse(profileImageUri);
                            profileMatchesFrag.aboutMe = queryDocumentSnapshot.getString(Constants.KEY_ABOUT_YOURSELF);

                            profileMatchesFrags.add(profileMatchesFrag);

                            profileMatchesFrags.size();
                            profileMatchesFragmentAdapter.notifyDataSetChanged();
                            binding.profileViewPager.setVisibility(View.VISIBLE);
                            binding.textErrorMessage.setVisibility(View.INVISIBLE);


                        }



                    }else{
                        showErrorMessage();
                    }
                });
    }

    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s","No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading (Boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {

                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);


                    if (preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)) {
                        connected.add(receiverId);
                    } else {
                        connected.add(senderId);
                    }


                }
            }
        }

    };


}
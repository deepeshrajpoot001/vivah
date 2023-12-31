package com.example.vivah.fragment;


import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.example.vivah.R;
import com.example.vivah.adapters.profileHomeFragAdapter;
import com.example.vivah.databinding.DialogeLayoutFilterBinding;
import com.example.vivah.databinding.FragmentHomeBinding;
import com.example.vivah.models.ProfileHomeFrag;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private FragmentHomeBinding binding;
    private PreferenceManager preferenceManager;
    private List<ProfileHomeFrag> profileHomeFrags;
    private profileHomeFragAdapter profileHomeFragAdapter;

    private ArrayList<String> connected;

    String searchText = null;
    String filterState, filterDistrict, filterAgeFrom, filterAgeTo, filterMotherTongue,filterIncome;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        preferenceManager = new PreferenceManager(container.getContext());
        profileHomeFrags = new ArrayList<>();

        connected = new ArrayList<>();
        showErrorMessage();


        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(Constants.KEY_COLLECTION_INBOX)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_INBOX)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);

        filterState = "All";
        filterDistrict = "Select Your District";
        filterMotherTongue = "All";
        filterAgeFrom = "20";
        filterAgeTo = "39";
        filterIncome = "All";


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.refreshLayoutHome.setOnRefreshListener(() -> {
            if (searchText == null) {
                getUser("deepesh", false);
            } else {
                getUser(searchText, true);
            }
        });
        getUser("deepesh", false);
        setListener();

        profileHomeFragAdapter = new profileHomeFragAdapter(profileHomeFrags, getActivity());
        binding.profileShowRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.profileShowRecyclerView.setAdapter(profileHomeFragAdapter);


    }

    private void setListener() {

        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchText = s.toString();
                getUser(searchText, true);
            }
        });


        binding.filter.setOnClickListener(v -> {
            Dialog dialog = new Dialog(requireContext());
            DialogeLayoutFilterBinding dBinding = DialogeLayoutFilterBinding.inflate(getLayoutInflater());
            dialog.setContentView(dBinding.getRoot());
            String[] state = getResources().getStringArray(R.array.array_indian_states);
            String[] motherTongue = getResources().getStringArray(R.array.languages);

            ArrayList<String> tempState = new ArrayList<>(Arrays.asList(state));
            tempState.add(0, "All");

            ArrayList<String> tempMotherTongue = new ArrayList<>(Arrays.asList(motherTongue));
            tempMotherTongue.add(0, "All");

            Integer[] age = {20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39};
            ArrayList<Integer> ageFrom = new ArrayList<>(Arrays.asList(age));

            ArrayList<String> tempIncome = new ArrayList<>(Arrays.asList(Constants.yearlyIncome));
            tempIncome.add(0,"All");


            List<Integer> ageTo;
            ageTo = ageFrom.subList(2, 19);
            ArrayAdapter<Integer> adapterAgeTo = new ArrayAdapter<>(requireActivity().getApplicationContext(), android.R.layout.simple_spinner_item, ageTo);
            adapterAgeTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dBinding.ageTo.setAdapter(adapterAgeTo);
            dBinding.ageTo.setSelection(5);


            ArrayAdapter<String> adapterState = new ArrayAdapter<>(requireActivity().getApplicationContext(), android.R.layout.simple_spinner_item, tempState);
            ArrayAdapter<String> adapterMotherTongue = new ArrayAdapter<>(requireActivity().getApplicationContext(), android.R.layout.simple_spinner_item, tempMotherTongue);
            ArrayAdapter<Integer> adapterAgeFrom = new ArrayAdapter<>(requireActivity().getApplicationContext(), android.R.layout.simple_spinner_item, ageFrom);
            ArrayAdapter<String> adapterIncomeYearly = new ArrayAdapter<>(requireActivity().getApplicationContext(),android.R.layout.simple_spinner_item,tempIncome);


            adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterMotherTongue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterAgeFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterIncomeYearly.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            dBinding.state.setAdapter(adapterState);
            dBinding.motherTongue.setAdapter(adapterMotherTongue);
            dBinding.ageFrom.setAdapter(adapterAgeFrom);
            dBinding.income.setAdapter(adapterIncomeYearly);


            dBinding.state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    filterState = parent.getItemAtPosition(position).toString();

                    if (filterState.equals("All")) {
                        dBinding.district.setVisibility(View.GONE);
                        dBinding.districtText.setVisibility(View.GONE);
                        filterDistrict = "Select Your District";
                        filterState = "All";
                    } else {
                        Toast.makeText(getContext(), filterState, Toast.LENGTH_SHORT).show();
                        String[] district = getResources().getStringArray(findResourcesOfState(filterState));
                        ArrayAdapter<String> adapterDistrict = new ArrayAdapter<>(requireActivity().getApplicationContext(), android.R.layout.simple_spinner_item, district);
                        adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dBinding.district.setAdapter(adapterDistrict);
                        dBinding.district.setVisibility(View.VISIBLE);
                        dBinding.districtText.setVisibility(View.VISIBLE);

                        dBinding.district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (parent.getItemAtPosition(position).toString().equals("Select Your District")) {
                                    filterDistrict = "Select Your District";
                                } else {
                                    filterDistrict = parent.getItemAtPosition(position).toString();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            dBinding.ageFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    filterAgeFrom = parent.getItemAtPosition(position).toString();

                    List<Integer> ageTo;
                    int index = ageFrom.indexOf(Integer.parseInt(filterAgeFrom));
                    ageTo = ageFrom.subList(index + 1, 20);
                    ArrayAdapter<Integer> adapterAgeTo = new ArrayAdapter<>(requireActivity().getApplicationContext(), android.R.layout.simple_spinner_item, ageTo);
                    adapterAgeTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dBinding.ageTo.setAdapter(adapterAgeTo);

                    dBinding.ageTo.setSelection(ageTo.size()-1);

                    dBinding.ageTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            filterAgeTo = parent.getItemAtPosition(position).toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            dBinding.motherTongue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    filterMotherTongue = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            dBinding.income.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    filterIncome = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            dBinding.cancelButton.setOnClickListener(v1 -> dialog.dismiss());
            dBinding.applyButton.setOnClickListener(v12 -> {

                if (searchText == null) {
                    getUser("", true);
                } else {
                    getUser(searchText, true);
                }

                dialog.dismiss();

            });


            dialog.show();

        });


    }

    public int findResourcesOfState(String state) {
        state = state.toLowerCase();
        state = state.replace(' ', '_');
        state = "array_" + state + "_districts";

        return requireActivity().getApplicationContext().getResources().getIdentifier(state, "array", requireActivity().getPackageName());
    }

    private void getUser(String str, boolean flag) {
        String find;
        if (preferenceManager.getString(Constants.KEY_GENDER).equals("Male")) {
            find = "Female";
        } else {
            find = "Male";
        }



        binding.refreshLayoutHome.setRefreshing(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USER)
                .whereEqualTo(Constants.KEY_GENDER, find)
                .get()
                .addOnCompleteListener(task -> {


                    binding.refreshLayoutHome.setRefreshing(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {


                        profileHomeFrags.clear();
                        showErrorMessage();
                        profileHomeFragAdapter.notifyDataSetChanged();


                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }


                            boolean tempFlag = false;
                            for (int i = 0; i < connected.size(); i++) {
                                if (connected.get(i).equals(queryDocumentSnapshot.getId())) {
                                    tempFlag = true;
                                    break;
                                }
                            }
                            if (tempFlag) {
                                continue;
                            }





                            ProfileHomeFrag profileHomeFrag = new ProfileHomeFrag();
                            String dataName = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            profileHomeFrag.name = dataName;
                            int dataAge = Year.now().getValue() - Integer.parseInt(String.valueOf(queryDocumentSnapshot.get(Constants.KEY_YEAR_OF_DOB)));
                            profileHomeFrag.age = String.valueOf(dataAge);
                            profileHomeFrag.profession = queryDocumentSnapshot.getString(Constants.KEY_JOB_DESCRIPTION);
                            String dataDistrict = queryDocumentSnapshot.getString(Constants.KEY_DISTRICT);
                            String dataState = queryDocumentSnapshot.getString(Constants.KEY_STATE);
                            String motherTongueData = queryDocumentSnapshot.getString(Constants.KEY_MOTHER_TONGUE);
                            profileHomeFrag.birthOfPlace = dataDistrict + "," + dataState;
                            profileHomeFrag.id = queryDocumentSnapshot.getId();
                            profileHomeFrag.height = queryDocumentSnapshot.getString(Constants.KEY_HEIGHT);
                            String profileImageUri = queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE);
                            profileHomeFrag.profileImage = Uri.parse(profileImageUri);





                            if (flag) {
                                if (!filterState.equals("All")) {
                                    if (!filterState.equals(dataState)) {
                                        continue;
                                    }
                                }



                                if (!filterDistrict.equals("Select Your District")) {
                                    if (!filterDistrict.equals(dataDistrict)) {
                                        continue;
                                    }
                                }

                                if (!filterMotherTongue.equals("All")) {
                                    if (!filterMotherTongue.equals(motherTongueData)) {
                                        continue;
                                    }
                                }
                                if(!filterIncome.equals("All")){
                                    if (!filterIncome.equals(motherTongueData)) {
                                        continue;
                                    }
                                }



                                int minAge = Integer.parseInt(filterAgeFrom), maxAge = Integer.parseInt(filterAgeTo);

                                if ((dataAge <= minAge || dataAge >= maxAge)) {
                                    continue;
                                }


                                assert dataName != null;
                                dataName = dataName.toLowerCase();
                                String temp = str.toLowerCase();
                                if (dataName.contains(temp)) {
                                    profileHomeFrags.add(profileHomeFrag);
                                }
                            } else {
                                profileHomeFrags.add(profileHomeFrag);
                            }




                            profileHomeFragAdapter.notifyDataSetChanged();
                            if (profileHomeFrags.size() > 0) {
                                binding.profileShowRecyclerView.setVisibility(View.VISIBLE);
                                binding.textErrorMessage.setVisibility(View.INVISIBLE);
                            } else {
                                showErrorMessage();
                            }
                        }


                    } else {
                        showErrorMessage();
                    }
                });
    }



    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s","No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
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



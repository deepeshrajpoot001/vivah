package com.example.vivah.activities;

import static com.example.vivah.activities.CreateProfile1.profile;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.vivah.R;
import com.example.vivah.databinding.ActivityCreateProfile3Binding;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class CreateProfile3 extends AppCompatActivity {

    ActivityCreateProfile3Binding binding;
    private String[] district;
    private String[] state;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfile3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        profile.fullAddress="";
        profile.state="";
        profile.district="";

        preferenceManager = new PreferenceManager(getApplicationContext());




        binding.backButton.setOnClickListener(v-> onBackPressed());


         state = getResources().getStringArray(R.array.array_indian_states);

         ArrayAdapter<String> adapterState = new ArrayAdapter<>(getApplicationContext(),R.layout.drop_down_item,state);
         binding.state.setAdapter(adapterState);
         binding.state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 profile.state = binding.state.getText().toString();
                 updateDistrict(getResources().getStringArray(findResourcesOfState(profile.state)));
                 binding.districtLayout.setVisibility(View.VISIBLE);
             }
         });

        binding.continueButton.setOnClickListener(v -> {

            if(isValidDetail()){
                if(!binding.fullAddressEditText.getText().toString().trim().isEmpty()){
                    profile.fullAddress = binding.fullAddressEditText.getText().toString();
                }else{
                    profile.fullAddress = "Empty";
                }
                startActivity(new Intent(getApplicationContext(),CreateProfile4.class));
            }
            else{
                Toast.makeText(getApplicationContext(), "fill valid detail", Toast.LENGTH_SHORT).show();
            }


        });



    }

    public void updateDistrict(String[] district){
         this.district = district;
        ArrayAdapter<String> adapterDistrict = new ArrayAdapter<>(getApplicationContext(),R.layout.drop_down_item,district);
        binding.district.setText("");
        binding.district.setAdapter(adapterDistrict);

        binding.district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                profile.district = binding.district.getText().toString();
            }
        });




    }

    private void showMessage(String message,Integer Id,Integer layoutId){
        AutoCompleteTextView editText = findViewById(Id);
        editText.setError(message);
        editText.requestFocus();
        TextInputLayout layout = findViewById(layoutId);
        layout.setBoxStrokeColor(getColor(R.color.accent));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setError(null);
                layout.setBoxStrokeColor(getColor(com.google.android.material.R.color.design_default_color_primary));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean isValidDetail(){
        if(binding.state.getText().toString().trim().isEmpty()){
            showMessage("enter state",binding.state.getId(),binding.stateLayout.getId());
            return false;
        }else if(binding.district.getText().toString().trim().isEmpty()){
            showMessage("enter district",binding.district.getId(),binding.districtLayout.getId());
            return  false;
        }else if(binding.district.getText().toString().equals("Select Your District")){
            return false;
        }

        profile.state = binding.state.getText().toString();
        profile.district = binding.district.getText().toString();

        for(int i=0;i<state.length;i++){
                for(int j=0;j<district.length;j++){
                    if(profile.state.equals(state[i])&&profile.district.equals(district[j])){
                        return true;
                    }
                }
        }
        return false;

    }

    public int findResourcesOfState(String state){
        state = state.toLowerCase();
        state = state.replace(' ','_');
        state = "array_"+state+"_districts";

        return getApplicationContext().getResources().getIdentifier(state,"array",getPackageName());
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
                            profile.state = documentSnapshot.getString(Constants.KEY_STATE);
                            updateDistrict(state);
                            profile.district = documentSnapshot.getString(Constants.KEY_DISTRICT);
                            binding.state.setText(profile.state);
                            binding.district.setText(profile.district);


                            binding.district.setVisibility(View.VISIBLE);
                            binding.districtLayout.setVisibility(View.VISIBLE);

                            binding.continueButton.setClickable(true);

                            if(documentSnapshot.getString(Constants.KEY_FULL_ADDRESS)!=null) {
                                binding.fullAddressEditText.setText(documentSnapshot.getString(Constants.KEY_FULL_ADDRESS));
                                profile.fullAddress = documentSnapshot.getString(Constants.KEY_FULL_ADDRESS);
                            }
                        }
                    });
        }
    }
}
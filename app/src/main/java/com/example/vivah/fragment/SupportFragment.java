package com.example.vivah.fragment;



import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vivah.R;
import com.example.vivah.activities.AddImageActivity;
import com.example.vivah.activities.CreateProfile1;
import com.example.vivah.activities.CreateProfile8;
import com.example.vivah.activities.CreateProfile9;
import com.example.vivah.activities.MainActivity;
import com.example.vivah.activities.SignInActivity;
import com.example.vivah.activities.ViewProfile;
import com.example.vivah.databinding.FragmentHomeBinding;
import com.example.vivah.databinding.FragmentSupportBinding;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.exception.AppNotFoundException;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.PaymentApp;


public class SupportFragment extends Fragment {



    public SupportFragment() {
        // Required empty public constructor
    }
   private FragmentSupportBinding binding;
    private PreferenceManager preferenceManager;
    private EasyUpiPayment easyUpiPayment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSupportBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        preferenceManager = new PreferenceManager(container.getContext());

        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.textId.setText("Id: "+preferenceManager.getString(Constants.KEY_USER_ID));
        Glide.with(binding.circleImageView).load(preferenceManager.getString(Constants.KEY_PROFILE_IMAGE)).into(binding.circleImageView);

binding.circleImageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(container.getContext(), ViewProfile.class);
        intent.putExtra("requiredProfileId",preferenceManager.getString(Constants.KEY_USER_ID));
        intent.putExtra("requiredProfileName",preferenceManager.getString(Constants.KEY_NAME));
        intent.putExtra("type","requestSent");
        startActivity(intent);
    }
});


        binding.textEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(container.getContext(), CreateProfile1.class);
                startActivity(intent);
            }
        });
        binding.logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(container.getContext(),"Signing Out...",Toast.LENGTH_SHORT).show();
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                DocumentReference documentReference =
                        database.collection(Constants.KEY_COLLECTION_USER).document(
                                preferenceManager.getString(Constants.KEY_USER_ID)
                        );
                HashMap<String,Object> updates = new HashMap<>();
                updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
                documentReference.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                preferenceManager.clear();
                                startActivity(new Intent(container.getContext(), SignInActivity.class));
                                requireActivity().finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(container.getContext(),"unable to sign out",Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });


        binding.donateUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.paymentLinearLayout.setVisibility(View.VISIBLE);
                binding.PayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            paymentStart();
                        } catch (AppNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        

        return view;
    }
    
    public  void paymentStart() throws AppNotFoundException {

        String id = preferenceManager.getString(Constants.KEY_USER_ID)+UUID.randomUUID().toString().substring(0,5);
        String amount = binding.inputAmount.getText().toString();
        amount = amount + ".00";

        EasyUpiPayment.Builder  builder = new EasyUpiPayment.Builder(requireActivity())
                .with(PaymentApp.ALL)
                .setPayeeName("deepeshrajpoot001@okaxis")
                .setPayeeVpa("deepeshrajpoot001@okaxis")
                .setDescription("Pay team Vivah")
                .setAmount(amount)
                .setTransactionId(id)
                .setTransactionRefId(id);

        try {

            easyUpiPayment = builder.build();

            easyUpiPayment.startPayment();

        } catch (Exception exception) {
            exception.printStackTrace();
            showToast("Error: " + exception.getMessage());
        }

        EasyUpiPayment upi = builder.build();
        upi.startPayment();
    }
    private void showToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
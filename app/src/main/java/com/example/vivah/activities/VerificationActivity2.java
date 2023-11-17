package com.example.vivah.activities;

import static com.example.vivah.activities.CreateProfile1.profile;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


import com.example.vivah.databinding.ActivityVerification2Binding;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Year;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerificationActivity2 extends AppCompatActivity {
    ActivityVerification2Binding binding;
    private String verificationId;
    CountDownTimer countDownTimer;
    private String mobileNo;
    private String activityWork;
    private int i;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerification2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        i=60;
        countDownTimer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String str = i-- + " sec";
                binding.textResendOTP.setText(str);

            }

            @Override
            public void onFinish() {
                binding.textResendOTP.setText("Resend OTP");
                i=60;
            }
        };
        countDownTimer.start();

        mobileNo = String.format("+91-%s",getIntent().getStringExtra("mobile"));
        binding.textMobile.setText(mobileNo);
        preferenceManager = new PreferenceManager(this);
        setOTPInputs();
        activityWork = getIntent().getStringExtra("type");

        verificationId =  getIntent().getStringExtra("verificationId");
        binding.buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if(binding.inputCode1.getText().toString().trim().isEmpty()
                        ||binding.inputCode2.getText().toString().trim().isEmpty()
                        ||binding.inputCode3.getText().toString().trim().isEmpty()
                        ||binding.inputCode4.getText().toString().trim().isEmpty()
                        ||binding.inputCode5.getText().toString().trim().isEmpty()
                        ||binding.inputCode6.getText().toString().trim().isEmpty()){
                    Toast.makeText(VerificationActivity2.this,"Please enter valid code",Toast.LENGTH_SHORT).show();
                      return;
                }

                String code = binding.inputCode1.getText().toString()+
                        binding.inputCode2.getText().toString()+
                        binding.inputCode3.getText().toString()+
                        binding.inputCode4.getText().toString()+
                        binding.inputCode5.getText().toString()+
                        binding.inputCode6.getText().toString();

                if(verificationId!=null){
                    loading(true );
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    loading(false);
                                    if(task.isSuccessful()) {


                                          if(Objects.equals(activityWork, "setPassword")){

                                        profile.mobileNo = getIntent().getStringExtra("mobile");
                                        countDownTimer.cancel();
                                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                                        HashMap<String, Object> user = new HashMap<>();
                                        user.put(Constants.KEY_PROFILE_PROGRESS,1);
                                        user.put(Constants.KEY_CALL_WORD, profile.callWord);
                                        user.put(Constants.KEY_CREATED_BY, profile.createBy);
                                        user.put(Constants.KEY_GENDER, profile.gender);
                                        String name = profile.firstName + " " + profile.lastName;
                                        user.put(Constants.KEY_NAME, name );
                                        user.put(Constants.KEY_SEARCH,name.toLowerCase());
                                        user.put(Constants.KEY_FIRST_NAME, profile.firstName);
                                        user.put(Constants.KEY_LAST_NAME, profile.lastName);
                                        user.put(Constants.KEY_DATE_OF_DOB, profile.dd);
                                        user.put(Constants.KEY_MONTH_OF_DOB, profile.mm);
                                        user.put(Constants.KEY_YEAR_OF_DOB, profile.yyyy);
                                        user.put(Constants.KEY_STATE, profile.state);
                                        user.put(Constants.KEY_DISTRICT, profile.district);
                                        user.put(Constants.KEY_FULL_ADDRESS, profile.fullAddress);
                                        user.put(Constants.KEY_LIVE_WITH_FAMILY, profile.liveWithFamily);
                                        user.put(Constants.KEY_WHERE_FAMILY_LIVE,profile.whereFamilyLive);
                                        user.put(Constants.KEY_MARITAL_STATUS, profile.maritalStatus);
                                        user.put(Constants.KEY_DIET, profile.diet);
                                        user.put(Constants.KEY_HEIGHT, profile.height);
                                        user.put(Constants.KEY_WEIGHT, profile.weight);
                                        user.put(Constants.KEY_MOTHER_TONGUE, profile.motherTongue);
                                        user.put(Constants.KEY_HIGHEST_QUALIFICATION, profile.highestQualification);
                                        user.put(Constants.KEY_JOB_DESCRIPTION, profile.jobDescription);
                                        user.put(Constants.KEY_INCOME, profile.income);
                                        user.put(Constants.KEY_CASTE, profile.caste);
                                        user.put(Constants.KEY_SUB_CASTE, profile.subCast);
                                        user.put(Constants.KEY_GOTRA, profile.gotra);
                                        user.put(Constants.KEY_FATHER_NAME, profile.fatherName);
                                        user.put(Constants.KEY_FATHER_OCCUPATION, profile.fatherOccupation);
                                        user.put(Constants.KEY_MOTHER_NAME, profile.motherName);
                                        user.put(Constants.KEY_MOTHER_OCCUPATION, profile.motherOccupation);
                                        user.put(Constants.KEY_MOBILE_NO, profile.mobileNo);


                                        database.collection(Constants.KEY_COLLECTION_USER)
                                                .add(user)
                                                .addOnSuccessListener(documentReference -> {
                                                    loading(false);
                                                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                                                    preferenceManager.putString(Constants.KEY_FIRST_NAME, profile.firstName);
                                                    preferenceManager.putString(Constants.KEY_LAST_NAME, profile.lastName);
                                                    preferenceManager.putString(Constants.KEY_NAME,profile.firstName+" "+profile.lastName);
                                                    preferenceManager.putString(Constants.KEY_MOBILE_NO, profile.mobileNo);
                                                    preferenceManager.putString(Constants.KEY_CALL_WORD,profile.callWord);
                                                    preferenceManager.putString(Constants.KEY_GENDER, profile.gender);
                                                    preferenceManager.putString(Constants.KEY_PLACE_OF_BIRTH,profile.district+","+profile.state);
                                                    preferenceManager.putString(Constants.KEY_AGE,  (Year.now().getValue()- profile.yyyy) +" year old");
                                                    preferenceManager.putString(Constants.KEY_JOB_DESCRIPTION,profile.jobDescription);

                                                    Intent intent = new Intent(getApplicationContext(), SetPassword.class);
                                                    intent.putExtra("type","setPassword");
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                })
                                                .addOnFailureListener(exception -> {
                                                    loading(false);
                                                    Toast.makeText(VerificationActivity2.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    }else if(Objects.equals(activityWork, "forgetPassword")){


                                                              Intent intent = new Intent(getApplicationContext(), SetPassword.class);
                                                              intent.putExtra("type","forgetPassword");
                                                              intent.putExtra("mobileNo",getIntent().getStringExtra("mobile"));
                                                              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                              startActivity(intent);




                                          }


                                    }else{
                                        Toast.makeText(VerificationActivity2.this,"The verification code entered was invalid",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }

            }
        });






        binding.textResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.textResendOTP.getText().toString().equals("Resend OTP")){
                    Toast.makeText(getApplicationContext(),"OTP send successfully",Toast.LENGTH_SHORT).show();
                    countDownTimer.start();


                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91"+getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        VerificationActivity2.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(VerificationActivity2.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = newVerificationId;
                                countDownTimer.start();
                                Toast.makeText(VerificationActivity2.this,"OTP Sent",Toast.LENGTH_SHORT).show();
                            }
                        }
                );

            }
            }
        });


            }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.buttonVerify.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonVerify.setVisibility(View.VISIBLE);
        }
    }
    private void setOTPInputs(){
        binding.inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.inputCode2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.inputCode3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        binding.inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.inputCode4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        binding.inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    binding.inputCode5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        binding.inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    binding.inputCode6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        binding.inputCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.buttonVerify.performClick();

            }
        });
        binding.inputCode6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(binding.inputCode6.getText().toString().isEmpty()) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        binding.inputCode5.setText(null);
                        binding.inputCode5.requestFocus();
                    }
                }
                return false;
            }
        });
        binding.inputCode5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(binding.inputCode6.getText().toString().isEmpty()) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        binding.inputCode4.setText(null);
                        binding.inputCode4.requestFocus();
                    }
                }
                return false;
            }
        });
        binding.inputCode4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(binding.inputCode6.getText().toString().isEmpty()) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        binding.inputCode3.setText(null);
                        binding.inputCode3.requestFocus();
                    }
                }
                return false;
            }
        });
        binding.inputCode3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(binding.inputCode6.getText().toString().isEmpty()) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        binding.inputCode2.setText(null);
                        binding.inputCode2.requestFocus();
                    }
                }
                return false;
            }
        });
        binding.inputCode2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(binding.inputCode6.getText().toString().isEmpty()) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        binding.inputCode1.setText(null);
                        binding.inputCode1.requestFocus();
                    }
                }
                return false;
            }
        });

    }

}





















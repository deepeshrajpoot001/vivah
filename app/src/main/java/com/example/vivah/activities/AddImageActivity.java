package com.example.vivah.activities;



import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vivah.adapters.ImageAdapter;
import com.example.vivah.databinding.ActivityAddImageBinding;
import com.example.vivah.models.Profile;
import com.example.vivah.utilities.Constants;
import com.example.vivah.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;


public class AddImageActivity extends AppCompatActivity {
    private ActivityAddImageBinding binding;
    private ActivityResultLauncher<String> mGetContent;

    private PreferenceManager preferenceManager;
   private Uri profileImage;
    private ArrayList<String> moreImageUrlArray;
    private ImageAdapter imageAdapter;
    private Boolean isMoreImageUpload = false;
    private Boolean isProfileImageUpload = false;
   private FirebaseStorage firebaseStorage;
   private FirebaseFirestore database;
   private DocumentReference documentReference;


    private String profileImageUrl;
   private   int progress = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        firebaseStorage = FirebaseStorage.getInstance();
        moreImageUrlArray = new ArrayList<>();
        preferenceManager = new PreferenceManager(getApplicationContext());








        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading(true);
                if(isProfileImageUpload&&isMoreImageUpload) {

                    database = FirebaseFirestore.getInstance();
                    DocumentReference documentReference =
                            database.collection(Constants.KEY_COLLECTION_USER).document(preferenceManager.getString(Constants.KEY_USER_ID));
                    documentReference.update(
                            Constants.KEY_PROFILE_IMAGE,profileImageUrl,
                            Constants.KEY_MORE_IMAGE,moreImageUrlArray,
                            Constants.KEY_PROFILE_PROGRESS,progress
                    );

                    preferenceManager.putString(Constants.KEY_PROFILE_IMAGE,profileImageUrl);


                            Intent intent = new Intent(getApplicationContext(), CreateProfile6.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            loading(false);
                            startActivity(intent);
            }else{
                    Toast.makeText(getApplicationContext(),"add at least two image",Toast.LENGTH_SHORT).show();
                    loading(false);
                }


            }
        });




        // one two four firvdsfdddd,,,kkkkkkkk
        //dfsdfsd
        //sdfsdfsdf
        //sdfsdfsdsdfsdsdf
        //sfdsfsdsfsdfsdfsdfsdfsdfsdfsdfsdf
      //  uri.add(Uri.parse("android.resource://com.example.vivah/drawable/ic_add"));
        imageAdapter = new ImageAdapter(moreImageUrlArray, AddImageActivity.this);
        binding.moreImageRecyclerView.setLayoutManager(new GridLayoutManager(AddImageActivity.this,4));
        binding.moreImageRecyclerView.setAdapter(imageAdapter);




        if(ContextCompat.checkSelfPermission(AddImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddImageActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},103);
        }


      binding.addMoreImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreImageLoading(true);
                Intent iGallery = new Intent();
                iGallery.setType("image/*");
                iGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                iGallery.setAction(Intent.ACTION_GET_CONTENT);
                pickMoreImage.launch(Intent.createChooser(iGallery,"Select Picture"));
             //  startActivityForResult(Intent.createChooser(iGallery,"Select Picture"),1);fff
            }
        });



  binding.imageProfile.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          mGetContent.launch("image/*");

      }
  });

  mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
      @Override
      public void onActivityResult(Uri result) {
          if(result!=null) {
              Intent intent = new Intent(AddImageActivity.this, CropperActivity.class);
              intent.putExtra("DATA", result.toString());
              pickProfileImage.launch(intent);
          }
      }
  });



        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){

            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_USER)
                    .document(preferenceManager.getString(Constants.KEY_USER_ID))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.get(Constants.KEY_PROFILE_PROGRESS).toString().equals("6")) {
                                progress = 6;
                                isMoreImageUpload = true;
                                isProfileImageUpload = true;
                                profileImageUrl = documentSnapshot.getString(Constants.KEY_PROFILE_IMAGE);
                                Glide.with(binding.imageProfile).load(profileImageUrl).into(binding.imageProfile);
                                Profile pr = documentSnapshot.toObject(Profile.class);
                                assert pr != null;
                                ArrayList<String>  s = pr.more_image;
                                for(int i=0;i<s.size();i++) {
                                    moreImageUrlArray.add(s.get(i));

                                }
                                imageAdapter.notifyDataSetChanged();


                            }
                        }
                    });
        }



    }

    private final ActivityResultLauncher<Intent> pickProfileImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){

                        if(result.getData().getStringExtra("RESULT").isEmpty()){
                            return;
                        }
                        loading_Image(true);
                        String resultProfileImage = result.getData().getStringExtra("RESULT");
                        binding.moreImageTextView.setVisibility(View.VISIBLE);
                        Uri resultUri = null;
                        if (resultProfileImage != null) {
                            resultUri = Uri.parse(resultProfileImage);
                        }

                            profileImage =  resultUri;

                        StorageReference storageReference = firebaseStorage.getReference(preferenceManager.getString(Constants.KEY_FIRST_NAME)+" "+preferenceManager.getString(Constants.KEY_LAST_NAME) + "/profileImage");
                        storageReference.putFile(profileImage)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        //profileImageUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();


                                       if (taskSnapshot.getMetadata() != null) {
                                            if (taskSnapshot.getMetadata().getReference() != null){
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    profileImageUrl = uri.toString();

                                                }
                                            });
                                        }
                                       }




                                        binding.imageProfile.setImageURI(profileImage);
                                        isProfileImageUpload = true;
                                        loading_Image(false);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loading_Image(false);
                                         Toast.makeText(getApplicationContext(),"reupload image",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                    }
                                });



                    }
                }
            });


    private final ActivityResultLauncher<Intent> pickMoreImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData().getClipData()!=null) {
                        int x = result.getData().getClipData().getItemCount();
                        for (int i = 0; i < x; i++) {

                            Uri moreImageResultUri = result.getData().getClipData().getItemAt(i).getUri();

                            StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference("uploads");
                            StorageReference storageReference = firebaseStorage.child(String.valueOf(System.currentTimeMillis()));
                            storageReference.putFile(moreImageResultUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            //  moreImageUrlArray.add(imageUrl);

                                            if (taskSnapshot.getMetadata() != null) {
                                                if (taskSnapshot.getMetadata().getReference() != null) {
                                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            String imageUrl = uri.toString();
                                                            moreImageUrlArray.add(imageUrl);
                                                            imageAdapter.notifyDataSetChanged();
                                                        }
                                                    });
                                                }
                                            }


                                            Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                                            isMoreImageUpload = true;
                                            imageAdapter.notifyDataSetChanged();
                                            moreImageLoading(false);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {


                                        }
                                    });


                        }
                    }else if(result.getData().getData()!=null){
                        Uri moreImageResultUri = result.getData().getData();

                        StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference("uploads");
                        StorageReference storageReference = firebaseStorage.child(String.valueOf(System.currentTimeMillis()));
                        storageReference.putFile(result.getData().getData())
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        if (taskSnapshot.getMetadata() != null) {
                                            if (taskSnapshot.getMetadata().getReference() != null) {
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        moreImageUrlArray.add(imageUrl);
                                                        imageAdapter.notifyDataSetChanged();
                                                    }
                                                });
                                            }
                                        }

                                        Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                                        isMoreImageUpload = true;
                                        imageAdapter.notifyDataSetChanged();
                                        moreImageLoading(false);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                    }
                                });


                    }



                }
            });

    private String getFireExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.nextButton.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.GONE);
            binding.nextButton.setVisibility(View.VISIBLE);
        }
    }
    private void loading_Image(Boolean isLoading){
        if(isLoading){
            binding.imageProfile.setVisibility(View.INVISIBLE);
            binding.progressBarProfile.setVisibility(View.VISIBLE);
        }else{
            binding.progressBarProfile.setVisibility(View.GONE);
            binding.imageProfile.setVisibility(View.VISIBLE);
        }
    }

    private void moreImageLoading(Boolean isLoading){
        if(isLoading){
            binding.addMoreImages.setVisibility(View.INVISIBLE);
            binding.addImageProgressBar.setVisibility(View.VISIBLE);
            binding.message.setVisibility(View.VISIBLE);
        }else{
            binding.addMoreImages.setVisibility(View.VISIBLE);
            binding.addImageProgressBar.setVisibility(View.INVISIBLE);
            binding.message.setVisibility(View.INVISIBLE);
        }


}
}
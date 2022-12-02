package com.paszlelab.dcroarapp.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.StorageReference;
import com.paszlelab.dcroarapp.Activity.LoginPage;
import com.paszlelab.dcroarapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment{

    private static final String TAG = "Profile Activity";
    private static final String TAG2 = "Check User Data >>";
    TextView txtChangePic, txtUpdateData,txtResetPassword, txtSignOut;
    ImageView imgProfilePic, addProfilePic;
    View view;
    EditText edFName, edLName, edEmail, edPhoneNumber, edPassword;

    //Get user Data to form
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DatabaseReference reference;
    StorageReference storageReference;
//    CollectionReference collection;
    String userId;

    //Hold User detail
    private String FName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View fragmentView;
    private View v;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile
                , container, false);

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.fragmentView = view;

        fAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Student");

        //See who's logging in
        FirebaseUser user =  fAuth.getCurrentUser();
        Log.d(TAG, "User ID is >>" + user.getUid());

        // Get all the user data and put it to form field
        edFName = view.findViewById(R.id.edFName);
        edLName = view.findViewById(R.id.edLName);
        edEmail = view.findViewById(R.id.edEmail);
        edEmail.setEnabled(false);
        edPhoneNumber = view.findViewById(R.id.edPhoneNumber);
        loadNote();


        //=========================        0         ======================================================
        // Change Profile Picture

        imgProfilePic = view.findViewById(R.id.imgProfilePic);
        addProfilePic = view.findViewById(R.id.addProfilePic);
        addProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();

            }
        });

        // =================================================================================================


        //======================        1        ===========================================================
        //Update User Data

        txtUpdateData = view.findViewById(R.id.txtUpdateData);
        txtUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserData();
            }
        });
        //=====================================================================================




        //======================       2        ===========================================================
        //Reset User Password

        txtResetPassword = view.findViewById(R.id.txtResetPassword);
        txtResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString().trim();
                Toast.makeText(getContext(), "Check your email to reset your password", Toast.LENGTH_SHORT).show();
                if(email.length() > 8){
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "Check your email to reset your password", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getContext(), "Error! Your request is denied \n Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        //===========================================================================================================



        //=============================     3     =========================================================================
        //SignOut User
        txtSignOut = view.findViewById(R.id.txtSignOut);
        txtSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(getActivity(), LoginPage.class));
            }
        });
    }
    //============================================================================================================




    //==================        4       =================================================================================
    //Part of Load User data to the form
    public void loadNote(){
        fStore = FirebaseFirestore.getInstance();
        FirebaseUser user2 =  fAuth.getCurrentUser();

        DocumentReference noteRef = fStore.collection("Student").document(user2.getUid());

        noteRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document != null && document.exists()){
                        String firstName = (String) document.getString("firstName");
                        String lastName = (String) document.getString("lastName");
                        String emailAddress = (String) document.getString("emailAddress");
                        String phNo = (String) document.getString("phoneNumber");
//                        Log.d("TAG", "First Name >>>" + firstName);
                        edFName.setText(firstName);
                        edLName.setText(lastName);
                        edEmail.setText(emailAddress);
                        edPhoneNumber.setText(phNo);

                    }else{
                        Log.d(TAG, "No such document");
                    }
                }else{
                    Log.d(TAG, "failed asu !!!");
                }
            }
        });
    }
    // =============================================================================================



    //===================================    5     =================================================
    //To open dialog box when user click the plus button to change profile picture
    private void chooseProfilePicture(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("").setTitle("Profile Picture");

        //Set message & Perform action on button click
        builder.setMessage("Please choose your picture")
                .setCancelable(false)
                .setPositiveButton("Take Photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity().getApplicationContext(), "Camera is opened", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Choose Photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity().getApplicationContext(), "Library is opened", Toast.LENGTH_SHORT).show();
                        takePictureFromGallery();
                    }
                });
        //Creating dialog box
        AlertDialog alert =builder.create();
        alert.setTitle("Select Profile Picture");
        alert.show();
    }
    // =============================================================================================

    // =======================     5.1    ==========================================================
    // Take picture from gallery
     private void takePictureFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    public ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                Uri imageUri = data.getData();
                imgProfilePic.setImageURI(imageUri);
//                StorageReference childRef = storageReference.child("profileImages").child(userId + ".jpg");
//                UploadTask uploadTask = childRef.putFile(imageUri);
//                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(getActivity(), "upload successful", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getActivity(), "upload messed up", Toast.LENGTH_SHORT).show();
//                    }
//                });

            }else{
                Toast.makeText(getActivity(), "Image is not selected >>", Toast.LENGTH_SHORT).show();
            }
        }
    });

    // =============================================================================================





    // ============================       6        ======================================================
    //Part of user update feature
    public void updateUserData(){
        fStore = FirebaseFirestore.getInstance();
        FirebaseUser user3 = fAuth.getCurrentUser();
        final DocumentReference sfDocRef = fStore.collection("Student").document(user3.getUid());
        fStore.runTransaction(new Transaction.Function<Object>() {
            @Nullable
            @Override
            public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);

                //Updating new user field from the form
                String fName = edFName.getText().toString();
                String lName = edLName.getText().toString();
                String pNumber = edPhoneNumber.getText().toString();

                //Send transaction back to Firebase
                transaction.update(sfDocRef, "firstName",fName);
                transaction.update(sfDocRef, "lastName",lName);
                transaction.update(sfDocRef, "phoneNumber",pNumber);

                //success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Object>(){
            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "User is updated !");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "User is not updated.", e);
            }
        });
    }
    // =======================================================================================



}
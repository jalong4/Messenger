package ca.jimlong.messenger.controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import ca.jimlong.messenger.utils.Utils;
import ca.jimlong.messenger.R;
import ca.jimlong.messenger.models.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private CircleImageView mProfileImage;
    private TextView mProfileImageLabel;
    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegisterButton;
    private TextView mAlreadyHaveAccountTextView;
    private FirebaseAuth mAuth;

    private Uri mSelectedPhoto;
    private String mProfileImageUrl;

    private static final String TAG = "RegisterActivity";
    static final int PICK_IMAGE_REQUEST = 0;  // The request code


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mProfileImage = (CircleImageView) findViewById(R.id.profile_image_circleimageview);
        mProfileImageLabel = (TextView) findViewById(R.id.profile_image_textview);
        mUsername = (EditText) findViewById(R.id.username_edittext);
        mEmail = (EditText) findViewById(R.id.email_edittext);
        mPassword = (EditText) findViewById(R.id.password_edittext);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mAlreadyHaveAccountTextView = (TextView) findViewById(R.id.already_have_account_textview);

        mAuth = FirebaseAuth.getInstance();

        mProfileImage.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });


        mRegisterButton.setOnClickListener (v -> {

            String username = mUsername.getText().toString();
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();

            Log.d(TAG, "Username: " + username);
            Log.d(TAG, "Email: " + email);
            Log.d(TAG, "Password: " + password);

            createUser(email, password);
        });

        mAlreadyHaveAccountTextView.setOnClickListener(v -> {
            Intent intent = new Intent(this,  LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Utils.updateUI(currentUser, "logged in");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != PICK_IMAGE_REQUEST || resultCode != Activity.RESULT_OK || data == null) return;

        mSelectedPhoto = data.getData();
        Log.d(TAG, "Activity complete with result: " + mSelectedPhoto.toString());


        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectedPhoto);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        mProfileImageLabel.setText("");
        mProfileImage.setImageBitmap(bitmap);
    }

    private void createUser(String email, String password) {

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email/pw", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    uploadImage();
                    Utils.updateUI(mAuth.getCurrentUser(), "created");
                })

                .addOnFailureListener(this, task -> {
                    Toast.makeText(this, "Failed to Create User", Toast.LENGTH_LONG).show();
                    Utils.updateUI(null, "created: " + task.getMessage());
                });


    }

    private void uploadImage() {

        if (mSelectedPhoto == null) {
            saveUserToDatabase();
            return;
        }

        String filename = UUID.randomUUID().toString();
        StorageReference  ref = FirebaseStorage.getInstance().getReference("/images/" + filename);

        ref.putFile(mSelectedPhoto)
                .addOnCompleteListener(task -> {
                    Log.d(TAG, "Successfully uploaded photo: " + task.getResult().getMetadata().getPath());
                    ref.getDownloadUrl().addOnSuccessListener(downloadUrlTask -> {
                        mProfileImageUrl = downloadUrlTask.toString();
                        Log.d(TAG, "Photo Url: " + mProfileImageUrl);
                        saveUserToDatabase();
                    });
                })
                .addOnFailureListener(task -> {
                    Log.d(TAG, "Failed to uploaded photo");
                    saveUserToDatabase();
                    return;
                });
    }

    private void saveUserToDatabase() {

        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference("/users/" + uid);

        User user = new User(uid, mUsername.getText().toString(), mProfileImageUrl);
        Log.d(TAG, "Saving User\n" + user.toString() + "\n");

        ref.setValue(user)
                .addOnSuccessListener(task -> {
                    Log.d(TAG, "User saved to database successfully");

                    Intent intent = new Intent(this, MessagesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(task -> {
                    Log.d(TAG, "Failed to save user to database");
                });
    }


}

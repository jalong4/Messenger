package ca.jimlong.messenger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginButton;
    private TextView mBackToRegistrationTextView;
    private FirebaseAuth mAuth;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.email_edittext);
        mPassword = (EditText) findViewById(R.id.password_edittext);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mBackToRegistrationTextView = (TextView) findViewById(R.id.back_to_registration_textview);

        mAuth = FirebaseAuth.getInstance();

        mLoginButton.setOnClickListener(v -> {

            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();

            Log.d(TAG, "Email: " + email);
            Log.d(TAG, "Password: " + password);

            login(email, password);
        });

        mBackToRegistrationTextView.setOnClickListener(v -> finish());
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        AuthUtils.updateUI(currentUser, "logged in");
    }

    private void login(String email, String password) {

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email/pw", Toast.LENGTH_LONG).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) return;
                    AuthUtils.updateUI(mAuth.getCurrentUser(), "logged in");
                })

                .addOnFailureListener(this, task -> {
                    Toast.makeText(this, "Failed to Login", Toast.LENGTH_LONG).show();
                    AuthUtils.updateUI(null, "logged in: " + task.getMessage());
                });
    }
}

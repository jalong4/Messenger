package ca.jimlong.messenger;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthUtils {

    private static final String TAG = "AuthUtils";

    public static void updateUI(FirebaseUser user, String action) {

        if (user == null) {
            Log.d(TAG, "User not " + action);
        } else {
            Log.d(TAG, "User " + action + ", uid: " + user.getUid());
        }
    }


    public static void verifyUserIsLoggedIn(Context context) {
        String uid = FirebaseAuth.getInstance().getUid();

        if (uid == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }
}

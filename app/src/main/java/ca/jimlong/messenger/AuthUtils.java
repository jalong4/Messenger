package ca.jimlong.messenger;

import android.util.Log;
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
}

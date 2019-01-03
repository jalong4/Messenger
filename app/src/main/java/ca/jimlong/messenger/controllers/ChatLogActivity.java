package ca.jimlong.messenger.controllers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.jimlong.messenger.R;
import ca.jimlong.messenger.adapters.ChatLogAdapter;
import ca.jimlong.messenger.models.ChatMessage;
import ca.jimlong.messenger.models.User;
import ca.jimlong.messenger.utils.Utils;

public class ChatLogActivity extends AppCompatActivity {

    private ChatLogAdapter mChatLogAdapter;
    private RecyclerView mChatLogs;
    private TextView mMessage;
    private Button mSendButton;
    private User mToUser;
    private User mFromUser;

    private static final String TAG = "ChatLogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_log);

        mToUser = (User) getIntent().getParcelableExtra(User.KEY);

        getSupportActionBar().setTitle(mToUser.getUsername());

        mMessage = (TextView) findViewById(R.id.message_edittext_chat_log);
        mSendButton = (Button) findViewById(R.id.send_button_chat_log);

        mChatLogs = (RecyclerView) findViewById(R.id.chat_log_recylerview);

        mSendButton.setOnClickListener(v -> {

            String fromUid = Utils.myUid();
            String toUid = mToUser.getUid();

            DatabaseReference refFrom = FirebaseDatabase.getInstance().getReference("/user-messages/" + fromUid + "/" + toUid).push();
            DatabaseReference refTo = FirebaseDatabase.getInstance().getReference("/user-messages/" + toUid  + "/" + fromUid).push();
            DatabaseReference refLatestFrom = FirebaseDatabase.getInstance().getReference("/latest-messages/" + fromUid + "/" + toUid);
            DatabaseReference refLatestTo = FirebaseDatabase.getInstance().getReference("/latest-messages/" + toUid  + "/" + fromUid);

            ChatMessage chatMessage = new ChatMessage(refFrom.getKey(),
                    mMessage.getText().toString(),
                    Utils.myUid(),
                    mToUser.getUid(),
                    System.currentTimeMillis() / 1000);

            refFrom.setValue(chatMessage)
                    .addOnSuccessListener(task -> {
                        Log.d(TAG, "ChatMessage From message saved successfully");
                        mMessage.setText("");
                    })
                    .addOnFailureListener(task -> {
                        Log.d(TAG, "Failed to save chatMessage From message");
                    });

            chatMessage.setId(refTo.getKey());
            refTo.setValue(chatMessage)
                    .addOnSuccessListener(task -> {
                        Log.d(TAG, "ChatMessage To message saved successfully");
                        mMessage.setText("");
                    })
                    .addOnFailureListener(task -> {
                        Log.d(TAG, "Failed to save chatMessage To message");
                    });


            refLatestFrom.setValue(chatMessage)
                    .addOnSuccessListener(task -> {
                        Log.d(TAG, "LatestFrom ChatMessage message saved successfully");
                        mMessage.setText("");
                    })
                    .addOnFailureListener(task -> {
                        Log.d(TAG, "Failed to save latestFrom chatMessage message");
                    });

            refLatestTo.setValue(chatMessage)
                    .addOnSuccessListener(task -> {
                        Log.d(TAG, "LatestTo ChatMessage message saved successfully");
                        mMessage.setText("");
                    })
                    .addOnFailureListener(task -> {
                        Log.d(TAG, "Failed to save latestTo chatMessage message");
                    });
        });

        // Look up (LoggedIn) FromUser and Load ChatLog
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/users/" + Utils.myUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFromUser = dataSnapshot.getValue(User.class);
                mChatLogAdapter = new ChatLogAdapter(mFromUser, mToUser);
                mChatLogs.setAdapter(mChatLogAdapter);
                loadChatLog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void loadChatLog() {

        String myUid = Utils.myUid();

        mChatLogs.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mChatLogs.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mChatLogAdapter.getItemCount() > 0) {
                                mChatLogs.smoothScrollToPosition(mChatLogAdapter.getItemCount() - 1);
                            }
                        }
                    }, 100);
                }
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/user-messages/" + mFromUser.getUid() + "/" + mToUser.getUid());

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);

                mChatLogAdapter.addItem(chatMessage);
                mChatLogAdapter.notifyDataSetChanged();

                if (mChatLogAdapter.getItemCount() > 0) {
                    mChatLogs.scrollToPosition(mChatLogAdapter.getItemCount() - 1);
                    Log.d(TAG, "Message added to log");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

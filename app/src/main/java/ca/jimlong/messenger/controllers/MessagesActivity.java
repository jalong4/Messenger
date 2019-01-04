package ca.jimlong.messenger.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import ca.jimlong.messenger.adapters.MessageAdapter;
import ca.jimlong.messenger.models.ChatMessage;
import ca.jimlong.messenger.models.Message;
import ca.jimlong.messenger.models.User;
import ca.jimlong.messenger.utils.MenuTintUtils;
import ca.jimlong.messenger.utils.Utils;
import ca.jimlong.messenger.R;

public class MessagesActivity extends AppCompatActivity implements MessageAdapter.OnItemClicked {

    private MessageAdapter mMessageAdapter;
    private RecyclerView mMessages;
    private static final String TAG = "MessagesActivity";
    private User mFromUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Utils.verifyUserIsLoggedIn(this);

        mMessages = (RecyclerView) findViewById(R.id.messages_recyclerView_messages);
        mMessageAdapter = new MessageAdapter();
        mMessageAdapter.setOnClick(MessagesActivity.this);
        mMessages.setAdapter(mMessageAdapter);
        mMessages.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));


        // Look up (LoggedIn) FromUser and Load ChatLog
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/users/" + Utils.myUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFromUser = dataSnapshot.getValue(User.class);
                if (mFromUser != null && mFromUser.getUsername() != null) {
                    getSupportActionBar().setTitle(mFromUser.getUsername());
                    loadMessages();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }


    private void loadMessages() {

        String dbPath = "/latest-messages/" + Utils.myUid();
        Log.d(TAG, "creating db ref for: " + dbPath);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(dbPath);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);

                String partnerId = (chatMessage.getToId().equals(Utils.myUid())) ? chatMessage.getFromId() : chatMessage.getToId();
                DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("/users/" + partnerId);
                refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        Message m = new Message(chatMessage.getId(), chatMessage.getFromId(), chatMessage.getToId(), u.getUsername(), chatMessage.getText(), u.getProfileImageUrl(), chatMessage.getTimestamp());
                        mMessageAdapter.addItem(m);
                        mMessageAdapter.notifyDataSetChanged();

                        if (mMessageAdapter.getItemCount() > 0) {
                            mMessages.scrollToPosition(mMessageAdapter.getItemCount() - 1);
                            Log.d(TAG, "Message added to log");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);

                String partnerId = (chatMessage.getToId().equals(Utils.myUid())) ? chatMessage.getFromId() : chatMessage.getToId();
                DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("/users/" + partnerId);
                refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        Message m = new Message(chatMessage.getId(), chatMessage.getFromId(), chatMessage.getToId(), u.getUsername(), chatMessage.getText(), u.getProfileImageUrl(), chatMessage.getTimestamp());
                        mMessageAdapter.replaceItem(m);
                        mMessageAdapter.notifyDataSetChanged();

                        if (mMessageAdapter.getItemCount() > 0) {
                            mMessages.scrollToPosition(mMessageAdapter.getItemCount() - 1);
                            Log.d(TAG, "Message changed");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
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
        mMessageAdapter.notifyDataSetChanged();

    }

    private void setTitleToCurrentUser() {
        getSupportActionBar().setTitle("");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/users/" + Utils.myUid());

        ref.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);
                getSupportActionBar().setTitle(username);
                Log.d(TAG, "username: " + username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "Row " + Integer.toString(position) + " clicked!");
        Intent intent = new Intent(this, ChatLogActivity.class);
        Message message = mMessageAdapter.getItems().get(position);

        String partnerId = (message.getToId().equals(Utils.myUid())) ? message.getFromId() : message.getToId();
        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("/users/" + partnerId);
        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                intent.putExtra(User.KEY, u);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_log_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            case R.id.menu_new_message:
                startActivity(new Intent(this, NewMessageActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        MenuTintUtils.tintAllIcons(menu, Color.WHITE);
        return super.onCreateOptionsMenu(menu);
    }
}

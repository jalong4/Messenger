package ca.jimlong.messenger.controllers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.jimlong.messenger.R;
import ca.jimlong.messenger.adapters.UserAdapter;
import ca.jimlong.messenger.models.User;
import ca.jimlong.messenger.utils.Utils;

public class NewMessageActivity extends AppCompatActivity implements UserAdapter.OnItemClicked {


    private UserAdapter mUserAdapter = new UserAdapter();
    private RecyclerView mUsers;

    private static final String TAG = "NewMessageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        getSupportActionBar().setTitle("Select User");

        mUsers = (RecyclerView) findViewById(R.id.users_recycleview);
        mUsers.setAdapter(mUserAdapter);
        mUserAdapter.setOnClick(NewMessageActivity.this);

        loadUsers();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ChatLogActivity.class);
        intent.putExtra(User.KEY, mUserAdapter.getItems().get(position));
        startActivity(intent);
        finish();
    }

    private void loadUsers() {

        String myUid = Utils.myUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    User user = d.getValue(User.class);
                    if (myUid.equals(user.getUid())) {
                        continue;  // exclude self
                    }
                    mUserAdapter.getItems().add(user);
                }
                mUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

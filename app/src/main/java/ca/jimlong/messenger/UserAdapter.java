package ca.jimlong.messenger;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<User> mUsers;

    public UserAdapter() {
        mUsers = new ArrayList<User>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView mProfileImage;
        public TextView mUsernameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mProfileImage = (CircleImageView) itemView.findViewById(R.id.profileImage_imageview);
            mUsernameTextView = (TextView) itemView.findViewById(R.id.username_textview);

        }
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View userView = inflater.inflate(R.layout.user_row_new_message, parent, false);
        return new ViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder viewHolder, int position) {
        User user = mUsers.get(position);
        Picasso.get().load(user.getProfileImageUrl()).into(viewHolder.mProfileImage);
        viewHolder.mUsernameTextView.setText(user.getUsername());
    }


    public List<User> getItems() {
        return mUsers;
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

}

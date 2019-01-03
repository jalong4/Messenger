package ca.jimlong.messenger.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import ca.jimlong.messenger.R;
import ca.jimlong.messenger.controllers.ChatLogActivity;
import ca.jimlong.messenger.models.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> mUsers;

    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(int position);
    }

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

        viewHolder.itemView.setOnClickListener(v -> onClick.onItemClick(position));
    }


    public List<User> getItems() {
        return mUsers;
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void setOnClick(OnItemClicked onClick) { this.onClick = onClick; }

}

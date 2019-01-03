package ca.jimlong.messenger.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ca.jimlong.messenger.R;
import ca.jimlong.messenger.models.ChatMessage;
import ca.jimlong.messenger.models.ChatType;
import ca.jimlong.messenger.models.User;
import ca.jimlong.messenger.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Store a member variable for the contacts
    private List<ChatMessage> mChatMessages;
    private User mToUser;
    private User mFromUser;
    private static final String TAG = "ChatLogAdapter";

    public ChatLogAdapter(User fromUser, User toUser) {
        mFromUser = fromUser;
        mToUser = toUser;
        mChatMessages = new ArrayList<ChatMessage>();
    }

    public class ViewHolderFrom extends RecyclerView.ViewHolder {
        public CircleImageView mProfileImage;
        public TextView mMessage;

        public ViewHolderFrom(View itemView) {
            super(itemView);
            mProfileImage = (CircleImageView) itemView.findViewById(R.id.profileImage_chat_from);
            mMessage = (TextView) itemView.findViewById(R.id.message_chat_from);
        }
    }

    public class ViewHolderTo extends RecyclerView.ViewHolder {
        public CircleImageView mProfileImage;
        public TextView mMessage;

        public ViewHolderTo(View itemView) {
            super(itemView);
            mProfileImage = (CircleImageView) itemView.findViewById(R.id.profileImage_chat_to);
            mMessage = (TextView) itemView.findViewById(R.id.message_chat_to);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (ChatType.valueOf(viewType)) {
            case FROM:
                return new ViewHolderFrom(inflater.inflate(R.layout.chat_from_row, parent, false));
            case TO:
            default:
                return new ViewHolderTo(inflater.inflate(R.layout.chat_to_row, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mChatMessages.get(position);
        return (Utils.myUid().equals(chatMessage.getFromId())) ? ChatType.FROM.getValue() : ChatType.TO.getValue();

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ChatMessage chatMessage = mChatMessages.get(position);
        switch (ChatType.valueOf(viewHolder.getItemViewType())) {
            case FROM:
                ViewHolderFrom viewHolderFrom = (ViewHolderFrom) viewHolder;
                Picasso.get().load(mFromUser.getProfileImageUrl()).into(viewHolderFrom.mProfileImage);
                viewHolderFrom.mMessage.setText(chatMessage.getText());
                break;

            case TO:
                ViewHolderTo viewHolderTo = (ViewHolderTo) viewHolder;
                Picasso.get().load(mToUser.getProfileImageUrl()).into(viewHolderTo.mProfileImage);
                viewHolderTo.mMessage.setText(chatMessage.getText());
                break;
        }

    }


    public List<ChatMessage> getItems() {
        return mChatMessages;
    }

    @Override
    public int getItemCount() {
        return mChatMessages.size();
    }

    public void addItem(ChatMessage chatMessage) {
        mChatMessages.add(chatMessage);
    }

}


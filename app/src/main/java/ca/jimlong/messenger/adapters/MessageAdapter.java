package ca.jimlong.messenger.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.jimlong.messenger.R;
import ca.jimlong.messenger.models.ChatMessage;
import ca.jimlong.messenger.models.Message;
import ca.jimlong.messenger.models.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Map<String, Message> mMessagesMap = new HashMap<>();
    private List<Message> mMessages;

    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public MessageAdapter() {
        mMessages = new ArrayList<Message>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView mProfileImage;
        public TextView mUsernameTextView;
        public TextView mMessageTextView;
        public TextView mDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mProfileImage = (CircleImageView) itemView.findViewById(R.id.profile_image_cicrleimageview_message_row);
            mUsernameTextView = (TextView) itemView.findViewById(R.id.username_textview_message_row);
            mMessageTextView = (TextView) itemView.findViewById(R.id.message_textview_message_row);
            mDateTextView = (TextView) itemView.findViewById(R.id.date_textview_message_row);

        }
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View userView = inflater.inflate(R.layout.message_row, parent, false);
        return new ViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        viewHolder.mMessageTextView.setText(message.getText());
        viewHolder.mUsernameTextView.setText(message.getUsername());
        Picasso.get().load(message.getProfileImageUrl()).into(viewHolder.mProfileImage);
//        viewHolder.mDateTextView.setText(message.getDate());

        viewHolder.itemView.setOnClickListener(v -> onClick.onItemClick(position));
    }


    public List<Message> getItems() {
        return mMessages;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void addItem(Message message) {
        mMessages.add(message);
        mMessagesMap.put(message.getId(), message);
    }

    public void replaceItem(Message message) {
        String key = message.getId();
        if (mMessagesMap.containsKey(key)) {
            int index = mMessages.indexOf(mMessagesMap.get(key));
            mMessages.set(index, message);
            mMessagesMap.put(message.getId(), message);
        }
    }

    public void setOnClick(OnItemClicked onClick) { this.onClick = onClick; }

}

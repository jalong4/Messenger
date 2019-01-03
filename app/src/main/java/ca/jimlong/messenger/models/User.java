package ca.jimlong.messenger.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public static final String KEY = "User";

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private String uid;
    private String username;
    private String profileImageUrl;

    public User() {

    }

    public User(String uid, String username, String profileImageUrl) {
        this.uid = uid;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }


    public User(Parcel in){
        this.uid = in.readString();
        this.username =  in.readString();
        this.profileImageUrl =  in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.username);
        dest.writeString(this.profileImageUrl);
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }

}

package ca.jimlong.messenger;

public class User {

    private String uid;
    private String username;
    private String profileImageUrl;

    User() {

    }

    User(String uid, String username, String profileImageUrl) {
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

    public String toString() {
        return "uid: " + uid + "\n" + "username: " + username + "\n" + "profileImageUrl: " + profileImageUrl + "\n";
    }

}

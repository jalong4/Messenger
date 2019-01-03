package ca.jimlong.messenger.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Message {

    public static final String KEY = "Message";

    private String id;
    private String fromId;
    private String toId;
    private String username;
    private String text;
    private String profileImageUrl;
    private Long timestamp;

    public Message() {

    }

    public Message(String id, String fromId, String toId, String username, String text, String profileImageUrl, Long timestamp) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.username = username;
        this.text = text;
        this.profileImageUrl = profileImageUrl;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getToId() {
        return toId;
    }

    public String getFromId() {
        return fromId;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getDate() {
        return timeAgoSinceDate();
    }


    private String timeAgoSinceDate() {
        return "Just Now";
//        let calendar = Calendar.current
//        let now = currentDate
//        let earliest = (now as NSDate).earlierDate(date)
//        let latest = (earliest == now) ? date : now
//        let components:DateComponents = (calendar as NSCalendar).components([NSCalendar.Unit.minute , NSCalendar.Unit.hour , NSCalendar.Unit.day , NSCalendar.Unit.weekOfYear , NSCalendar.Unit.month , NSCalendar.Unit.year , NSCalendar.Unit.second], from: earliest, to: latest, options: NSCalendar.Options())
//
//        if (components.year! >= 2) {
//            return "\(components.year!) years ago"
//        } else if (components.year! >= 1){
//            if (numericDates){
//                return "1 year ago"
//            } else {
//                return "Last year"
//            }
//        } else if (components.month! >= 2) {
//            return "\(components.month!) months ago"
//        } else if (components.month! >= 1){
//            if (numericDates){
//                return "1 month ago"
//            } else {
//                return "Last month"
//            }
//        } else if (components.weekOfYear! >= 2) {
//            return "\(components.weekOfYear!) weeks ago"
//        } else if (components.weekOfYear! >= 1){
//            if (numericDates){
//                return "1 week ago"
//            } else {
//                return "Last week"
//            }
//        } else if (components.day! >= 2) {
//            return "\(components.day!) days ago"
//        } else if (components.day! >= 1){
//            if (numericDates){
//                return "1 day ago"
//            } else {
//                return "Yesterday"
//            }
//        } else if (components.hour! >= 2) {
//            return "\(components.hour!) hours ago"
//        } else if (components.hour! >= 1){
//            if (numericDates){
//                return "1 hour ago"
//            } else {
//                return "An hour ago"
//            }
//        } else if (components.minute! >= 2) {
//            return "\(components.minute!) minutes ago"
//        } else if (components.minute! >= 1){
//            if (numericDates){
//                return "1 minute ago"
//            } else {
//                return "A minute ago"
//            }
//        } else if (components.second! >= 3) {
//            return "\(components.second!) seconds ago"
//        } else {
//            return "Just now"
//        }

    }

}

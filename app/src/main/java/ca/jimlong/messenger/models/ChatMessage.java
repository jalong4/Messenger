package ca.jimlong.messenger.models;

public class ChatMessage {
    private String id;
    private String text;
    private String fromId;
    private String toId;
    private Long timestamp; // in seconds

    public ChatMessage() {

    }

    public ChatMessage(String id, String text, String fromId, String toId, Long timestamp) {
        this.id = id;
        this.text = text;
        this.fromId = fromId;
        this.toId = toId;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", fromId='" + fromId + '\'' +
                ", toId='" + toId + '\'' +
                ", timestamp='" + Long.toString(timestamp) + '\'' +
                '}';
    }

}

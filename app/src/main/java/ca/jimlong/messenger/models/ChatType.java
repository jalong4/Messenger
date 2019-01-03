package ca.jimlong.messenger.models;

import java.util.HashMap;
import java.util.Map;

public enum ChatType {
    FROM(0),
    TO(1);
    private final int value;

    private ChatType(int value) {
        this.value = value;
    }
    private static Map map = new HashMap<>();


    static {
        for (ChatType pageType : ChatType.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static ChatType valueOf(int pageType) {
        return (ChatType) map.get(pageType);
    }
    public int getValue() {
        return this.value;
    }
}

package utils;

import java.sql.PreparedStatement;

public class MapData {
    private Object value;
    private Consumer<PreparedStatement, Integer, Object> consumer;

    public MapData(Object value, Consumer<PreparedStatement, Integer, Object> consumer) {
        this.value = value;
        this.consumer = consumer;
    }

    public Object getValue() {
        return value;
    }

    public Consumer<PreparedStatement, Integer, Object> getConsumer() {
        return consumer;
    }
}

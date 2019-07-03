package utils;

import java.sql.SQLException;

public interface  Consumer <X, Y, Z> {
    void apply(X x, Y y, Z z) throws SQLException;
}

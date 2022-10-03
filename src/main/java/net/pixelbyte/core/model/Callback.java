package net.pixelbyte.core.model;

import java.sql.SQLException;

public interface Callback<T> {

        void call(T objectType) throws SQLException;
}

package net.pixelbyte.core.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Callback {

        void execute(ResultSet resultSet) throws SQLException;
}

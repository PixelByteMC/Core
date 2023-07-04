/*
 * Callback
 * Core
 *
 * Created by leobaehre on 7/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.pixelbyte.core.model;

import java.sql.SQLException;

public interface Callback<T> {

        void call(T objectType) throws SQLException;
}

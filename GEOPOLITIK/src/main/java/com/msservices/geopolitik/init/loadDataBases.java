package com.msservices.geopolitik.init;

import com.msservices.geopolitik.connection.DatabaseConnection;

public class loadDataBases {

    public static void load() {
        DatabaseConnection.init();
    }
}

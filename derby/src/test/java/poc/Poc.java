package poc;

import com.trganda.server.SlaveServer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Poc {

    private static SlaveServer server;

    @BeforeAll
    public static void startSlave() {
        try {
            DriverManager.getConnection("jdbc:derby:db;create=true");
        } catch (SQLException e) {
            // ttk...
        }
        server = new SlaveServer(4851);
    }

    @AfterAll
    public static void stopServer() {
        server.close();
    }

    @Test
    public void derby() throws SQLException {
        DriverManager.getConnection("jdbc:derby:db;startMaster=true;slaveHost=127.0.0.1");
    }
}

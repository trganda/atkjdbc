package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Server implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private final HttpServer httpServer;

    public Server(int port) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        this.httpServer.createContext("/", new ExtensionHandler());
        this.httpServer.start();
    }

    private static class ExtensionHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange resp) throws IOException {

            String path = resp.getRequestURI().getPath().substring(1);

            System.out.println("[+] Requested path: " + path);

            InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
            if (is == null) {
                resp.sendResponseHeaders(404, -1);
                return;
            }
            byte[] bytes = new byte[is.available()];
            is.read(bytes);

            resp.getResponseHeaders().add("Content-Type:", "application/octet-stream");
            resp.sendResponseHeaders(200, bytes.length);

            OutputStream out = resp.getResponseBody();
            out.write(bytes);
            out.flush();
            out.close();
        }
    }

    @Override
    public void close() {
        this.httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {
        new Server(8001);
    }
}

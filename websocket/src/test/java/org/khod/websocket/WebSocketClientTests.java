package org.khod.websocket;

import io.netty.buffer.ByteBuf;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class WebSocketClientTests {
    private TestWebSocketServer server;
    private Queue<ByteBuf> queue;

    private final static String hostname = "127.0.0.1";
    private final static int port = 9001;

    @BeforeEach
    void setup() {
        queue = new ArrayDeque<>();
        server = new TestWebSocketServer(new InetSocketAddress(hostname, port));
        server.start();

        Awaitility.waitAtMost(5, TimeUnit.SECONDS).until(() -> server.isStarted());
    }

    @AfterEach
    void teardown() throws Exception {
        server.stop();
    }

    @Test
    void testClientReceivesMessage() throws Exception {
        WebSocketClient client = new WebSocketClient(queue, "ws://%s:%d".formatted(hostname, port));
        client.connect();

        final String message = "hello";
        server.broadcast(message);

        Awaitility.waitAtMost(1, TimeUnit.SECONDS).until(() -> !queue.isEmpty());
        final ByteBuf polledMessage = queue.poll();
        Assertions.assertTrue(queue.isEmpty());
        Assertions.assertNotNull(polledMessage);
        Assertions.assertEquals(message, polledMessage.readString(message.length(), StandardCharsets.US_ASCII));
    }

    @Test
    void testClientDoesntProcessEmptyMessage() throws Exception {
        WebSocketClient client = new WebSocketClient(queue, "ws://%s:%d".formatted(hostname, port));
        client.connect();

        final String message = "";
        server.broadcast(message);

        Awaitility.await().atLeast(1, TimeUnit.SECONDS);
        Assertions.assertTrue(queue.isEmpty());
    }

    @Test
    void testBufferIsRetained() throws Exception {
        WebSocketClient client = new WebSocketClient(queue, "ws://%s:%d".formatted(hostname, port));
        client.connect();

        final String message = "hello";
        server.broadcast(message);

        Awaitility.waitAtMost(1, TimeUnit.SECONDS).until(() -> !queue.isEmpty());
        final ByteBuf polledMessage = queue.poll();
        Assertions.assertTrue(queue.isEmpty());
        Assertions.assertNotNull(polledMessage);
        Assertions.assertEquals(1, polledMessage.refCnt());
    }
}

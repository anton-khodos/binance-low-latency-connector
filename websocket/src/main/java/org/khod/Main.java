package org.khod;

import io.netty.buffer.ByteBuf;
import org.jctools.queues.SpscArrayQueue;
import org.khod.websocket.DummyPrinterQueueConsumer;
import org.khod.websocket.WebSocketClient;

import javax.net.ssl.SSLException;

public class Main {
        public static void main(String[] args)
                throws SSLException, InterruptedException {
                SpscArrayQueue<ByteBuf> queue = new SpscArrayQueue<>(4096);
                new Thread(new DummyPrinterQueueConsumer(queue), "consumer").start();
                WebSocketClient client = new WebSocketClient(queue, "ws://127.0.0.1:8080/ws");
                client.connect();
        }
}
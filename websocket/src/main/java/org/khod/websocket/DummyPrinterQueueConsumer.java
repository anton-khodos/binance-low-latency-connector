package org.khod.websocket;

import io.netty.buffer.ByteBuf;
import org.jctools.queues.SpscArrayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyPrinterQueueConsumer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DummyPrinterQueueConsumer.class);

    private final SpscArrayQueue<ByteBuf> queue;

    public DummyPrinterQueueConsumer(final SpscArrayQueue<ByteBuf> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            ByteBuf buffer = queue.poll();

            if (buffer != null) {
                // print content
                String json = buffer.toString(io.netty.util.CharsetUtil.UTF_8);
                logger.atInfo().log(json);
                buffer.release();
            }
        }
    }
}

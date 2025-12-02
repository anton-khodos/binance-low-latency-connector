package org.khod.websocket;

import io.netty.buffer.ByteBuf;
import org.jctools.queues.SpscArrayQueue;

public class DummyPrinterQueueConsumer implements Runnable {

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
                System.out.println(json);

                // release direct ByteBuf
                buffer.release();
            }
        }
    }
}

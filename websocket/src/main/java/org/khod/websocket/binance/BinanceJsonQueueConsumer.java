package org.khod.websocket.binance;

import io.netty.buffer.ByteBuf;
import org.jctools.queues.SpscArrayQueue;
import org.khod.data.ILifetimeObject;
import org.khod.json.BinanceJsonParser;
import org.khod.pojo.item.DefaultPojoItem;
import org.khod.websocket.IWebSocketCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public class BinanceJsonQueueConsumer<T extends DefaultPojoItem> implements ILifetimeObject {
    private static final Logger logger = LoggerFactory.getLogger(BinanceWebSocketClient.class);

    private final SpscArrayQueue<ByteBuf> queue;
    private final IWebSocketCallback<T> callback;
    private final BinanceJsonParser<T> jsonParser;
    private final T flyweightItem;
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final Thread runnerThread;
    private int idle = 0;


    public BinanceJsonQueueConsumer(
            final SpscArrayQueue<ByteBuf> queue, final IWebSocketCallback<T> callback, final T flyweightItem) {
        this.queue = queue;
        this.callback = callback;
        this.flyweightItem = flyweightItem;
        jsonParser = new BinanceJsonParser<>();
        runnerThread = new Thread(this::run, "Thread-" + flyweightItem.getClass().getSimpleName() + "-consumer");
        runnerThread.setDaemon(true);
    }

    @Override
    public void start() {
        if(!started.compareAndExchange(false,true)) {
            runnerThread.start();
        } else {
            logger.atWarn().log("Failed to start already started BinanceJsonQueueConsumer");
        }
    }

    @Override
    public void stop() {
        if (started.compareAndExchange(true,false)) {
            logger.atWarn().log("Failed to stop already stopped BinanceJsonQueueConsumer");
        }
    }

    public void run() {
        while (started.get()) {
            ByteBuf buffer = queue.poll();
            if (buffer != null) {
                idle = 0;
                processBuffer(buffer);
            } else {
                if (++idle < 100) {
                    Thread.onSpinWait();
                } else {
                    LockSupport.parkNanos(1_000);
                }
            }
        }
    }

    private void processBuffer(final ByteBuf buffer) {
        try {
            jsonParser.parsePojo(buffer, flyweightItem);
            callback.onMessage(flyweightItem);
        } catch (Exception e) {
            logger.atError().setCause(e).log("Failed to process message.");
        } finally {
            buffer.release();
        }
    }
}

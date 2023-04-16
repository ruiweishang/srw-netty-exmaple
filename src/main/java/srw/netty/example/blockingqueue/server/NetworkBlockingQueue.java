package srw.netty.example.blockingqueue.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author shangruiwei
 * @date 2023/4/16 19:26
 */
public class NetworkBlockingQueue {

    public static final NetworkBlockingQueue INSTANCE = new NetworkBlockingQueue();

    // 本地创建一个无上限的阻塞队列
    private static final BlockingQueue<String> localQueue = new LinkedBlockingQueue<>();


    public boolean offer(String item) {
        // 添加元素
        return localQueue.offer(item);
    }

    public String poll() {
        return localQueue.poll();
    }

    private void pushSuccessHandle(String item) {
        // 通知等待的channel

    }
}

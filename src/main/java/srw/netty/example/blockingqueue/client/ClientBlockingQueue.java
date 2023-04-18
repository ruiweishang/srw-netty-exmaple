package srw.netty.example.blockingqueue.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.*;
import srw.netty.example.blockingqueue.enums.CommandEnum;
import srw.netty.example.blockingqueue.model.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shangruiwei
 * @date 2023/4/18 19:01
 */
public class ClientBlockingQueue {

    private Channel ch;

    private static volatile List<Promise<String>> pollPromiseList = new ArrayList<>();

    public ClientBlockingQueue(Channel ch) {
        this.ch = ch;
    }

    public boolean offer(String element) {
        Command command = new Command(CommandEnum.OFFER.getCode(), element);
        ChannelFuture future = ch.writeAndFlush(command);
        future.addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    System.err.print("write failed: ");
                    future.cause().printStackTrace(System.err);
                }
            }
        });
        try {
            future.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Promise<String> poll() {
        ch.writeAndFlush(new Command(CommandEnum.POLL.getCode(), ""));
        Promise<String> promise = new DefaultPromise<>(ch.eventLoop());
        pollPromiseList.add(promise);
        return promise;
    }

    public static void notifyPoll(String element) {
        List<Promise<String>> list = null;
        synchronized (ClientBlockingQueue.class) {
            list = pollPromiseList;
            pollPromiseList = new ArrayList<>();
        }
        if (list != null && list.size() > 0) {
            for (Promise<String> promise : list) {
                promise.trySuccess(element);
            }
        }
    }
}

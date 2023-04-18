package srw.netty.example.blockingqueue.client;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import srw.netty.example.blockingqueue.model.Command;
import srw.netty.example.blockingqueue.model.Result;

/**
 * @author shangruiwei
 * @date 2023/4/16 20:14
 */
public class ClientHandler extends ChannelDuplexHandler {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Result r = (Result) msg;
        if (r.getCode().equals(0)) {
            // 通知阻塞的
            ClientBlockingQueue.notifyPoll(r.getContent());
        }
    }
}

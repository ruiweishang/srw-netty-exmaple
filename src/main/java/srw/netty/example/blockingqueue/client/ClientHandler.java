package srw.netty.example.blockingqueue.client;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import srw.netty.example.blockingqueue.model.Command;
import srw.netty.example.blockingqueue.model.Result;

/**
 * @author shangruiwei
 * @date 2023/4/16 20:14
 */
public class ClientHandler extends ChannelDuplexHandler {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        int i = ((String) msg).indexOf(" ");
        if (i > 0) {
            Command command = new Command(((String) msg).substring(0, i), ((String) msg).substring(i + 1));
            ctx.write(command, promise);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Result r = (Result) msg;
        if (r.getCode().equals(0)) {
            System.out.println(r.getContent());
        } else if (r.getCode().equals(-1)) {
            // 重新获取一次
            System.out.println("请重新获取一次");
        } else if (r.getCode().equals(-2)) {
            System.out.println("等待数据返回...");
        }
    }
}

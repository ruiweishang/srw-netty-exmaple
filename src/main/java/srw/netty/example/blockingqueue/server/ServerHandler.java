package srw.netty.example.blockingqueue.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import srw.netty.example.blockingqueue.model.Command;

/**
 * @author shangruiwei
 * @date 2023/4/16 19:27
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final CommandHandler commandHandler = CommandHandler.INSTANCE;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Command command = (Command) msg;
        ctx.writeAndFlush(commandHandler.invoke(ctx, command));
    }
}

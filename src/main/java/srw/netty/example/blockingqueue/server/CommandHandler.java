package srw.netty.example.blockingqueue.server;

import io.netty.channel.ChannelHandlerContext;
import srw.netty.example.blockingqueue.model.Command;
import srw.netty.example.blockingqueue.model.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shangruiwei
 * @date 2023/4/16 19:34
 */
public class CommandHandler {

    public static final CommandHandler INSTANCE = new CommandHandler();

    private NetworkBlockingQueue networkBlockingQueue = NetworkBlockingQueue.INSTANCE;

    private volatile List<ChannelHandlerContext> pollAwaitCtx = new ArrayList<>();

    public Result invoke(ChannelHandlerContext ctx, Command command) {
        Integer code = 0;
        String content = null;

        try {
            String cmd = command != null ? command.getCommand() : null;
            if (cmd != null) {
                switch (cmd) {
                    case "offer":
                        this.offer(command.getContent());
                        break;
                    case "poll":
                        String item = networkBlockingQueue.poll();
                        if (item == null) {
                            // 没有数据，不回写channel
                            synchronized (this) {
                                code = -2;
                                content = "等待数据";
                                pollAwaitCtx.add(ctx);
                            }
                        } else {
                            // 有数据，立即返回
                            content = item;
                        }
                        break;
                    default:
                        code = 110;
                        content = "未知命令";
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            code = 100;
            content = "系统失败";
        }

        return new Result(code, content);
    }

    private void offer(String item) {
        if (networkBlockingQueue.offer(item)) {
            // 如果投递成功，通知pollAwaitCtx，向channel写数据
            List<ChannelHandlerContext> tempPollAwaitCtx = null;
            synchronized (this) {
                tempPollAwaitCtx = pollAwaitCtx;
                pollAwaitCtx = new ArrayList<>();
            }
            if (tempPollAwaitCtx.size() > 0) {
                for (ChannelHandlerContext awaitCtx : tempPollAwaitCtx) {
                    awaitCtx.channel().writeAndFlush(new Result(-1, null));
                }
            }
        }
    }
}

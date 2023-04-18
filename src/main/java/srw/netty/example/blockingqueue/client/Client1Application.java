package srw.netty.example.blockingqueue.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import srw.netty.example.blockingqueue.enums.CommandEnum;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author shangruiwei
 * @date 2023/4/16 19:18
 */
public class Client1Application {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8002"));

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();

                            p.addLast(
                                    new LoggingHandler(),
                                    new ObjectEncoder(),
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    new ClientHandler());
                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect(HOST, PORT).sync();

            // channel
            Channel ch = f.channel();
            System.out.println("Enter commands (quit to end)");

            ClientBlockingQueue clientBlockingQueue = new ClientBlockingQueue(ch);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (; ; ) {
                final String input = in.readLine();
                final String line = input != null ? input.trim() : null;
                if (line == null || "quit".equalsIgnoreCase(line)) { // EOF or "quit"
                    ch.close().sync();
                    break;
                } else if (line.isEmpty()) { // skip `enter` or `enter` with spaces.
                    continue;
                }

                String command = "";
                String element = "";
                int i = line.indexOf(' ');
                if (i > 0) {
                    command = line.substring(0, i);
                    element = line.substring(i + 1);
                } else {
                    command = line;
                }
                CommandEnum commandEnum = CommandEnum.getByCode(command);
                if (commandEnum == null) {
                    System.out.println("不支持此命令，请重新输入");
                    continue;
                }
                switch (commandEnum) {
                    case OFFER:
                        clientBlockingQueue.offer(element);
                        break;
                    case POLL:
                        Promise<String> promise = clientBlockingQueue.poll();
                        // 阻塞时poll
                        System.out.println(promise.sync().get());
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }
}

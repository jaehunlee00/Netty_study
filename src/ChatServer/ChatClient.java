package ChatServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient {
    private static final int ListenPort = 8888;
    static final boolean SSL = System.getProperty("ssl") != null;
    SslContext sslCtx;

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            if (SSL) {
                sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            } else {
                sslCtx = null;
            }
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatClientInitializer(sslCtx));

            Channel channel = b.connect("localhost", ListenPort).sync().channel();
            ChannelFuture lastWriteFuture = null;

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            for (;;) {
                String line = in.readLine();
                if (line == null) { break; }

                lastWriteFuture = channel.writeAndFlush(line + "\r\n");
                if ("exit".equalsIgnoreCase(line)) {
                    channel.closeFuture().sync(); // 서버에서 채널 닫을 때까지 기다림
                    break;
                }
            }

            if (lastWriteFuture != null) {
                lastWriteFuture.sync(); // 채널 닫기 전에 모든 메세지가 flush 될 때까지 기다림
            }

        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new ChatClient().start();
    }
}

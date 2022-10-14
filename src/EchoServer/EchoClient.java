package EchoServer;
// ver. Netty 4.1.68.Final
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
    private static final int PORT = 8888;
    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new EchoClientHandler());
                        }
                    });

            ChannelFuture f = b.connect("localhost", PORT).sync(); // connect는 비동기 입출력 메소드, 메소드 호출 결과로 ChannelFuture 반환, 이의 sync 메소드는 ChannelFuture 객체의 요청이 완료될 때까지 대기
            f.channel().closeFuture().sync();
        }
        finally {
            group.shutdownGracefully();
        }
    }
}

package TelnetServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TelnetServer {
    private static final int listenPort = 8888;

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new TelnetServerInitializer());

            b.bind(listenPort).sync().channel().closeFuture().sync();
            // 부트스트랩에 텔넷 서비스 포트 지정, 서버 소켓에 바인딩된 채널이 종료될 때까지 대기 설정
            // ChannelFuture 인터페이스의 sync 메소드는 지정한 Future 객체의 동작이 완료될 때까지 대기
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}

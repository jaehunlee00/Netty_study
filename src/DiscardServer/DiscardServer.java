package DiscardServer;
// ver. Netty 4.1.68.Final
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {
    private static final int PORT = 8888;

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // 클라이언트의 연결을 담당하는 부모 스레드 그룹
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 클라이언트와의 입출력을 담당하는 자식 스레드 그룹, 수치 설정 안하면 CPU 코어 수에 2를 곱한 값

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup) // 스레드 그룹 초기화
                    .channel(NioServerSocketChannel.class) // 채널 초기화
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 소켓 채널에서 발생하는 이벤트 처리
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline(); // 채널 파이프라인 객체 생성
                            p.addLast(new DiscardServerHandler()); // 파이프라인에 핸들러 등록
                        }
                    });

            ChannelFuture f = b.bind(PORT).sync();
            f.channel().closeFuture().sync();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

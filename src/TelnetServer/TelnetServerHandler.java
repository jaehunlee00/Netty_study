package TelnetServer;
// ver. Netty 4.1.68.Final
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import java.net.InetAddress;
import java.util.Date;

@Sharable // 공유가능 상태 표시, 지정된 클래스를 채널 파이프라인에서 공유할 수 있음 (스레드 경합 없이 참조 가능)
public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {
    public void channelActive(ChannelHandlerContext ctx) throws  Exception {
        ctx.write("Hi, " + InetAddress.getLocalHost().getHostName() + "\r\n");
        ctx.write("Current Time: " + new Date() + "\r\n");
        ctx.flush();
    }

    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        boolean close = false;
        String response;

        if (request.isEmpty()) {
            response = "Give me a command.\r\n";
        } else if ("exit".equalsIgnoreCase(request)) {
            response = "Connection Closed\r\n";
            close = true;
        } else {
            response = "Your Command is " + request + "\r\n";
        }

        ChannelFuture future = ctx.write(response); // 생성된 메세지를 채널에 기록
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

    }

    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

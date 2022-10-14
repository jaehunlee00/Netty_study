package EchoServer;
// ver. Netty 4.1.68.Final
import java.nio.charset.Charset;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = ((ByteBuf) msg).toString(Charset.defaultCharset());
        System.out.println(message);
        ctx.write(msg); // 클라이언트가 서버에 보낸 메세지 그대로 소켓 채널에 올림
        // ctx는 CHC의 인터페이스 객체로 채널 파이프라인에 대한 이벤트를 처리

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception { // channelRead의 이벤트가 처리 완료된 후 자동으로 수행되는 이벤트 메소드
        ctx.flush(); // 메세지를 모두 읽으면 클라이언트에 전송
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

package EchoServer;
// ver. Netty 4.1.68.Final
import java.nio.charset.Charset;
import java.util.Scanner;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) { // channelActive 이벤트는 ChannelInboundHandler에 정의된 이벤트로 소켓 채널이 최초 활성화되었을 때 실행
        String sendMessage = new Scanner(System.in).nextLine();
        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(sendMessage.getBytes());

        String builder = "전송: " +
                sendMessage;

        System.out.println(builder);
        ctx.writeAndFlush(messageBuffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // 서버로부터 수신된 데이터가 있을 때 호출됨
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());

        String builder = "수신: " +
                readMessage;
        System.out.println(builder);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

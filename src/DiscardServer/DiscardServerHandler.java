package DiscardServer;
// ver. Netty 4.1.68.Final
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.Charset;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // 입력된 데이터를 처리하는 이벤트 핸들러
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = ((ByteBuf) msg).toString(Charset.defaultCharset()); // 소켓 채널에서 메세지 가져옴
        System.out.println(message);
    }

    // 이는 extends SimpleChannelInboundHandler를 사용한다면 channelRead0로 구현 가능한데
    // 요청받은 데이터의 유형이 애초에 지정한 Generic을 매개변수 유형으로 두고 있어 channelRead에서 진행되는 일련의 작업을 할 필요가 없음.
    // Simple... 클래스에서는 데이터가 수신되었을 때 호출되는 channelRead 이벤트에 대한 처리가 이미 구현되어 있음

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

package EchoServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class EchoServerHandler2 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("Channel Read: " + ((ByteBuf) msg).toString(Charset.defaultCharset()));
        ChannelFuture channelFuture = ctx.writeAndFlush(msg);
        // 수신된 데이터를 클라이언트 소켓 버퍼에 기록하고 버퍼의 데이터를 채널로 전송하는 비동기 메소드 writeAndFlush를 호출하고 ChannelFuture 객체를 받음
        //channelFuture.addListener(ChannelFutureListener.CLOSE);
        // ChannelFuture 객체에 채널을 종료하는 리스너를 등록, ChannelFuture 객체가 완료 이벤트를 수신할 때 수행됨.

        final int writeMessageSize = ((ByteBuf) msg).readableBytes();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                // operationComplete 메소드는 ChannelFuture 객체에서 발생하는 작업 완료 이벤트 메소드
                System.out.println("전송한 Bytes: " + writeMessageSize);
                channelFuture.channel().close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

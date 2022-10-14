package TelnetServer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TelnetServerInitializer extends ChannelInitializer<SocketChannel> {
    private static final StringDecoder DECODER = new StringDecoder();
    private static final StringEncoder ENCODER = new StringEncoder();
    private static final TelnetServerHandler SERVER_HANDLER = new TelnetServerHandler();
    // 연결되는 새로운 클라이언트 채널들이 동일한 인코더/디코더 객체를 공유 (모든 채널 파이프라인에서 공유됨)

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter())); // 구분자 기반의 패킷을 처리하는 기본 디코더
        pipeline.addLast(DECODER);
        pipeline.addLast(ENCODER);
        pipeline.addLast(SERVER_HANDLER);
    }
}

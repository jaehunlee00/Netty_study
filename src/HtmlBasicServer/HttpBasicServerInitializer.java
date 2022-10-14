package HtmlBasicServer;
// ver. Netty 4.1.68.Final
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

public class HttpBasicServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public HttpBasicServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new HttpServerCodec()); // 네티 기본 제공 HTTP 서버코덱, 인바운드와 아웃바운드 핸들러 모두 구현
        // HttpServerCodec의 생성자에서 HttpRequestDecoder와 HttpResposeEncoder를 모두 생성
        // 수신된 ByteBuf 객체를 HttpRequest와 HttpContent 객체로 변환하고 HttpRespose 객체를 ByteBuf로 인코딩하여 송신
        p.addLast(new HttpBasicServerHandler());
    }
}

package ChatServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetAddress;
import java.nio.charset.Charset;

@ChannelHandler.Sharable
public class ChatServerHandler extends ChannelInboundHandlerAdapter {
    private final ChannelGroup channels;
    public ChatServerHandler(ChannelGroup channels) {
        this.channels = channels;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        ctx.write("Welcome, " + InetAddress.getLocalHost().getHostName() + "\r\n");
        ctx.write("This is for test only.\r\n");
        ctx.flush();

        channels.writeAndFlush(InetAddress.getLocalHost().getHostName() + "Entered");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Handler Added Event");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;

        if ("exit".equalsIgnoreCase(message)) {
            ctx.close();
        }

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Handler Removed Event");
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

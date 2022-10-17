package ChatServer;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

@ChannelHandler.Sharable
public class ChatServerHandler extends ChannelInboundHandlerAdapter {
    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("New Channel is now ready-to-go.");
        Channel handling_channel = ctx.channel();

        for (Channel channel : channelGroup) {
            channel.writeAndFlush(handling_channel.remoteAddress() + "is entered.\r\n");
        }

        channelGroup.add(handling_channel);
        if (channelGroup.size() > 1) {
            handling_channel.writeAndFlush(channelGroup.size() + " clients are in this room.\r\n");
        } else {
            handling_channel.writeAndFlush(channelGroup.size() + " client is in this room.\r\n");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("New channel is now active.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;
        Channel handling_channel = ctx.channel();

        for (Channel channel : channelGroup) {
            if (channel != handling_channel) {
                channel.writeAndFlush("[" + handling_channel.remoteAddress() + "]: " + message + "\r\n");
            }
        }
        if ("exit".equalsIgnoreCase(message)) {
            ctx.close();
        }


    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Handler Removed Event");
        Channel handling_channel = ctx.channel();

        for (Channel channel : channelGroup) {
            channel.writeAndFlush(handling_channel.remoteAddress() + " is exited.\r\n");
        }

        ctx.writeAndFlush("Bye, " + handling_channel.remoteAddress() + "\r\n");
        channelGroup.remove(handling_channel);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

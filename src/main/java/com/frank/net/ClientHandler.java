package com.frank.net;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by zuowenbin on 17/8/27.
 */
@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private MessageHandler messageHandler;

    public ClientHandler(MessageHandler messageHandler){
        this.messageHandler = messageHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(messageHandler == null){
            return;
        }
        super.channelRead(ctx, msg);
    }
}

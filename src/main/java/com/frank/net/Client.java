package com.frank.net;

import com.frank.msg.MessageDecoder;
import com.frank.msg.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by zuowenbin on 17/8/27.
 */
public class Client {

    private EventLoopGroup eventLoopGroup;

    private Bootstrap bootstrap;

    private Channel channel;

    private String ip;

    private int port;

    public  Client(String ip, int port, MessageHandler messageHandler, Class<? extends MessageDecoder> messageDecoder){
        this.ip = ip;
        this.port = port;
        initNettyConnect(ip, port, messageHandler, messageDecoder);
    }

    private void initNettyConnect(String ip, int port, final MessageHandler messageHandler, final Class<? extends MessageDecoder> messageDecoder){
        try {
            eventLoopGroup = new NioEventLoopGroup(4);
            bootstrap = new Bootstrap().group(eventLoopGroup)
                    .channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("encoder", new MessageEncoder());
                            socketChannel.pipeline().addLast("decoder", messageDecoder.newInstance());
                            socketChannel.pipeline().addLast(new ClientHandler(messageHandler));
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            channel = channelFuture.channel();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void close(){
        if(channel != null){
            channel.close();
        }
        if(eventLoopGroup != null){
            eventLoopGroup.shutdownGracefully();
        }
    }

    public boolean isConnect(){
        if(channel != null){
            return channel.isActive();
        }
        return false;
    }

    public synchronized void reConnect(){
        if(isConnect()){
            return;
        }
        try{
            channel.close();
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            channel = channelFuture.channel();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}

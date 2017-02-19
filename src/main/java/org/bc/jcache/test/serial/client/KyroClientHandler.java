package org.bc.jcache.test.serial.client;

import org.bc.jcache.test.serial.CacheData;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class KyroClientHandler extends ChannelInboundHandlerAdapter{

    private ChannelHandlerContext ctx;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send the message to Server
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        // you can use the Object from Server here
    	CacheData resp = (CacheData)msg;
        System.out.println(resp.getData());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {

        cause.printStackTrace();
        ctx.close();
    }
    
    public void send(Object msg){
        ctx.writeAndFlush(msg);
    }
}

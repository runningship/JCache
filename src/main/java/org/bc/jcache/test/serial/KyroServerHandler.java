package org.bc.jcache.test.serial;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class KyroServerHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        CacheData data = (CacheData)msg;
        System.out.println("server receive msg:"+msg);

//        ctx.writeAndFlush(car);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("�µĿͻ�����������:"+ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("�ͻ��˶Ͽ�����:"+ctx.channel().remoteAddress());
    }
    
    
}

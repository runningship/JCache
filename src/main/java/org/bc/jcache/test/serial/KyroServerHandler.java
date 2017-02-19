package org.bc.jcache.test.serial;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class KyroServerHandler extends ChannelInboundHandlerAdapter{

	private Map<String,Object> store = new HashMap<String,Object>();
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        CacheData data = (CacheData)msg;
        if("put".equals(data.getCmd())){
        	store.put(data.getKey(),data.getData());
        	System.out.println("server store data:"+msg);
        }else if("get".equals(data.getCmd())){
        	CacheData result = new CacheData();
        	result.setData(store.get(data.getKey()));
        	result.setCmd("result");
        	result.setKey(data.getKey());
        	ctx.writeAndFlush(result);
        }else if("remove".equals(data.getCmd())){
        	store.remove(data.getKey());
        	CacheData result = new CacheData();
        	result.setData("removed");
        }
        
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
        System.out.println("收到客户端连接"+ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("客户端连接断开:"+ctx.channel().remoteAddress());
    }
    
    
}

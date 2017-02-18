package org.bc.jcache.test.serial.client;

import java.io.IOException;

import org.bc.jcache.test.serial.CacheData;
import org.bc.jcache.test.serial.KyroMsgDecoder;
import org.bc.jcache.test.serial.KyroMsgEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class KyroTransferClient {

    private String host;
    private int port;

    private ChannelFuture future;
    private KodoCLI cli;
    
    private KyroClientHandler clientHandler;
    
    public KyroTransferClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException, IOException {

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        clientHandler = new KyroClientHandler();
        try {
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new KyroMsgEncoder(),
                                    new KyroMsgDecoder(),
                                    clientHandler);
                        }
                    });

            future = bootstrap.connect(host, port).sync();
            cli = new KodoCLI();
            cli.start();
//            future.channel().closeFuture().sync();
//            future.channel().close();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public void put(String key , String content ,int timeout){
        CacheData data = new CacheData();
        data.setKey(key);
        data.setData(content);
        data.setCmd("put");
        data.setTimeout(timeout);
        clientHandler.send(data);
    }
    public void get(String key){
        CacheData data = new CacheData();
        data.setKey(key);
        data.setCmd("get");
        clientHandler.send(data);
    }
    
    public void stop(){
        future.channel().close();
    }
    public static void main(String[] args) throws Exception {

        CacheData message = new CacheData();
        message.setKey("test-car");
        message.setData("bmw".getBytes());
        new KyroTransferClient("192.168.23.240", 9527).start();
    }
}

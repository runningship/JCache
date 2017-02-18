package org.bc.jcache.test.serial;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class KyroMsgDecoder extends ByteToMessageDecoder {

    final int HEAD_LENGTH = 4;

    private Kryo kryo = new Kryo();
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < HEAD_LENGTH) {  //���HEAD_LENGTH���������ڱ�ʾͷ���ȵ��ֽ�����  ����Encoder�����Ǵ�����һ��int���͵�ֵ����������HEAD_LENGTH��ֵΪ4.
            return;
        }
        in.markReaderIndex();                  //���Ǳ��һ�µ�ǰ��readIndex��λ��
        int dataLength = in.readInt();       // ��ȡ���͹�������Ϣ�ĳ��ȡ�ByteBuf ��readInt()������������readIndex����4
        if (dataLength < 0) { // ���Ƕ�������Ϣ�峤��Ϊ0�����ǲ�Ӧ�ó��ֵ���������������������ر����ӡ�
            ctx.close();
        }

        if (in.readableBytes() < dataLength) { //��������Ϣ�峤�����С�����Ǵ��͹�������Ϣ���ȣ���resetReaderIndex. ������markReaderIndexʹ�õġ���readIndex���õ�mark�ĵط�
            in.resetReaderIndex();
            return;
        }

        byte[] body = new byte[dataLength];  //��������
        in.readBytes(body);
        Object o = convertToObject(body);  //��byte����ת��Ϊ������Ҫ�Ķ���
        out.add(o);
    }

    private Object convertToObject(byte[] body) {

        Input input = null;
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(body);
            input = new Input(bais);

            return kryo.readObject(input, CacheData.class);
        } catch (KryoException e) {
            e.printStackTrace();
        }finally{
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(bais);
        }

        return null;
    }
}

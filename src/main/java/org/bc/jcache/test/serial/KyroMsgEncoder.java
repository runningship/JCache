package org.bc.jcache.test.serial;

import java.io.ByteArrayOutputStream;

import org.apache.commons.io.IOUtils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Output;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class KyroMsgEncoder extends MessageToByteEncoder<CacheData>{

    private Kryo kryo = new Kryo();
    
    @Override
    protected void encode(ChannelHandlerContext ctx, CacheData msg, ByteBuf out) throws Exception {
        byte[] body = convertToBytes(msg);  //������ת��Ϊbyte
        int dataLength = body.length;  //��ȡ��Ϣ�ĳ���
        out.writeInt(dataLength);  //�Ƚ���Ϣ����д�룬Ҳ������Ϣͷ
        out.writeBytes(body);  //��Ϣ���а�������Ҫ���͵�����
    }

    private byte[] convertToBytes(CacheData car) {

        ByteArrayOutputStream bos = null;
        Output output = null;
        try {
            bos = new ByteArrayOutputStream();
            output = new Output(bos);
            kryo.writeObject(output, car);
            output.flush();

            return bos.toByteArray();
        } catch (KryoException e) {
            e.printStackTrace();
        }finally{
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(bos);
        }
        return null;
    }
}

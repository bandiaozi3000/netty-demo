package com.atguigu.netty.demo.netty.inandouthandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {

    /**
     * 加入接受的数据是abcdabcdabcdabcd,字符一个是1个字节,所以是16个字节,该handler会被调用两次
     * 注意:不是该handler被调用两次后才会进入下一个handler,而是该handler和下一个handler为一个整体被调用两次
     * 可以看出输出结果:
     *     MyByteToLongDecoder 被调用
     *     从客户端/127.0.0.1:4140读取到long7017280452178371428
     *     MyByteToLongDecoder 被调用
     *     从客户端/127.0.0.1:4140读取到long7017280452178371428
     * 由上述输出结果可得知结论没错.
     *
     * @param ctx 上下文对象
     * @param in 入站的BtyeBuf
     * @param out list集合,将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder 被调用");
        //因为Long是8个字节
        if(in.readableBytes()>=8){
            out.add(in.readLong());
        }
    }
}

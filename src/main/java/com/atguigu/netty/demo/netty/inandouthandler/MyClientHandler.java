package com.atguigu.netty.demo.netty.inandouthandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyClientHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("从服务端接收到的数据为:"+(Long)msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        ctx.writeAndFlush(123456L); //发送的是一个long

        //分析
        //1. "abcdabcdabcdabcd" 是 16个字节
        //2. 该处理器的前一个handler 是 MyLongToByteEncoder(虽说下一次调用是MyLongToByteEncoder,但是出站调用顺序是从尾到头,所以说该处理器下一个handler是MyLongToByteEncoder没问题)
        //3. MyLongToByteEncoder 父类  MessageToByteEncoder
        //4. 父类  MessageToByteEncoder
        /*
         public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf buf = null;
        try {
            if (acceptOutboundMessage(msg)) { //判断当前msg 是不是应该处理的类型，如果是就处理，不是就跳过encode
                @SuppressWarnings("unchecked")
                I cast = (I) msg;
                buf = allocateBuffer(ctx, cast, preferDirect);
                try {
                    encode(ctx, cast, buf);
                } finally {
                    ReferenceCountUtil.release(cast);
                }

                if (buf.isReadable()) {
                    ctx.write(buf, promise);
                } else {
                    buf.release();
                    ctx.write(Unpooled.EMPTY_BUFFER, promise);
                }
                buf = null;
            } else {
                ctx.write(msg, promise);
            }
        }
        4. 因此我们编写 Encoder 是要注意传入的数据类型和处理的数据类型一致
        */
        //因为传输的是abcdabcdabcdabce,不是long类型,所以不会经过MyLongToByteEncoder处理器,因为该处理器只处理long类型的.
//        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabce", CharsetUtil.UTF_8));
    }
}

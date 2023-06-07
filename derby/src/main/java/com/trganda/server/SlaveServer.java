package com.trganda.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class SlaveServer implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(SlaveServer.class);
    private final Channel channel;
    private final EventLoopGroup parentGroup;
    private final EventLoopGroup childGroup;

    public SlaveServer(int port) {
        parentGroup = new NioEventLoopGroup();
        childGroup = new NioEventLoopGroup();
        final ChannelFuture channelFuture =
                new ServerBootstrap()
                        .group(parentGroup, childGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(
                                new ChannelInitializer<NioSocketChannel>() {
                                    @Override
                                    protected void initChannel(NioSocketChannel ch)
                                            throws Exception {
                                        final ChannelPipeline pipeline = ch.pipeline();
                                        pipeline.addLast(new SlaveServerHandler());
                                    }
                                })
                        .bind(port);
        channel = channelFuture.channel();
        channelFuture.awaitUninterruptibly();
    }

    @Override
    public void close() {
        channel.close();
        childGroup.shutdownGracefully().awaitUninterruptibly();
        parentGroup.shutdownGracefully().awaitUninterruptibly();
    }

    private static class SlaveServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            logger.info("channelActive");
            try (InputStream stream =
                    SlaveServer.class.getClassLoader().getResourceAsStream("cc5.bin")) {
                assert stream != null;
                byte[] bytes = new byte[stream.available()];
                stream.read(bytes);

                ByteBuf buf = ctx.alloc().buffer(bytes.length);
                buf.writeBytes(bytes);

                ctx.writeAndFlush(buf);
                logger.info("response payload with cc5");
            } catch (Exception e) {
                // ttk...
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            logger.info("Server channel inactive");
        }
    }
}

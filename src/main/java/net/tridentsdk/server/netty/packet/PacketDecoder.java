/*
 * Trident - A Multithreaded Server Alternative
 * Copyright 2014 The TridentSDK Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.tridentsdk.server.netty.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import net.tridentsdk.server.TridentServer;
import net.tridentsdk.server.netty.ClientConnection;
import net.tridentsdk.server.netty.Codec;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Channel handler that decodes the packet data sent from the stream in the form of the byte buffer. This is needed to
 * interpret the data sent correctly, and make sure that the data maintains its transmission integrity. <p/> <p>Note
 * this is not shareable. It must be thread confined, or create a new instance for each channel.</p>
 *
 * @author The TridentSDK Team
 */
public class PacketDecoder extends ReplayingDecoder<Void> {

    private final Inflater inflater = new Inflater();
    private ClientConnection connection;
    private int rawLength;

    @Override
    public void handlerAdded(ChannelHandlerContext context) {
        this.connection = ClientConnection.getConnection(context);
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buf, List<Object> objects) throws Exception {
        boolean compressed = connection.isCompressionEnabled();
        this.rawLength = Codec.readVarInt32(buf);

        if (!(compressed) || rawLength < TridentServer.getInstance().getCompressionThreshold()) {
            ByteBuf data = buf.readBytes(this.rawLength);

            objects.add(new PacketData(data));
            return;
        }

        byte[] compressedData = new byte[buf.readableBytes()];
        byte[] decompressed = new byte[rawLength];

        buf.readBytes(compressedData);
        inflater.setInput(compressedData);

        inflater.inflate(decompressed);
        objects.add(new PacketData(Unpooled.wrappedBuffer(decompressed)));

        inflater.reset();
    }
}

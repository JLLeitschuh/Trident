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
package net.tridentsdk.impl.packets.play.in;

import io.netty.buffer.ByteBuf;
import net.tridentsdk.api.msg.MessageBuilder;
import net.tridentsdk.impl.packets.play.out.PacketPlayOutChatMessage;
import net.tridentsdk.impl.player.PlayerConnection;
import net.tridentsdk.impl.player.TridentPlayer;
import net.tridentsdk.impl.netty.ClientConnection;
import net.tridentsdk.impl.netty.Codec;
import net.tridentsdk.impl.netty.packet.InPacket;
import net.tridentsdk.impl.netty.packet.OutPacket;
import net.tridentsdk.impl.netty.packet.Packet;

public class PacketPlayInChat extends InPacket {

    /**
     * Message sent by the client, represented in JSON <p/> TODO: provide example
     */
    protected String message;

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    public Packet decode(ByteBuf buf) {
        this.message = Codec.readString(buf);

        return this;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void handleReceived(ClientConnection connection) {
        PlayerConnection pc = (PlayerConnection) connection;
        TridentPlayer player = pc.getPlayer();
        OutPacket packet = new PacketPlayOutChatMessage();

        packet.set("jsonMessage", new MessageBuilder(String
                .format("<%s> %s", player.getDisplayName(), this.message)));

        TridentPlayer.sendAll(packet);
    }
}
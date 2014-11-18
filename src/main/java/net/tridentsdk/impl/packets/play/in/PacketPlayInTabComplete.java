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
import net.tridentsdk.api.Location;
import net.tridentsdk.api.event.player.PlayerTabCompleteEvent;
import net.tridentsdk.impl.packets.play.out.PacketPlayOutTabComplete;
import net.tridentsdk.impl.player.PlayerConnection;
import net.tridentsdk.impl.netty.ClientConnection;
import net.tridentsdk.impl.netty.Codec;
import net.tridentsdk.impl.netty.packet.InPacket;
import net.tridentsdk.impl.netty.packet.Packet;

/**
 * Sent when the user presses tab while writing text. The payload contains all text behind the cursor.
 */
public class PacketPlayInTabComplete extends InPacket {

    /**
     * Text currently written
     */
    protected String text;
    /**
     * If player is looking at a specific block
     */
    protected boolean hasPosition;
    /**
     * Position of the block the player is looking at, only sent if hasPosition is true
     */
    protected Location lookedAtBlock;

    @Override
    public int getId() {
        return 0x14;
    }

    @Override
    public Packet decode(ByteBuf buf) {
        this.text = Codec.readString(buf);
        this.hasPosition = buf.readBoolean();

        if (this.hasPosition) {
            long encoded = buf.readLong();
            double x = (double) (encoded << 38);
            double y = (double) (encoded << 26 >> 52);
            double z = (double) (encoded << 38 >> 38);

            this.lookedAtBlock = new Location(null, x, y, z);
        }

        return this;
    }

    public String getText() {
        return this.text;
    }

    public boolean isHasPosition() {
        return this.hasPosition;
    }

    public Location getLookedAtBlock() {
        return this.lookedAtBlock;
    }

    @Override
    public void handleReceived(ClientConnection connection) {
        PlayerTabCompleteEvent event = new PlayerTabCompleteEvent(
                ((PlayerConnection) connection).getPlayer(), this.text);

        if (event.getSuggestions().length > 0) {
            connection.sendPacket(new PacketPlayOutTabComplete().set("matches", event.getSuggestions()));
        }
    }
}

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
import net.tridentsdk.api.util.Vector;
import net.tridentsdk.impl.netty.ClientConnection;
import net.tridentsdk.impl.netty.packet.InPacket;
import net.tridentsdk.impl.netty.packet.Packet;

public class PacketPlayInBlockPlace extends InPacket {

    /**
     * Location of the block being placed
     */
    protected Location location;
    protected byte direction; // wat
    /**
     * Position of the cursor, incorrect use of a Vector xD
     */
    protected Vector cursorPosition;

    @Override
    public int getId() {
        return 0x08;
    }

    @Override
    public Packet decode(ByteBuf buf) {
        long encodedLocation = buf.readLong();

        this.location = new Location(null, (double) (encodedLocation >> 38), (double) (encodedLocation << 26 >> 52),
                (double) (encodedLocation << 38 >> 38));
        this.direction = buf.readByte();

        // ignore held item
        for (int i = 0; i < buf.readableBytes() - 3; i++) {
            buf.readByte();
        }

        double x = (double) buf.readByte();
        double y = (double) buf.readByte();
        double z = (double) buf.readByte();

        this.cursorPosition = new Vector(x, y, z);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public byte getDirection() {
        return this.direction;
    }

    public Vector getCursorPosition() {
        return this.cursorPosition;
    }

    @Override
    public void handleReceived(ClientConnection connection) {
        // TODO: Act accordingly
    }
}

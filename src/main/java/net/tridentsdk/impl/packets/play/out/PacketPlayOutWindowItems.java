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
package net.tridentsdk.impl.packets.play.out;

import io.netty.buffer.ByteBuf;
import net.tridentsdk.impl.data.Slot;
import net.tridentsdk.impl.netty.packet.OutPacket;

public class PacketPlayOutWindowItems extends OutPacket {

    protected int windowId;
    protected Slot[] slots;

    @Override
    public int getId() {
        return 0x30;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public Slot[] getSlots() {
        return this.slots;
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeByte(this.windowId);
        buf.writeShort(this.slots.length);

        for (Slot s : this.slots) {
            s.write(buf);
        }
    }
}

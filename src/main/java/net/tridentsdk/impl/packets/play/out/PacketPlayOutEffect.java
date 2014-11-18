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
import net.tridentsdk.api.Location;
import net.tridentsdk.impl.data.Position;
import net.tridentsdk.impl.netty.packet.OutPacket;

public class PacketPlayOutEffect extends OutPacket {

    protected int effectId;
    protected Location loc;
    protected int data;
    protected boolean playSound;

    @Override
    public int getId() {
        return 0x28;
    }

    public int getEffectId() {
        return this.effectId;
    }

    public Location getLoc() {
        return this.loc;
    }

    public int getData() {
        return this.data;
    }

    public boolean isPlaySound() {
        return this.playSound;
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(this.effectId);

        new Position(this.loc).write(buf);

        buf.writeInt(this.data);
        buf.writeBoolean(this.playSound);
    }
}
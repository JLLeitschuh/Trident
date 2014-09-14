/*
 * Copyright (c) 2014, The TridentSDK Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     1. Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *     2. Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *     3. Neither the name of the The TridentSDK Team nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL The TridentSDK Team BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.tridentsdk.packets.play.out;

import io.netty.buffer.ByteBuf;
import net.tridentsdk.api.board.TagVisibility;
import net.tridentsdk.server.netty.Codec;
import net.tridentsdk.server.netty.packet.OutPacket;

public class PacketPlayOutTeams extends OutPacket {

    private String teamName;
    private Mode mode;

    private String teamDisplay;
    private String teamPrefix;
    private String teamSuffix;

    private short friendlyFire;
    private TagVisibility tagVisibility;
    private short color;

    private String[] players;

    @Override
    public int getId() {
        return 0x3E;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public Mode getMode() {
        return this.mode;
    }

    public String getTeamDisplay() {
        return this.teamDisplay;
    }

    public String getTeamPrefix() {
        return this.teamPrefix;
    }

    public String getTeamSuffix() {
        return this.teamSuffix;
    }

    public short getFriendlyFire() {
        return this.friendlyFire;
    }

    public TagVisibility getTagVisibility() {
        return this.tagVisibility;
    }

    public short getColor() {
        return this.color;
    }

    public String[] getPlayers() {
        return this.players;
    }

    @Override
    public void encode(ByteBuf buf) {
        Codec.writeString(buf, this.teamName);
        buf.writeByte(this.mode.toByte());

        if (mode.b == 1 || mode.b == 2) {
            Codec.writeString(buf, this.teamDisplay);
            Codec.writeString(buf, this.teamPrefix);
            Codec.writeString(buf, this.teamSuffix);

            buf.writeByte((int) this.friendlyFire);
            Codec.writeString(buf, tagVisibility.toString());
            buf.writeByte((int) this.color);
        }

        if (mode.b == 3 || mode.b == 4) {
            Codec.writeVarInt32(buf, this.players.length);

            for (String s : this.players) {
                Codec.writeString(buf, s);
            }
        }
    }

    public enum Mode {

        CREATED(0),
        REMOVED(1),
        UPDATED(2),
        ADD_PLAYER(3),
        REMOVE_PLAYER(4);

        private final byte b;

        Mode(int i) {
            this.b = (byte) i;
        }

        public byte toByte() {
            return b;
        }
    }
}
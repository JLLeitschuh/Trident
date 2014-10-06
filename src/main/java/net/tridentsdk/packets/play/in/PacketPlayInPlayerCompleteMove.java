/*
 * Copyright (c) 2014, TridentSDK Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of TridentSDK nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.tridentsdk.packets.play.in;

import io.netty.buffer.ByteBuf;
import net.tridentsdk.api.Location;
import net.tridentsdk.api.event.Cancellable;
import net.tridentsdk.api.event.player.PlayerMoveEvent;
import net.tridentsdk.packets.play.out.PacketPlayOutEntityTeleport;
import net.tridentsdk.player.PlayerConnection;
import net.tridentsdk.player.TridentPlayer;
import net.tridentsdk.server.netty.ClientConnection;
import net.tridentsdk.server.netty.packet.Packet;

/**
 * Packet sent when player moved both x, y, z and yaw, and pitch.
 */
public class PacketPlayInPlayerCompleteMove extends PacketPlayInPlayerMove {

    /**
     * New yaw of the client
     */
    protected float newYaw;
    /**
     * New pitch of the client
     */
    protected float newPitch;

    @Override
    public int getId() {
        return 0x06;
    }

    @Override
    public Packet decode(ByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();

        super.location = new Location(null, x, y, z);

        this.newYaw = buf.readFloat();
        this.newPitch = buf.readFloat();

        super.onGround = buf.readBoolean();
        return this;
    }

    @Override
    public void handleReceived(ClientConnection connection) {
        TridentPlayer player = ((PlayerConnection) connection).getPlayer();
        super.location.setWorld(player.getWorld());

        Cancellable event = new PlayerMoveEvent(player, player.getLocation(), super.location);

        if (event.isCancelled()) {
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();

            packet.set("entityId", player.getId());
            packet.set("location", player.getLocation());
            packet.set("onGround", player.isOnGround());

            connection.sendPacket(packet);
        }

        // process move
    }
}

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

package net.tridentsdk.packets.status;

import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import net.tridentsdk.server.netty.Codec;
import net.tridentsdk.server.netty.packet.OutPacket;
import net.tridentsdk.server.netty.packet.PacketType;

/**
 * Status response to PacketStatusInRequest
 *
 * @author The TridentSDK Team
 * @see net.tridentsdk.packets.status.PacketStatusInRequest
 */
public class PacketStatusOutResponse extends OutPacket {
    /**
     * The actual response, represented in JSON in the protocol
     */
    Response response;

    public PacketStatusOutResponse() {
        this.response = new Response();
    }

    @Override
    public int getId() {
        return 0x00;
    }

    public Response getResponse() {
        return this.response;
    }

    @Override
    public void encode(ByteBuf buf) {
        String json = new GsonBuilder().create().toJson(this.response);
        Codec.writeString(buf, json);
    }

    @Override
    public PacketType getType() {
        return PacketType.OUT;
    }

    public static class Response {
        /**
         * Version information
         */
        Version version = new Version();
        /**
         * Information regarding players
         */
        final Players players = new Players();
        /**
         * Description is the MOTD
         */
        final Description description = new Description();

        public static class Version {
            /**
             * Name of the version TODO make configurable
             */
            String name = "1.8";
            /**
             * Protocol version, 47 for 1.8
             */
            int protocol = 47;
        }

        public static class Players {
            /**
             * The slots of the server
             */
            int max = 10;
            /**
             * Amount of players online
             */
            int online = 5;
        }

        public static class Description {
            /**
             * MOTD
             */
            String text = "default blah blah this is never going to show";
        }
    }
}

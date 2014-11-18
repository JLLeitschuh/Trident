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
package net.tridentsdk.impl.netty.protocol;

import net.tridentsdk.api.docs.AccessNoDoc;
import net.tridentsdk.impl.packets.status.PacketStatusInPing;
import net.tridentsdk.impl.packets.status.PacketStatusInRequest;
import net.tridentsdk.impl.packets.status.PacketStatusOutPing;
import net.tridentsdk.impl.packets.status.PacketStatusOutResponse;

@AccessNoDoc
class Status extends PacketManager {
    Status() {
        this.inPackets.put(0x00, PacketStatusInRequest.class);
        this.inPackets.put(0x01, PacketStatusInPing.class);

        this.outPackets.put(0x00, PacketStatusOutResponse.class);
        this.outPackets.put(0x01, PacketStatusOutPing.class);
    }
}
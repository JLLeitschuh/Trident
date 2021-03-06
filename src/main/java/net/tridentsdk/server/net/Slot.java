/*
 * Trident - A Multithreaded Server Alternative
 * Copyright 2017 The TridentSDK Team
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
package net.tridentsdk.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.tridentsdk.base.Substance;
import net.tridentsdk.inventory.Item;
import net.tridentsdk.meta.ItemMeta;
import net.tridentsdk.meta.nbt.Tag;
import net.tridentsdk.server.inventory.TridentItem;

import javax.annotation.concurrent.Immutable;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * This class represents a protocol Slot object used to send
 * data pertaining to inventory items.
 */
@Immutable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Slot {
    /**
     * Represents a slot that contains no data
     */
    public static final Slot EMPTY = new Slot((short) -1, (byte) 0, (short) 0, new ItemMeta());

    // Self explanatory
    @Getter
    private final short id;
    @Getter
    private final byte count;
    @Getter
    private final short damage;
    @Getter
    private final ItemMeta meta;

    /**
     * Creates a new slot using the given byte buffer to
     * read encoded values of the slot.
     *
     * @param buf the buffer which to read
     * @return the slot
     */
    public static Slot read(ByteBuf buf) {
        short id = buf.readShort();
        if (id != -1) {
            byte count = buf.readByte();
            short dmg = buf.readShort();
            Tag.Compound nbt = Tag.decode(new DataInputStream(new ByteBufInputStream(buf)));

            if (id == Substance.AIR.getId()) {
                return EMPTY;
            } else {
                return new Slot(id, count, dmg, nbt == null ? new ItemMeta() : new ItemMeta(nbt));
            }
        } else {
            return EMPTY;
        }
    }

    /**
     * Creates a new slot using the information wrapped by
     * the given item.
     *
     * <p>This method automatically checks whether the item
     * is {@code null} and in which case, it returns
     * {@link #EMPTY}.</p>
     *
     * @param item the item which to send in slot format
     * @return the new slot
     */
    public static Slot newSlot(Item item) {
        if (item.isEmpty()) {
            return EMPTY;
        }

        return new Slot((short) item.getSubstance().getId(),
                (byte) item.getCount(),
                item.getDamage(), item.getMeta());
    }

    /**
     * Converts this slot to a tangible item.
     *
     * @return the item
     */
    public TridentItem toItem() {
        return new TridentItem(Substance.fromNumericId(this.id), this.count, (byte) this.damage, this.meta);
    }

    /**
     * Writes the slot data to the given byte buffer.
     *
     * @param buf the buffer which to write
     */
    public void write(ByteBuf buf) {
        buf.writeShort(this.id);

        if (this.id != -1) {
            buf.writeByte(this.count);
            buf.writeShort(this.damage);
            this.meta.writeNbt(new DataOutputStream(new ByteBufOutputStream(buf)));
        }
    }
}
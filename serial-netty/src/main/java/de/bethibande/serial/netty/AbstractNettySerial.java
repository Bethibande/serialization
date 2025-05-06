package de.bethibande.serial.netty;

import io.netty.buffer.ByteBuf;

public abstract class AbstractNettySerial {

    protected ByteBuf buffer;

    protected AbstractNettySerial() {
    }

    protected AbstractNettySerial(final ByteBuf buffer) {
        this.buffer = buffer;
    }

    public void setBuffer(final ByteBuf buffer) {
        this.buffer = buffer;
    }



}

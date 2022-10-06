package com.my.iplumber.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}

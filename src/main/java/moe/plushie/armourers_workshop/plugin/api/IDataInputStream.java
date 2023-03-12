package moe.plushie.armourers_workshop.plugin.api;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public interface IDataInputStream {

    static IDataInputStream of(DataInputStream stream) {
        return () -> stream;
    }

    DataInputStream getInputStream();

    default void readFully(byte[] b) throws IOException {
        getInputStream().readFully(b);
    }

    default void readFully(byte[] b, int off, int len) throws IOException {
        getInputStream().readFully(b, off, len);
    }

    default byte readByte() throws IOException {
        return getInputStream().readByte();
    }

    default boolean readBoolean() throws IOException {
        return getInputStream().readBoolean();
    }

    default short readShort() throws IOException {
        return getInputStream().readShort();
    }

    default int readInt() throws IOException {
        return getInputStream().readInt();
    }

    default long readLong() throws IOException {
        return getInputStream().readLong();
    }

    default double readDouble() throws IOException {
        return getInputStream().readDouble();
    }

    default String readString() throws IOException {
        int size = getInputStream().readUnsignedShort();
        return readString(size);
    }

    default String readString(int len) throws IOException {
        byte[] bytes = new byte[len];
        getInputStream().readFully(bytes, 0, len);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}

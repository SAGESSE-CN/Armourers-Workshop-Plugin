package moe.plushie.armourers_workshop.plugin.api;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public interface IDataOutputStream {

    static IDataOutputStream of(DataOutputStream stream) {
        return () -> stream;
    }

    DataOutputStream getOutputStream();

    default void write(byte[] bytes) throws IOException {
        getOutputStream().write(bytes);
    }

    default void write(byte[] b, int off, int len) throws IOException {
        getOutputStream().write(b, off, len);
    }

    default void writeByte(int v) throws IOException {
        getOutputStream().writeByte(v);
    }

    default void writeBoolean(boolean v) throws IOException {
        getOutputStream().writeBoolean(v);
    }

    default void writeShort(int v) throws IOException {
        getOutputStream().writeShort(v);
    }

    default void writeInt(int v) throws IOException {
        getOutputStream().writeInt(v);
    }

    default void writeLong(long v) throws IOException {
        getOutputStream().writeLong(v);
    }

    default void writeDouble(double v) throws IOException {
        getOutputStream().writeDouble(v);
    }

    default void writeString(String v) throws IOException {
        byte[] bytes = v.getBytes(StandardCharsets.UTF_8);
        int size = bytes.length;
        if (size > 65535) {
            throw new IOException("String is over the max length allowed.");
        }
        getOutputStream().writeShort((short) size);
        getOutputStream().write(bytes);
    }

    default void writeString(String v, int len) throws IOException {
        byte[] bytes = v.getBytes(StandardCharsets.UTF_8);
        getOutputStream().write(bytes, 0, len);
    }

}

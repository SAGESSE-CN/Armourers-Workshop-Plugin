package moe.plushie.armourers_workshop.core.skin.serialize;

import com.google.common.collect.Lists;
import moe.plushie.armourers_workshop.api.IDataInputStream;
import moe.plushie.armourers_workshop.api.skin.ISkinType;
import moe.plushie.armourers_workshop.core.skin.Skin;
import moe.plushie.armourers_workshop.core.skin.SkinProperties;
import moe.plushie.armourers_workshop.core.skin.SkinProperty;
import moe.plushie.armourers_workshop.core.skin.SkinTypes;
import moe.plushie.armourers_workshop.core.skin.exception.InvalidCubeTypeException;
import moe.plushie.armourers_workshop.core.skin.exception.NewerFileVersionException;
import moe.plushie.armourers_workshop.init.ModLog;
import moe.plushie.armourers_workshop.utils.SkinFileUtils;
import net.cocoonmc.core.math.Vector3i;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SkinSerializer {

    public static void writeToStream(Skin skin, DataOutputStream stream) throws IOException {
        stream.write(skin.getBytes());
    }

    public static Skin readSkinFromStream(DataInputStream stream) throws IOException, NewerFileVersionException, InvalidCubeTypeException {
        byte[] bytes = SkinFileUtils.readStreamToByteArray(stream);

        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(bytes));
        int fileVersion = inputStream.readInt();
        SkinFileHeader header = _readSkinInfoFromStream(bytes, fileVersion);
        return new Skin(header.getType(), header.getProperties(), bytes);
    }

    public static SkinFileHeader readSkinInfoFromStream(DataInputStream stream) throws IOException, NewerFileVersionException, InvalidCubeTypeException {
        byte[] bytes = SkinFileUtils.readStreamToByteArray(stream);

        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(bytes));
        int fileVersion = inputStream.readInt();
        return _readSkinInfoFromStream(bytes, fileVersion);
    }

    public static List<Vector3i> readSkinBlockFromSkin(Skin skin) {
        try {
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(skin.getBytes()));
            int fileVersion = inputStream.readInt();
            if (fileVersion >= 13) {
                return _readSkinBlockFromStream_v13(skin.getBytes(), fileVersion);
            }
            return _readSkinBlockFromStream_v12(skin.getBytes(), fileVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Lists.newArrayList();
    }

    private static SkinFileHeader _readSkinInfoFromStream(byte[] bytes, int fileVersion) throws IOException, NewerFileVersionException {
        if (fileVersion >= 13) {
            return _readSkinInfoFromStream_v13(bytes, fileVersion);
        }
        return _readSkinInfoFromStream_v12(bytes, fileVersion);
    }

    private static SkinFileHeader _readSkinInfoFromStream_v12(byte[] bytes, int fileVersion) throws IOException, NewerFileVersionException {
        IDataInputStream stream = null;
        stream = createStream(bytes, 0, bytes.length);
        SkinProperties properties = null;
        boolean loadedProps = true;
        IOException e = null;
        if (fileVersion < 12) {
            String authorName = stream.readString();
            String customName = stream.readString();
            String tags = "";
            if (!(fileVersion < 4)) {
                tags = stream.readString();
            }
            properties = SkinProperties.create();
            properties.put(SkinProperty.ALL_AUTHOR_NAME, authorName);
            properties.put(SkinProperty.ALL_CUSTOM_NAME, customName);
            if (!tags.equalsIgnoreCase("")) {
                properties.put(SkinProperty.ALL_KEY_TAGS, tags);
            }
        } else {
            try {
                properties = SkinProperties.create();
                properties.readFromStream(stream);
            } catch (IOException propE) {
                ModLog.error("prop load failed");
                e = propE;
                loadedProps = false;
                properties = SkinProperties.create();
            }
        }
        ISkinType skinType = null;

        if (fileVersion < 5) {
            if (loadedProps) {
                String regName = getTypeNameByLegacyId(stream.readByte() - 1);
                skinType = SkinTypes.byName(regName);
            } else {
                throw e;
            }
        } else {
            if (loadedProps) {
                String regName = stream.readString();
                skinType = SkinTypes.byName(regName);
            }
        }
        return new SkinFileHeader(fileVersion, skinType, properties);
    }

    private static SkinFileHeader _readSkinInfoFromStream_v13(byte[] bytes, int fileVersion) throws IOException, NewerFileVersionException {
        IDataInputStream stream = null;

        Range header1 = safeFindRange(bytes, "PROPS-START", 4);
        Range header2 = safeFindRange(bytes, "PROPS-END", header1.end);

        Range type1 = safeFindRange(bytes, "TYPE-START", header2.end);
        Range type2 = safeFindRange(bytes, "TYPE-END", type1.end);

        stream = createStream(bytes, header1.end, header2.start);
        SkinProperties properties = SkinProperties.create();
        properties.readFromStream(stream);

        stream = createStream(bytes, type1.end, type2.start);
        String regName = stream.readString();
        ISkinType skinType = SkinTypes.byName(regName);

        return new SkinFileHeader(fileVersion, skinType, properties);
    }


    private static String getTypeNameByLegacyId(int legacyId) {
        switch (legacyId) {
            case 0:
                return "armourers:head";
            case 1:
                return "armourers:chest";
            case 2:
                return "armourers:legs";
            case 3:
                return "armourers:skirt";
            case 4:
                return "armourers:feet";
            case 5:
                return "armourers:sword";
            case 6:
                return "armourers:bow";
            case 7:
                return "armourers:arrow";
            default:
                return null;
        }
    }

    private static List<Vector3i> _readSkinBlockFromStream_v12(byte[] bytes, int fileVersion) throws IOException, NewerFileVersionException {
        ArrayList<Vector3i> list = new ArrayList<>();
        return list;
    }

    private static List<Vector3i> _readSkinBlockFromStream_v13(byte[] bytes, int fileVersion) throws IOException, NewerFileVersionException {
        ArrayList<Vector3i> list = new ArrayList<>();
        int start = 4;
        while (true) {
            Range part1 = findRange(bytes, "PART-START", start);
            if (part1.start < 0) {
                break;
            }
            Range part2 = safeFindRange(bytes, "PART-END", part1.end);
            IDataInputStream stream = createStream(bytes, part1.end, part2.start);
            String regName = stream.readString();
            int size = stream.readInt();
            for (int i = 0; i < size; i++) {
                // id/x/y/z + r/g/b/t * 6
                int id = stream.readByte();
                int x = stream.readByte();
                int y = stream.readByte();
                int z = stream.readByte();
                for (int side = 0; side < 6; side++) {
                    int rgbt = stream.readInt();
                }
                list.add(new Vector3i(x, y, z));
            }
            start = part2.end;
        }
        return list;
    }

    private static Range safeFindRange(byte[] bytes, String key, int start) throws IOException {
        Range range = findRange(bytes, key, start);
        if (range.start == -1) {
            throw new IOException("not found section " + key);
        }
        return range;
    }

    private static Range findRange(byte[] bytes, String key, int start) throws IOException {
        int length = key.length();
        byte[] bytes1 = new byte[key.length() + 2];
        bytes1[0] = (byte) ((length >> 8) & 0xff);
        bytes1[1] = (byte) (length & 0xff);
        System.arraycopy(key.getBytes(StandardCharsets.UTF_8), 0, bytes1, 2, length);
        int index = indexOf(bytes, bytes1, start, bytes.length);
        return new Range(index, index + bytes1.length);
    }

    private static int indexOf(byte[] array, byte[] target, int start, int end) {
        if (target.length == 0) {
            return 0;
        }

        outer:
        for (int i = start; i < end - target.length + 1; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    private static IDataInputStream createStream(byte[] bytes, int start, int end) {
        return IDataInputStream.of(new DataInputStream(new ByteArrayInputStream(bytes, start, end)));
    }

    private static class Range {
        final int start;
        final int end;

        Range(int start, int end) {

            this.start = start;
            this.end = end;
        }
    }
}

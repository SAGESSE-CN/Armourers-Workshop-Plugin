package moe.plushie.armourers_workshop.utils;

import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.NbtIo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * because `commons.io` versions on some servers are too low,
 * so we can't directly reference `common.io` in the other source code.
 */
public class SkinFileUtils {

    /**
     * Normalizes a path, removing double and single dot path steps.
     */
    public static String normalize(final String filename) {
        return FilenameUtils.normalize(filename);
    }

    /**
     * Normalizes a path, removing double and single dot path steps.
     */
    public static String normalize(final String filename, final boolean unixSeparator) {
        return FilenameUtils.normalize(filename, unixSeparator);
    }

    /**
     * Normalizes a path, removing double and single dot path steps,
     * and removing any final directory separator.
     */
    public static String normalizeNoEndSeparator(final String filename, final boolean unixSeparator) {
        return FilenameUtils.normalizeNoEndSeparator(filename, unixSeparator);
    }

    /**
     * Concatenates a filename to a base path using normal command line style rules.
     */
    public static String concat(final String basePath, final String fullFilenameToAdd) {
        return FilenameUtils.concat(basePath, fullFilenameToAdd);
    }

    public static File[] listFiles(final File directory) {
        try {
            return directory.listFiles();
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Gets the base name, minus the full path and extension, from a full filename.
     */
    public static String getBaseName(final String filename) {
        return FilenameUtils.getBaseName(filename);
    }

    /**
     * Removes the extension from a filename.
     */
    public static String removeExtension(final String filename) {
        return FilenameUtils.removeExtension(filename);
    }

    /**
     * Gets the extension of a filename.
     */
    public static String getExtension(final String filename) {
        return FilenameUtils.getExtension(filename);
    }

    public static String getRelativePath(final String path, final String rootPath) {
        if (path.equals(rootPath)) {
            return "/";
        }
        if (path.startsWith(rootPath)) {
            return path.substring(rootPath.length());
        }
        return path;
    }

    public static String getRelativePath(final String path, final String rootPath, final boolean unixSeparator) {
        return normalize(getRelativePath(path, rootPath), unixSeparator);
    }

    public static String getRelativePath(final File path, final File rootPath) {
        return getRelativePath(path.getAbsolutePath(), rootPath.getAbsolutePath());
    }

    public static String getRelativePath(final File path, final File rootPath, final boolean unixSeparator) {
        return normalize(getRelativePath(path, rootPath), unixSeparator);
    }

    /**
     * Makes a directory, including any necessary but nonexistent parent
     * directories. If a file already exists with specified name but it is
     * not a directory then an IOException is thrown.
     * If the directory cannot be created (or does not already exist)
     * then an IOException is thrown.
     */
    public static void forceMkdir(final File directory) throws IOException {
        FileUtils.forceMkdir(directory);
    }

    /**
     * Makes any necessary but nonexistent parent directories for a given File. If the parent directory cannot be
     * created then an IOException is thrown.
     */
    public static void forceMkdirParent(final File file) throws IOException {
        final File parent = file.getParentFile();
        if (parent != null) {
            forceMkdir(parent);
        }
    }

    /**
     * Deletes a file, never throwing an exception. If file is a directory, delete it and all sub-directories.
     */
    public static boolean deleteQuietly(final File file) {
        return FileUtils.deleteQuietly(file);
    }

    /**
     * Reads the contents of a file into a byte array.
     * The file is always closed.
     */
    public static byte[] readFileToByteArray(final File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }

    public static byte[] readStreamToByteArray(final InputStream file) throws IOException {
        return IOUtils.toByteArray(file);
    }

//    /**
//     * Copies bytes from an InputStream source to a file destination. The directories up to destination will be created if they don't already exist. destination will be overwritten if it already exists.
//     * The source stream is closed.
//     */
//    public static void copyInputStreamToFile(final InputStream inputStream, final File destination) throws IOException {
//        forceMkdirParent(destination);
//        OutputStream outputStream = new FileOutputStream(destination);
//
//        int bytesRead;
//        byte[] buffer = new byte[8 * 1024];
//        while ((bytesRead = inputStream.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, bytesRead);
//        }
//        IOUtils.closeQuietly(inputStream);
//        IOUtils.closeQuietly(outputStream);
//    }

    public static void writeNBT(CompoundTag compoundTag, File file) throws IOException {
        NbtIo.write(compoundTag, Files.newOutputStream(file.toPath()));
    }

    public static CompoundTag readNBT(File file) throws IOException {
        if (file.exists()) {
            return NbtIo.read(Files.newInputStream(file.toPath()));
        }
        return null;
    }

    public static CompoundTag readNBT(String contents) {
        try {
            return CompoundTag.parseTag(contents);
        } catch (Exception e) {
            return CompoundTag.newInstance();
        }
    }
}

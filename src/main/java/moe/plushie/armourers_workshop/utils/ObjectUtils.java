package moe.plushie.armourers_workshop.utils;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ObjectUtils {

    public static <S, T> T unsafeCast(S src) {
        // noinspection unchecked
        return (T) src;
    }

    @Nullable
    public static <S, T> T safeCast(S src, Class<T> type) {
        if (type.isInstance(src)) {
            return type.cast(src);
        }
        return null;
    }

//    public static String replaceString(String string, NSRange range, String replacementString) {
//        return (new StringBuilder(string)).replace(range.startIndex(), range.endIndex(), replacementString).toString();
//    }

    public static <K, V> Map<K, V> toMap(K key, V value) {
        HashMap<K, V> map = new HashMap<>(1);
        map.put(key, value);
        return map;
    }

    public static <K, V> void difference(Map<K, V> oldValue, Map<K, V> newValue, BiConsumer<K, V> removeHandler, BiConsumer<K, V> insertHandler) {
        HashMap<K, V> insertEntities = new HashMap<>();
        HashMap<K, V> removedEntities = new HashMap<>(oldValue);
        newValue.forEach((key, value) -> {
            if (removedEntities.remove(key) == null) {
                insertEntities.put(key, value);
            }
        });
        removedEntities.forEach(removeHandler);
        insertEntities.forEach(insertHandler);
    }

    public static <K, V, R> V find(Map<K, V> map, R req, Function<K, R> resolver) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (req.equals(resolver.apply(entry.getKey()))) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static <S, T> ArrayList<T> compactMap(S[] in, Function<? super S, @Nullable T> transform) {
        ArrayList<T> results = new ArrayList<>(in.length);
        for (S value : in) {
            T res = transform.apply(value);
            if (res != null) {
                results.add(res);
            }
        }
        return results;
    }

    public static <S, T> ArrayList<T> compactMap(Collection<S> in, Function<? super S, @Nullable T> transform) {
        ArrayList<T> results = new ArrayList<>(in.size());
        for (S value : in) {
            T res = transform.apply(value);
            if (res != null) {
                results.add(res);
            }
        }
        return results;
    }

    @Nullable
    public static <S, T> T flatMap(@Nullable S src, Function<S, T> consumer) {
        if (src != null) {
            return consumer.apply(src);
        }
        return null;
    }

    public static <S> void ifPresent(@Nullable S src, Consumer<S> consumer) {
        if (src != null) {
            consumer.accept(src);
        }
    }

    @SafeVarargs
    public static <T> ArrayList<T> map(T... objects) {
        ArrayList<T> results = new ArrayList<>(objects.length);
        Collections.addAll(results, objects);
        return results;
    }


//    public static void set(IMatrix3f matrixIn, IMatrix3f matrixOut) {
//        FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
//        matrixIn.store(buffer);
//        matrixOut.load(buffer);
//    }
//
//    public static void set(IMatrix4f matrixIn, IMatrix4f matrixOut) {
//        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
//        matrixIn.store(buffer);
//        matrixOut.load(buffer);
//    }

    // "<%s: 0x%x; arg1 = arg2; ...; argN-1 = argN>"
    public static String makeDescription(Object obj, Object... arguments) {
        StringBuilder builder = new StringBuilder();
        builder.append("<");
        builder.append(getClassName(obj.getClass()));
        builder.append(": ");
        builder.append(String.format("0x%x", System.identityHashCode(obj)));
        for (int i = 0; i < arguments.length; i += 2) {
            if (isEmptyOrNull(arguments[i + 1])) {
                continue;
            }
            builder.append("; ");
            builder.append(arguments[i]);
            builder.append(" = ");
            builder.append(arguments[i + 1]);
        }
        builder.append(">");
        return builder.toString();
    }

    public static boolean isEmptyOrNull(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Collection<?>) {
            return ((Collection<?>) value).isEmpty();
        }
        if (value instanceof String) {
            return ((String) value).isEmpty();
        }
        return false;
    }

    public static String getClassName(Class<?> clazz) {
        String name = clazz.getTypeName();
        Package pkg = clazz.getPackage();
        if (pkg != null) {
            return name.replace(pkg.getName() + ".", "");
        }
        return clazz.getSimpleName();
    }
}


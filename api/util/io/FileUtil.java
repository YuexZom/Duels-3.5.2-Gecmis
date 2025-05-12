/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.realized.duels.util.io;

import java.io.File;
import java.io.IOException;

public final class FileUtil {
    public static boolean checkNonEmpty(File file, boolean create) throws IOException {
        if (!file.exists()) {
            if (create) {
                file.createNewFile();
            }
            return false;
        }
        return file.length() > 0L;
    }

    private FileUtil() {
    }
}


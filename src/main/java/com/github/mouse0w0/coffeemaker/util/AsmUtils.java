package com.github.mouse0w0.coffeemaker.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;

public class AsmUtils {
    public static byte[] rename(byte[] bytes, String oldName, String newName) {
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(0);
        ClassRemapper classRemapper = new ClassRemapper(cw, new SimpleRemapper(oldName, newName));
        cr.accept(classRemapper, 0);
        return cw.toByteArray();
    }
}

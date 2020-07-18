package com.github.mouse0w0.coffeemaker.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOUtils {

    public static ClassNode loadClassNode(Path file) throws IOException {
        try (InputStream inputStream = Files.newInputStream(file)) {
            ClassReader classReader = new ClassReader(inputStream);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            return classNode;
        }
    }
}

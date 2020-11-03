package com.github.mouse0w0.coffeemaker;

import com.github.mouse0w0.coffeemaker.extree.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.template.Template;
import com.github.mouse0w0.coffeemaker.template.TemplateParseException;
import com.github.mouse0w0.coffeemaker.template.TemplateParser;
import com.github.mouse0w0.coffeemaker.template.impl.TemplateParserImpl;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CoffeeMaker {

    private final TemplateParser templateParser = new TemplateParserImpl();

    private final Map<String, Template> templateMap = new HashMap<>();

    public TemplateParser getTemplateParser() {
        return templateParser;
    }

    public Template getTemplate(String name) {
        return templateMap.get(name);
    }

    public void loadTemplateFromJar(String url) throws IOException {
        try {
            loadTemplateFromJar(Paths.get(new URL(url).toURI()));
        } catch (URISyntaxException | MalformedURLException e) {
            throw new IllegalArgumentException("Cannot load template from url: " + url);
        }
    }

    public void loadTemplateFromJar(Path file) throws IOException {
        if (!Files.isRegularFile(file)) {
            throw new IllegalArgumentException("File is not a file");
        }

        FileSystem fileSystem = FileSystems.newFileSystem(file, Thread.currentThread().getContextClassLoader());
        for (Path rootDirectory : fileSystem.getRootDirectories()) {
            Iterator<Path> iterator = Files.walk(rootDirectory).iterator();
            while (iterator.hasNext()) {
                Path file1 = iterator.next();
                if (!Files.isRegularFile(file1)) continue;
                if (!file1.getFileName().toString().endsWith(".class")) continue;
                try (InputStream in = Files.newInputStream(file)) {
                    loadTemplate(in);
                }
            }
        }
    }

    public void loadTemplate(InputStream in) throws IOException {
        ClassNodeEx classNode = loadClassNode(in);
        try {
            Template template = templateParser.parse(classNode);
            templateMap.put(template.getName(), template);
        } catch (TemplateParseException ignored) {
        }
    }

    private static ClassNodeEx loadClassNode(InputStream in) throws IOException {
        ClassReader classReader = new ClassReader(in);
        ClassNodeEx classNode = new ClassNodeEx();
        classReader.accept(classNode, 0);
        return classNode;
    }
}

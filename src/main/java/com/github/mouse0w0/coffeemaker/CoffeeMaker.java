package com.github.mouse0w0.coffeemaker;

import com.github.mouse0w0.asm.extree.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.exception.IllegalTemplateException;
import com.github.mouse0w0.coffeemaker.impl.TemplateParserImpl;
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

    private TemplateParser templateParser = new TemplateParserImpl();

    private Map<String, Template> templateMap = new HashMap<>();
    private Map<String, Snippet> snippetMap = new HashMap<>();

    public TemplateParser getTemplateParser() {
        return templateParser;
    }

    public void setTemplateParser(TemplateParser templateParser) {
        this.templateParser = templateParser;
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
                ClassNodeEx classNode = loadClassNode(file1);
                try {
                    Template template = templateParser.parse(classNode);
                    templateMap.put(template.getName(), template);
                } catch (IllegalTemplateException ignored) {
                }
            }
        }
    }

    private static ClassNodeEx loadClassNode(Path file) throws IOException {
        try (InputStream inputStream = Files.newInputStream(file)) {
            ClassReader classReader = new ClassReader(inputStream);
            ClassNodeEx classNode = new ClassNodeEx();
            classReader.accept(classNode, 0);
            return classNode;
        }
    }

    public Template getTemplate(String name) {
        return templateMap.get(name);
    }
}

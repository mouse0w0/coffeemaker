package com.github.mouse0w0.coffeemaker;

import com.github.mouse0w0.coffeemaker.template.InvalidTemplateException;
import com.github.mouse0w0.coffeemaker.template.Template;
import com.github.mouse0w0.coffeemaker.template.TemplateParser;
import com.github.mouse0w0.coffeemaker.template.impl2.TemplateParserImpl;

import java.io.IOException;
import java.io.InputStream;
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

    public void loadTemplateFromJar(URL url) throws IOException {
        try {
            loadTemplateFromJar(Paths.get(url.toURI()));
        } catch (URISyntaxException e) {
            throw new IOException("Failed to convert url to uri, url:" + url);
        }
    }

    public void loadTemplateFromJar(String file) throws IOException {
        loadTemplateFromJar(Paths.get(file));
    }

    public void loadTemplateFromJar(Path file) throws IOException {
        if (!Files.isRegularFile(file)) {
            throw new IllegalArgumentException("The file is not a file");
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (FileSystem fileSystem = FileSystems.newFileSystem(file, classLoader)) {
            for (Path rootDirectory : fileSystem.getRootDirectories()) {
                Iterator<Path> iterator = Files.walk(rootDirectory).iterator();
                while (iterator.hasNext()) {
                    Path classFile = iterator.next();
                    if (!Files.isRegularFile(classFile)) continue;
                    if (!classFile.getFileName().toString().endsWith(".class")) continue;
                    try (InputStream in = Files.newInputStream(classFile)) {
                        loadTemplate(in);
                    } catch (InvalidTemplateException ignored) {
                    }
                }
            }
        }
    }

    public void loadTemplate(InputStream in) throws IOException {
        Template template = templateParser.parse(in);
        templateMap.put(template.getName(), template);
    }
}

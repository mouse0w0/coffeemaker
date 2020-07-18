package com.github.mouse0w0.coffeemaker;

import com.github.mouse0w0.coffeemaker.exception.IllegalTemplateException;
import com.github.mouse0w0.coffeemaker.impl.TemplateResolverImpl;
import com.github.mouse0w0.coffeemaker.util.IOUtils;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CoffeeMaker {

    private TemplateResolver templateResolver = new TemplateResolverImpl();

    private Map<String, Template> templateMap = new HashMap<>();
    private Map<String, Snippet> snippetMap = new HashMap<>();

    public TemplateResolver getTemplateResolver() {
        return templateResolver;
    }

    public void setTemplateResolver(TemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
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
                ClassNode classNode = IOUtils.loadClassNode(file1);
                try {
                    Template template = templateResolver.resolve(classNode);
                    templateMap.put(template.getName(), template);
                } catch (IllegalTemplateException ignored) {
                }
            }
        }
    }

    public Template getTemplate(String name) {
        return templateMap.get(name);
    }
}

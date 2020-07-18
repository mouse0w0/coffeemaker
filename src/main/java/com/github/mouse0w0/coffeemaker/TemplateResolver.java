package com.github.mouse0w0.coffeemaker;

import com.github.mouse0w0.coffeemaker.exception.IllegalTemplateException;
import com.github.mouse0w0.coffeemaker.exception.TemplateLoadException;
import org.objectweb.asm.tree.ClassNode;

public interface TemplateResolver {
    Template resolve(ClassNode classNode) throws IllegalTemplateException, TemplateLoadException;
}

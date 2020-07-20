package com.github.mouse0w0.coffeemaker;

import com.github.mouse0w0.coffeemaker.exception.IllegalTemplateException;
import com.github.mouse0w0.coffeemaker.exception.TemplateParseException;
import org.objectweb.asm.tree.ClassNode;

public interface TemplateParser {
    Template parse(ClassNode classNode) throws IllegalTemplateException, TemplateParseException;
}

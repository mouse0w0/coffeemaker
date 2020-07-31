package com.github.mouse0w0.coffeemaker;

import com.github.mouse0w0.asm.extree.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.exception.IllegalTemplateException;
import com.github.mouse0w0.coffeemaker.exception.TemplateParseException;

public interface TemplateParser {
    Template parse(ClassNodeEx classNode) throws IllegalTemplateException, TemplateParseException;
}

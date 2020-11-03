package com.github.mouse0w0.coffeemaker.template;

import com.github.mouse0w0.coffeemaker.extree.ClassNodeEx;

public interface TemplateParser {
    boolean isTemplate(ClassNodeEx classNode);

    Template parse(ClassNodeEx classNode) throws TemplateParseException;
}

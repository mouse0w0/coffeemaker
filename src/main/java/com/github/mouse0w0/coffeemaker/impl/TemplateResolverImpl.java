package com.github.mouse0w0.coffeemaker.impl;

import com.github.mouse0w0.coffeemaker.Template;
import com.github.mouse0w0.coffeemaker.TemplateResolver;
import com.github.mouse0w0.coffeemaker.exception.IllegalTemplateException;
import com.github.mouse0w0.coffeemaker.exception.TemplateLoadException;
import com.github.mouse0w0.coffeemaker.syntax.ATemplate;
import com.github.mouse0w0.coffeemaker.util.ASMUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

public class TemplateResolverImpl implements TemplateResolver {

    private static final Type TEMPLATE_ANNOTATION = Type.getType(ATemplate.class);

    @Override
    public Template resolve(ClassNode classNode) throws IllegalTemplateException, TemplateLoadException {
        AnnotationNode annoTemplateClass = ASMUtils.getInvisibleAnnotation(classNode, TEMPLATE_ANNOTATION);
        if (annoTemplateClass == null) {
            throw new IllegalTemplateException("Class " + classNode.name + "is not a template");
        }

        TemplateImpl template = new TemplateImpl(ASMUtils.getAnnotationValue(annoTemplateClass, "value"), classNode);

        return template;
    }
}

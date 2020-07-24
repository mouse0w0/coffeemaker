package com.github.mouse0w0.coffeemaker.impl;

import com.github.mouse0w0.coffeemaker.Processor;
import com.github.mouse0w0.coffeemaker.Template;
import com.github.mouse0w0.coffeemaker.TemplateParser;
import com.github.mouse0w0.coffeemaker.asm.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.exception.IllegalTemplateException;
import com.github.mouse0w0.coffeemaker.exception.TemplateParseException;
import com.github.mouse0w0.coffeemaker.impl.processor.ModifyAnnotationProcessor;
import com.github.mouse0w0.coffeemaker.syntax.ATemplate;
import com.github.mouse0w0.coffeemaker.util.ASMUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;

import java.util.ArrayList;
import java.util.List;

public class TemplateParserImpl implements TemplateParser {

    private static final Type TEMPLATE_ANNOTATION = Type.getType(ATemplate.class);

    private final List<Processor.Factory> processorFactories = new ArrayList<>();

    public TemplateParserImpl() {
        addProcessorFactory(ModifyAnnotationProcessor.factory());
    }

    public void addProcessorFactory(Processor.Factory factory) {
        processorFactories.add(factory);
    }

    @Override
    public Template parse(ClassNodeEx classNode) throws IllegalTemplateException, TemplateParseException {
        AnnotationNode annoTemplateClass = ASMUtils.getInvisibleAnnotation(classNode, TEMPLATE_ANNOTATION);
        if (annoTemplateClass == null) {
            throw new IllegalTemplateException("Class " + classNode.name + "is not a template");
        }

        TemplateImpl template = new TemplateImpl(ASMUtils.getAnnotationValue(annoTemplateClass, "value"), classNode);
        List<Processor> processors = template.getProcessors();
        for (Processor.Factory factory : processorFactories) {
            factory.create(classNode, processors);
        }
        return template;
    }
}

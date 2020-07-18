package com.github.mouse0w0.coffeemaker.impl;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.Template;
import com.github.mouse0w0.coffeemaker.asm.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.processor.Processor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.List;

public class TemplateImpl implements Template {
    private final String name;
    private final ClassNode classNode;

    private final List<Processor> processors = new ArrayList<>();

    public TemplateImpl(String name, ClassNode classNode) {
        this.name = name;
        this.classNode = classNode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte[] toByteArray(Evaluator evaluator) {
        ClassNodeEx classNodeEx = new ClassNodeEx();
        classNode.accept(classNodeEx);

        processors.forEach(processor -> processor.process(classNodeEx, evaluator));

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classNodeEx.accept(classWriter);
        return classWriter.toByteArray();
    }

    public List<Processor> getProcessors() {
        return processors;
    }
}

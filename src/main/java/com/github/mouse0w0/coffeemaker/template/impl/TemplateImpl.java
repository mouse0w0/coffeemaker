package com.github.mouse0w0.coffeemaker.template.impl;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.template.Template;
import com.github.mouse0w0.coffeemaker.template.impl.extree.ClassNodeEx;
import org.objectweb.asm.ClassWriter;

import java.util.ArrayList;
import java.util.List;

public class TemplateImpl implements Template {
    private final String name;
    private final ClassNodeEx classNode;

    private final List<Processor> processors = new ArrayList<>();

    public TemplateImpl(String name, ClassNodeEx classNode) {
        this.name = name;
        this.classNode = classNode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte[] process(Evaluator evaluator) {
        ClassNodeEx cn = new ClassNodeEx();
        classNode.accept(cn);

        for (Processor processor : processors) {
            cn = processor.process(cn, evaluator);
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cn.accept(classWriter);
        return classWriter.toByteArray();
    }

    public List<Processor> getProcessors() {
        return processors;
    }
}

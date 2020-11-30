package com.github.mouse0w0.coffeemaker.template.impl;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.Template;
import com.github.mouse0w0.coffeemaker.template.tree.BtClass;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;

public class TemplateImpl implements Template {
    private final String name;
    private final BtClass btClass;

    public TemplateImpl(String name, BtClass btClass) {
        this.name = name;
        this.btClass = btClass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte[] process(Evaluator evaluator) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        btClass.accept(cw, evaluator);
        return cw.toByteArray();
    }

    @Override
    public byte[] process(String newClassName, Evaluator evaluator) {
        ClassReader cr = new ClassReader(process(evaluator));
        ClassWriter cw = new ClassWriter(0);
        ClassRemapper classRemapper = new ClassRemapper(cw, new SimpleRemapper(name, newClassName));
        cr.accept(classRemapper, 0);
        return cw.toByteArray();
    }
}

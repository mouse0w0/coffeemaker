package com.github.mouse0w0.coffeemaker.template.impl2;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.Template;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtClass;
import org.objectweb.asm.ClassWriter;

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
}

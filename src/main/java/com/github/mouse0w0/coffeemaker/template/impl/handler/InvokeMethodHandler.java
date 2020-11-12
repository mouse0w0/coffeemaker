package com.github.mouse0w0.coffeemaker.template.impl.handler;

import com.github.mouse0w0.coffeemaker.template.impl.Processor;
import com.github.mouse0w0.coffeemaker.template.impl.extree.MethodNodeEx;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.List;

public interface InvokeMethodHandler {
    String[] getSupportedInvokeMethods();

    void handle(MethodNodeEx method, MethodInsnNode insn, List<Processor> processors);
}

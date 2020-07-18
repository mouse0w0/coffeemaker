package com.github.mouse0w0.coffeemaker.handler;

import com.github.mouse0w0.coffeemaker.asm.MethodIdentifier;
import com.github.mouse0w0.coffeemaker.processor.Processor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Set;

public interface InvokeMethodHandler {
    Set<MethodIdentifier> getSupportedMethods();

    void handle(MethodNode method, AbstractInsnNode insn, List<Processor> processors);
}

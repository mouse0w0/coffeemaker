package com.github.mouse0w0.coffeemaker.impl.handler;

import com.github.mouse0w0.asm.extree.MethodNodeEx;
import com.github.mouse0w0.coffeemaker.Processor;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.List;

public interface InvokeMethodHandler {

    String[] getSupportedInvokeMethods();

    void handle(MethodNodeEx method, MethodInsnNode insn, List<Processor> processors);
}

package com.github.mouse0w0.coffeemaker.impl;

import com.github.mouse0w0.asm.extree.AnnotationNodeEx;
import com.github.mouse0w0.asm.extree.ClassNodeEx;
import com.github.mouse0w0.asm.extree.FieldNodeEx;
import com.github.mouse0w0.asm.extree.MethodNodeEx;
import com.github.mouse0w0.coffeemaker.Processor;
import com.github.mouse0w0.coffeemaker.Template;
import com.github.mouse0w0.coffeemaker.TemplateParser;
import com.github.mouse0w0.coffeemaker.asm.ASMUtils;
import com.github.mouse0w0.coffeemaker.exception.IllegalTemplateException;
import com.github.mouse0w0.coffeemaker.exception.TemplateParseException;
import com.github.mouse0w0.coffeemaker.impl.handler.AnnotationHandler;
import com.github.mouse0w0.coffeemaker.impl.handler.InvokeMethodHandler;
import com.github.mouse0w0.coffeemaker.impl.processor.ConstantProcessor;
import com.github.mouse0w0.coffeemaker.impl.processor.ModifyAnnotationProcessor;
import com.github.mouse0w0.coffeemaker.syntax.ATemplate;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateParserImpl implements TemplateParser {

    private static final String TEMPLATE_ANNOTATION = Type.getInternalName(ATemplate.class);

    private final Map<String, AnnotationHandler> annotationHandlerMap = new HashMap<>();
    private final Map<String, InvokeMethodHandler> invokeMethodHandlerMap = new HashMap<>();

    public TemplateParserImpl() {
        addAnnotationHandler(ModifyAnnotationProcessor.handler());
        addInvokeMethodHandler(ConstantProcessor.handler());
    }

    public void addAnnotationHandler(AnnotationHandler handler) {
        for (String annotationType : handler.getSupportedAnnotationTypes()) {
            annotationHandlerMap.put(annotationType, handler);
        }
    }

    public void addInvokeMethodHandler(InvokeMethodHandler handler) {
        for (String invokeMethod : handler.getSupportedInvokeMethods()) {
            invokeMethodHandlerMap.put(invokeMethod, handler);
        }
    }

    @Override
    public Template parse(ClassNodeEx classNode) throws IllegalTemplateException, TemplateParseException {
        AnnotationNodeEx annoTemplateClass = classNode.getAnnotation(TEMPLATE_ANNOTATION);
        if (annoTemplateClass == null) {
            throw new IllegalTemplateException("Class " + classNode.name + "is not a template");
        }

        TemplateImpl template = new TemplateImpl(annoTemplateClass.getValue("value"), classNode);

        List<Processor> processors = template.getProcessors();
        for (AnnotationNodeEx annotation : classNode.annotations.values()) {
            AnnotationHandler handler = annotationHandlerMap.get(annotation.desc);
            if (handler != null) {
                handler.handle(classNode, annotation, processors);
            }
        }

        for (FieldNodeEx field : classNode.fields.values()) {
            for (AnnotationNodeEx annotation : field.annotations.values()) {
                AnnotationHandler handler = annotationHandlerMap.get(annotation.desc);
                if (handler != null) {
                    handler.handle(field, annotation, processors);
                }
            }
        }

        for (MethodNodeEx method : classNode.methods.values()) {
            for (AnnotationNodeEx annotation : method.annotations.values()) {
                AnnotationHandler handler = annotationHandlerMap.get(annotation.desc);
                if (handler != null) {
                    handler.handle(method, annotation, processors);
                }
            }

            for (AbstractInsnNode insn : method.instructions) {
                if (insn instanceof MethodInsnNode) {
                    MethodInsnNode methodInsn = (MethodInsnNode) insn;
                    InvokeMethodHandler handler = invokeMethodHandlerMap.get(ASMUtils.getMethodId(methodInsn));
                    if (handler != null) {
                        handler.handle(method, methodInsn, processors);
                    }
                }
            }
        }

        return template;
    }
}

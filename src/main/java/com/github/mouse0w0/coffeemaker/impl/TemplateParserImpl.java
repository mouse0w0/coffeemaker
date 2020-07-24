package com.github.mouse0w0.coffeemaker.impl;

import com.github.mouse0w0.coffeemaker.Processor;
import com.github.mouse0w0.coffeemaker.Template;
import com.github.mouse0w0.coffeemaker.TemplateParser;
import com.github.mouse0w0.coffeemaker.asm.AnnotationNodeEx;
import com.github.mouse0w0.coffeemaker.asm.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.asm.FieldNodeEx;
import com.github.mouse0w0.coffeemaker.asm.MethodNodeEx;
import com.github.mouse0w0.coffeemaker.exception.IllegalTemplateException;
import com.github.mouse0w0.coffeemaker.exception.TemplateParseException;
import com.github.mouse0w0.coffeemaker.impl.handler.AnnotationHandler;
import com.github.mouse0w0.coffeemaker.impl.handler.InvokeMethodHandler;
import com.github.mouse0w0.coffeemaker.impl.handler.ModifyAnnotationHandler;
import com.github.mouse0w0.coffeemaker.syntax.ATemplate;
import com.github.mouse0w0.coffeemaker.util.ASMUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateParserImpl implements TemplateParser {

    private static final Type TEMPLATE_ANNOTATION = Type.getType(ATemplate.class);

    private final Map<String, AnnotationHandler> annotationHandlerMap = new HashMap<>();
    private final Map<String, InvokeMethodHandler> invokeMethodHandlerMap = new HashMap<>();

    public TemplateParserImpl() {
        addAnnotationHandler(ModifyAnnotationHandler.handler());
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
        AnnotationNode annoTemplateClass = ASMUtils.getInvisibleAnnotation(classNode, TEMPLATE_ANNOTATION);
        if (annoTemplateClass == null) {
            throw new IllegalTemplateException("Class " + classNode.name + "is not a template");
        }

        TemplateImpl template = new TemplateImpl(ASMUtils.getAnnotationValue(annoTemplateClass, "value"), classNode);

        List<Processor> processors = template.getProcessors();
        for (AnnotationNodeEx annotation : classNode.getAnnotationsEx().values()) {
            AnnotationHandler handler = annotationHandlerMap.get(annotation.desc);
            if (handler != null) {
                handler.handle(classNode, annotation, processors);
            }
        }

        for (FieldNodeEx field : classNode.getFieldsEx().values()) {
            for (AnnotationNodeEx annotation : field.getAnnotationsEx().values()) {
                AnnotationHandler handler = annotationHandlerMap.get(annotation.desc);
                if (handler != null) {
                    handler.handle(field, annotation, processors);
                }
            }
        }

        for (MethodNodeEx method : classNode.getMethodsEx().values()) {
            for (AnnotationNodeEx annotation : method.getAnnotationsEx().values()) {
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

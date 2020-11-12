package com.github.mouse0w0.coffeemaker.template.impl;

import com.github.mouse0w0.coffeemaker.template.*;
import com.github.mouse0w0.coffeemaker.template.impl.extree.AnnotationNodeEx;
import com.github.mouse0w0.coffeemaker.template.impl.extree.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.template.impl.extree.FieldNodeEx;
import com.github.mouse0w0.coffeemaker.template.impl.extree.MethodNodeEx;
import com.github.mouse0w0.coffeemaker.template.impl.handler.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateParserImpl implements TemplateParser {

    private static final String TEMPLATE_ANNOTATION = Type.getDescriptor(TemplateClass.class);

    private final Map<String, AnnotationHandler> annotationHandlerMap = new HashMap<>();
    private final Map<String, InvokeMethodHandler> invokeMethodHandlerMap = new HashMap<>();

    public TemplateParserImpl() {
        addAnnotationHandler(new ModifyClassNameHandler());
        addAnnotationHandler(new ModifySourceHandler());
        addAnnotationHandler(new ModifyAnnotationHandler());
        addInvokeMethodHandler(new ConstantsHandler());
        addInvokeMethodHandler(new GetStaticFieldHandler());
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

    public Template parse(ClassNodeEx classNode) throws TemplateParseException {
        if (classNode.getAnnotation(TEMPLATE_ANNOTATION) == null) {
            throw new InvalidTemplateException(classNode.name.replace('/', '.'));
        }
        classNode.removeAnnotation(TEMPLATE_ANNOTATION);

        TemplateImpl template = new TemplateImpl(classNode.name, classNode);

        List<Processor> processors = template.getProcessors();
        for (AnnotationNodeEx annotation : classNode.getAnnotations()) {
            AnnotationHandler handler = annotationHandlerMap.get(annotation.desc);
            if (handler != null) {
                handler.handle(classNode, annotation, processors);
            }
        }

        for (FieldNodeEx field : classNode.fields.values()) {
            for (AnnotationNodeEx annotation : field.getAnnotations()) {
                AnnotationHandler handler = annotationHandlerMap.get(annotation.desc);
                if (handler != null) {
                    handler.handle(field, annotation, processors);
                }
            }
        }

        for (MethodNodeEx method : classNode.methods.values()) {
            for (AnnotationNodeEx annotation : method.getAnnotations()) {
                AnnotationHandler handler = annotationHandlerMap.get(annotation.desc);
                if (handler != null) {
                    handler.handle(method, annotation, processors);
                }
            }

            for (AbstractInsnNode insn : method.instructions) {
                if (insn instanceof MethodInsnNode) {
                    MethodInsnNode methodInsn = (MethodInsnNode) insn;
                    InvokeMethodHandler handler = invokeMethodHandlerMap.get(Utils.getMethodId(methodInsn));
                    if (handler != null) {
                        handler.handle(method, methodInsn, processors);
                    }
                }
            }
        }

        return template;
    }

    @Override
    public Template parse(InputStream in) throws IOException, TemplateParseException {
        return parse(loadClassNode(in));
    }

    private static ClassNodeEx loadClassNode(InputStream in) throws IOException {
        ClassReader classReader = new ClassReader(in);
        ClassNodeEx classNode = new ClassNodeEx();
        classReader.accept(classNode, 0);
        return classNode;
    }
}

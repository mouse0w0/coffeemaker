package com.github.mouse0w0.coffeemaker.template.impl.handler;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.extree.Enum;
import com.github.mouse0w0.coffeemaker.extree.*;
import com.github.mouse0w0.coffeemaker.template.ModifyAnnotation;
import com.github.mouse0w0.coffeemaker.template.TemplateProcessException;
import com.github.mouse0w0.coffeemaker.template.impl.Processor;
import org.objectweb.asm.Type;

import java.util.Collections;
import java.util.List;

public class ModifyAnnotationHandler implements AnnotationHandler {
    private static final String MODIFY_ANNOTATION_DESC = Type.getDescriptor(ModifyAnnotation.class);
    private static final String ANNOTATIONS_DESC = Type.getDescriptor(ModifyAnnotation.Annotations.class);

    @Override
    public String[] getSupportedAnnotationTypes() {
        return new String[]{MODIFY_ANNOTATION_DESC, ANNOTATIONS_DESC};
    }

    @Override
    public void handle(AnnotationHolder owner, AnnotationNodeEx annotation, List<Processor> processors) {
        if (annotation.desc.equals(MODIFY_ANNOTATION_DESC)) {
            processors.add(new ProcessorImpl(owner, annotation));
        } else if (annotation.desc.equals(ANNOTATIONS_DESC)) {
            List<AnnotationNodeEx> values = annotation.getValue("value");
            for (AnnotationNodeEx modifyAnnotation : values) {
                processors.add(new ProcessorImpl(owner, modifyAnnotation));
            }
        }
        owner.removeAnnotation(annotation);
    }

    private static class ProcessorImpl implements Processor {
        private Object owner;
        private String type;
        private boolean visible;
        private List<AnnotationNodeEx> values;

        private ProcessorImpl(Object owner, AnnotationNodeEx modifyAnnotation) {
            this.owner = owner;
            this.type = modifyAnnotation.<Type>getValue("type").getDescriptor();
            this.visible = modifyAnnotation.getValue("visible", true);
            this.values = modifyAnnotation.getValue("values", Collections.emptyList());
        }

        @Override
        public ClassNodeEx process(ClassNodeEx classNode, Evaluator evaluator) {
            AnnotationHolder annotationHolder;
            if (owner instanceof ClassNodeEx) {
                annotationHolder = classNode;
            } else if (owner instanceof FieldNodeEx) {
                annotationHolder = classNode.getField(((FieldNodeEx) owner).name);
            } else if (owner instanceof MethodNodeEx) {
                annotationHolder = classNode.getMethod(((MethodNodeEx) owner).toMethod());
            } else {
                throw new TemplateProcessException("Should not reach here");
            }

            AnnotationNodeEx annotation = classNode.getAnnotation(type);
            if (annotation == null) {
                annotation = new AnnotationNodeEx(type, visible);
                annotationHolder.addAnnotation(annotation);
            }

            for (AnnotationNodeEx value : values) {
                Type type = value.getValue("type");
                boolean isEnum = value.getValue("isEnum", false);
                String name = value.getValue("name");
                String statement = value.getValue("statement");
                if (isEnum) {
                    annotation.putValue(name, new Enum(type, evaluator.eval(statement)));
                } else {
                    annotation.putValue(name, evaluator.eval(statement));
                }
            }
            return classNode;
        }
    }
}

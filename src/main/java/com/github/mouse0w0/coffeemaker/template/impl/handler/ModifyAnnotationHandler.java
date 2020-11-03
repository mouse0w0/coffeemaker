package com.github.mouse0w0.coffeemaker.template.impl.handler;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.extree.AnnotationNodeEx;
import com.github.mouse0w0.coffeemaker.extree.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.extree.Enum;
import com.github.mouse0w0.coffeemaker.template.ModifyAnnotation;
import com.github.mouse0w0.coffeemaker.template.impl.Processor;
import org.objectweb.asm.Type;

import java.util.Collections;
import java.util.List;

public class ModifyAnnotationHandler implements AnnotationHandler {
    private static final String MODIFY_ANNOTATION_DESC = Type.getDescriptor(ModifyAnnotation.class);
    private static final String MODIFY_ANNOTATIONS_DESC = Type.getDescriptor(ModifyAnnotation.Annotations.class);

    @Override
    public String[] getSupportedAnnotationTypes() {
        return new String[]{MODIFY_ANNOTATION_DESC, MODIFY_ANNOTATIONS_DESC};
    }

    @Override
    public void handle(Object owner, AnnotationNodeEx annotation, List<Processor> processors) {
        if (annotation.desc.equals(MODIFY_ANNOTATION_DESC)) {
            processors.add(new ProcessorImpl(annotation));
        } else if (annotation.desc.equals(MODIFY_ANNOTATIONS_DESC)) {
            List<AnnotationNodeEx> values = annotation.getValue("value");
            for (AnnotationNodeEx value : values) {
                processors.add(new ProcessorImpl(value));
            }
        }
    }

    private static class ProcessorImpl implements Processor {
        private String type;
        private boolean visible;
        private List<AnnotationNodeEx> values;

        private ProcessorImpl(AnnotationNodeEx annotation) {
            type = annotation.<Type>getValue("type").getDescriptor();
            visible = annotation.getValue("visible", true);
            values = annotation.getValue("values", Collections.emptyList());
        }

        @Override
        public void process(ClassNodeEx classNode, Evaluator evaluator) {
            AnnotationNodeEx annotation = classNode.getAnnotation(type);
            if (annotation == null) {
                annotation = new AnnotationNodeEx(type, visible);
                classNode.addAnnotation(annotation);
            }

            for (AnnotationNodeEx value : values) {
                Type type = value.getValue("type");
                boolean isEnum = value.getValue("isEnum", false);
                String name = value.getValue("name");
                String statement = value.getValue("statement");
                if (isEnum) {
                    value.putValue(name, new Enum(type, evaluator.eval(statement)));
                } else {
                    value.putValue(name, evaluator.eval(statement));
                }
            }
        }
    }
}

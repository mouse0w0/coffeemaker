package com.github.mouse0w0.coffeemaker.impl.processor;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.Processor;
import com.github.mouse0w0.coffeemaker.asm.AnnotationNodeEx;
import com.github.mouse0w0.coffeemaker.asm.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.syntax.ModifyAnnotation;
import com.github.mouse0w0.coffeemaker.syntax.ModifyAnnotations;
import org.objectweb.asm.Type;

import java.util.List;

public class ModifyAnnotationProcessor implements Processor {

    public static Processor.Factory factory() {
        return new Factory();
    }

    private final String type;
    private final String name;
    private final String statement;

    private ModifyAnnotationProcessor(AnnotationNodeEx annotation) {
        this.type = annotation.<Type>getValueEx("type").getDescriptor();
        this.name = annotation.getValueEx("name");
        this.statement = annotation.getValueEx("statement");
    }

    @Override
    public void process(ClassNodeEx classNode, Evaluator evaluator) {
        AnnotationNodeEx annotation = classNode.getAnnotationEx(type);
        annotation.setValueEx(name, evaluator.eval(statement));
    }

    private static class Factory implements Processor.Factory {

        private static final String MODIFY_ANNOTATION_DESC = Type.getDescriptor(ModifyAnnotation.class);
        private static final String MODIFY_ANNOTATIONS_DESC = Type.getDescriptor(ModifyAnnotations.class);

        @Override
        public void create(ClassNodeEx classNode, List<Processor> processors) {
            AnnotationNodeEx annotation = classNode.getAnnotationEx(MODIFY_ANNOTATION_DESC);
            if (annotation != null) {
                processors.add(new ModifyAnnotationProcessor(annotation));
                return;
            }

            annotation = classNode.getAnnotationEx(MODIFY_ANNOTATIONS_DESC);
            if (annotation != null) {
                List<AnnotationNodeEx> values = annotation.getValueEx("value");
                for (AnnotationNodeEx value : values) {
                    processors.add(new ModifyAnnotationProcessor(value));
                }
            }
        }
    }
}

package com.github.mouse0w0.coffeemaker.template.impl.handler;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.extree.AnnotationHolder;
import com.github.mouse0w0.coffeemaker.extree.AnnotationNodeEx;
import com.github.mouse0w0.coffeemaker.extree.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.template.ModifyClassName;
import com.github.mouse0w0.coffeemaker.template.impl.Processor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;

import java.util.List;

public class ModifyClassNameHandler implements AnnotationHandler {
    private static final String DESC = Type.getDescriptor(ModifyClassName.class);

    @Override
    public String[] getSupportedAnnotationTypes() {
        return new String[]{DESC};
    }

    @Override
    public void handle(AnnotationHolder owner, AnnotationNodeEx annotation, List<Processor> processors) {
        String statement = annotation.getValue("value");
        processors.add(new ProcessorImpl(statement));
        owner.removeAnnotation(annotation);
    }

    private static class ProcessorImpl implements Processor {

        private final String statement;

        private ProcessorImpl(String statement) {
            this.statement = statement;
        }

        @Override
        public ClassNodeEx process(ClassNodeEx classNode, Evaluator evaluator) {
            String newName = evaluator.eval(statement);
            ClassNodeEx newClassNode = new ClassNodeEx();
            ClassRemapper cr = new ClassRemapper(newClassNode, new SimpleRemapper(classNode.name, newName));
            classNode.accept(cr);
            return newClassNode;
        }
    }
}

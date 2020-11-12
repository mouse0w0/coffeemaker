package com.github.mouse0w0.coffeemaker.template.impl.handler;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.template.ModifySource;
import com.github.mouse0w0.coffeemaker.template.impl.Processor;
import com.github.mouse0w0.coffeemaker.template.impl.extree.AnnotationHolder;
import com.github.mouse0w0.coffeemaker.template.impl.extree.AnnotationNodeEx;
import com.github.mouse0w0.coffeemaker.template.impl.extree.ClassNodeEx;
import org.objectweb.asm.Type;

import java.util.List;

public class ModifySourceHandler implements AnnotationHandler {
    private static final String DESC = Type.getDescriptor(ModifySource.class);

    @Override
    public String[] getSupportedAnnotationTypes() {
        return new String[]{DESC};
    }

    @Override
    public void handle(AnnotationHolder owner, AnnotationNodeEx annotation, List<Processor> processors) {
        String sourceFile = annotation.getValue("sourceFile", "");
        String sourceDebug = annotation.getValue("sourceDebug", "");
        processors.add(new ProcessorImpl(sourceFile, sourceDebug));
        owner.removeAnnotation(annotation);
    }

    private static class ProcessorImpl implements Processor {

        private final String sourceFile;
        private final String sourceDebug;

        public ProcessorImpl(String sourceFile, String sourceDebug) {
            this.sourceFile = sourceFile;
            this.sourceDebug = sourceDebug;
        }

        @Override
        public ClassNodeEx process(ClassNodeEx classNode, Evaluator evaluator) {
            classNode.sourceFile = sourceFile;
            classNode.sourceDebug = sourceDebug;
            return classNode;
        }
    }
}

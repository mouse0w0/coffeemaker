package com.github.mouse0w0.coffeemaker.impl.processor;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.Processor;
import com.github.mouse0w0.coffeemaker.syntax.ModifyAnnotation;
import com.github.mouse0w0.coffeemaker.syntax.ModifyAnnotations;
import com.github.mouse0w0.coffeemaker.util.ASMUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;
import java.util.Map;

public class ModifyAnnotationProcessor implements Processor {

    public static Processor.Factory factory() {
        return new Factory();
    }

    private ModifyAnnotationProcessor(AnnotationNode annotation) {
        Map<String, Object> values = ASMUtils.getAnnotationValues(annotation);
    }

    @Override
    public void process(ClassNode classNode, Evaluator evaluator) {

    }

    private static class Factory implements Processor.Factory {

        private static final String MODIFY_ANNOTATION_DESC = Type.getDescriptor(ModifyAnnotation.class);
        private static final String MODIFY_ANNOTATIONS_DESC = Type.getDescriptor(ModifyAnnotations.class);

        @Override
        public void create(ClassNode classNode, List<Processor> processors) {
            for (AnnotationNode annotation : classNode.invisibleAnnotations) {
                if (MODIFY_ANNOTATION_DESC.equals(annotation.desc)) {
                    processors.add(new ModifyAnnotationProcessor(annotation));
                    return;
                }

                if (MODIFY_ANNOTATIONS_DESC.equals(annotation.desc)) {
                    List<AnnotationNode> values = ASMUtils.getAnnotationValue(annotation, "value");
                    assert values != null;
                    values.stream().map(ModifyAnnotationProcessor::new).forEach(processors::add);
                    return;
                }
            }
        }
    }
}

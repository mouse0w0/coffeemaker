package com.github.mouse0w0.coffeemaker.template.impl.handler;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.PrefixEvaluator;
import com.github.mouse0w0.coffeemaker.extree.AnnotationHolder;
import com.github.mouse0w0.coffeemaker.extree.AnnotationNodeEx;
import com.github.mouse0w0.coffeemaker.extree.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.extree.FieldNodeEx;
import com.github.mouse0w0.coffeemaker.template.DeclareStaticFieldForeach;
import com.github.mouse0w0.coffeemaker.template.impl.Processor;
import org.objectweb.asm.Type;

import java.util.List;

public class DeclareStaticFieldForeachHandler implements AnnotationHandler {
    private static final String DESC = Type.getDescriptor(DeclareStaticFieldForeach.class);

    @Override
    public String[] getSupportedAnnotationTypes() {
        return new String[]{DESC};
    }

    @Override
    public void handle(AnnotationHolder owner, AnnotationNodeEx annotation, List<Processor> processors) {
        String iterator = annotation.getValue("iterator", null);
        String elementName = annotation.getValue("elementName", null);
        String fieldName = annotation.getValue("fieldName", null);
        String factoryName = annotation.getValue("factoryName", null);
        String factoryDesc = annotation.getValue("factoryDesc", null);
        String[] factoryArgs = annotation.getValue("factoryArgs", null);

        FieldNodeEx field = (FieldNodeEx) owner;
        field.getOwner().removeField(field);
        processors.add(new ProcessorImpl(field, iterator, elementName, fieldName, factoryName, factoryDesc, factoryArgs));
    }

    private static class ProcessorImpl implements Processor {
        private final FieldNodeEx field;
        private final String iterator;
        private final String elementName;
        private final String fieldName;
        private final String factoryName;
        private final String factoryDesc;
        private final Type[] factoryArgTypes;
        private final String[] factoryArgs;

        public ProcessorImpl(FieldNodeEx field, String iterator, String elementName, String fieldName, String factoryName, String factoryDesc, String[] factoryArgs) {
            this.field = field;
            this.iterator = iterator;
            this.elementName = elementName.endsWith(".") ? elementName : elementName + ".";
            this.fieldName = fieldName;
            this.factoryName = factoryName;
            this.factoryDesc = factoryDesc;
            this.factoryArgTypes = Type.getArgumentTypes(factoryDesc);
            this.factoryArgs = factoryArgs;
        }

        @Override
        public ClassNodeEx process(ClassNodeEx classNode, Evaluator evaluator) {
            for (Object e : (Iterable<Object>) evaluator.eval(iterator)) {
                PrefixEvaluator subEvaluator = new PrefixEvaluator(evaluator, e, elementName);

                String fieldName = subEvaluator.eval(this.fieldName);

            }
            return classNode;
        }
    }
}

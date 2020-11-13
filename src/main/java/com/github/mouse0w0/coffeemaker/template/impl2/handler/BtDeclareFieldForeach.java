package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.evaluator.PrefixEvaluator;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtField;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

public class BtDeclareFieldForeach extends BtField {
    private final String iterable;
    private final String elementName;

    public BtDeclareFieldForeach(BtField btField, String iterable, String elementName, String fieldName) {
        this.iterable = iterable;
        this.elementName = elementName;
        btField.forEach(this::put);
        btField.put("name", new BtComputableValue(fieldName));
    }

    @Override
    public void accept(ClassVisitor classVisitor, Evaluator evaluator) {
        Iterable<?> iterable = evaluator.eval(this.iterable);
        for (Object value : iterable) {
            PrefixEvaluator subEvaluator = new PrefixEvaluator(evaluator, value, elementName);
            FieldVisitor fieldVisitor = classVisitor.visitField(
                    computeInt(ACCESS, subEvaluator),
                    computeString(NAME, subEvaluator),
                    computeDescriptor(DESCRIPTOR, subEvaluator),
                    computeString(SIGNATURE, subEvaluator),
                    compute(VALUE, subEvaluator));
            if (fieldVisitor == null) return;
            accept(fieldVisitor, subEvaluator);
        }
    }
}

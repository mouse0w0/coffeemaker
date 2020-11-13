package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.evaluator.PrefixEvaluator;
import com.github.mouse0w0.coffeemaker.template.Field;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtField;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

public class BtDeclareFieldForeach extends BtField {
    private final String iterable;
    private final String elementName;

    public BtDeclareFieldForeach(String iterable, String elementName) {
        this.iterable = iterable;
        this.elementName = elementName;
    }

    @Override
    public void accept(ClassVisitor classVisitor, Evaluator evaluator) {
        Iterable<Field> iterable = evaluator.eval(this.iterable);
        for (Field field : iterable) {
            PrefixEvaluator subEvaluator = new PrefixEvaluator(evaluator, field, elementName);
            FieldVisitor fieldVisitor = classVisitor.visitField(
                    computeInt(ACCESS, subEvaluator),
                    field.getName(),
                    field.getDescriptor(),
                    computeString(SIGNATURE, subEvaluator),
                    compute(VALUE, subEvaluator));
            if (fieldVisitor == null) return;
            accept(fieldVisitor, subEvaluator);
        }
    }
}

package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.evaluator.PrefixEvaluator;
import com.github.mouse0w0.coffeemaker.template.Field;
import com.github.mouse0w0.coffeemaker.template.TemplateProcessException;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtField;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

public class BtDeclareFieldForeach extends BtField {
    private final String iterable;
    private final String elementName;
    private final String expression;

    public BtDeclareFieldForeach(String iterable, String elementName, String expression) {
        this.iterable = iterable;
        this.elementName = elementName;
        this.expression = expression;
    }

    @Override
    public void accept(ClassVisitor classVisitor, Evaluator evaluator) {
        Iterable<Object> iterable = evaluator.eval(this.iterable);
        for (Object element : iterable) {
            final PrefixEvaluator subEvaluator = new PrefixEvaluator(evaluator, element, elementName);
            final Field field = subEvaluator.eval(expression);
            if (field == null) {
                throw new TemplateProcessException("The field cannot be null");
            }
            final FieldVisitor fieldVisitor = classVisitor.visitField(
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

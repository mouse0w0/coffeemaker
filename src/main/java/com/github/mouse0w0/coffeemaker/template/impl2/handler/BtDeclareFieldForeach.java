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
    private final boolean modifyDescriptor;

    public BtDeclareFieldForeach(String iterable, String elementName, String expression, boolean modifyDescriptor) {
        this.iterable = iterable;
        this.elementName = elementName;
        this.expression = expression;
        this.modifyDescriptor = modifyDescriptor;
    }

    @Override
    public void accept(ClassVisitor classVisitor, Evaluator evaluator) {
        Iterable<Object> iterable = evaluator.eval(this.iterable);
        if (modifyDescriptor) {
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
        } else {
            for (Object element : iterable) {
                final PrefixEvaluator subEvaluator = new PrefixEvaluator(evaluator, element, elementName);
                final Object field = subEvaluator.eval(expression);
                if (field == null) {
                    throw new TemplateProcessException("The field cannot be null");
                }
                final String name;
                if (field instanceof String) {
                    name = (String) field;
                } else if (field instanceof Field) {
                    name = ((Field) field).getName();
                } else {
                    throw new TemplateProcessException("The field must be String or Field");
                }
                final FieldVisitor fieldVisitor = classVisitor.visitField(
                        computeInt(ACCESS, subEvaluator),
                        name,
                        computeDescriptor(DESCRIPTOR, subEvaluator),
                        computeString(SIGNATURE, subEvaluator),
                        compute(VALUE, subEvaluator));
                if (fieldVisitor == null) return;
                accept(fieldVisitor, subEvaluator);
            }
        }
    }
}

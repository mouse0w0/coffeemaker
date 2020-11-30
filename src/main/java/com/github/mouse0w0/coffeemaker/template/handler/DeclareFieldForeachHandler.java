package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.DeclareFieldForeach;
import com.github.mouse0w0.coffeemaker.template.tree.AnnotationOwner;
import com.github.mouse0w0.coffeemaker.template.tree.BtAnnotation;
import com.github.mouse0w0.coffeemaker.template.tree.BtClass;
import com.github.mouse0w0.coffeemaker.template.tree.BtField;
import org.objectweb.asm.Opcodes;

public class DeclareFieldForeachHandler extends AnnotationHandler {
    @Override
    protected Class<?>[] getAcceptableAnnotations() {
        return new Class[]{DeclareFieldForeach.class};
    }

    @Override
    protected void handle(AnnotationOwner owner, BtAnnotation annotation) {
        String iterable = annotation.getValue("iterable");
        String elementName = annotation.getValue("variableName");
        String expression = annotation.getValue("expression", elementName);
        int modifyFinal = annotation.getValue("modifyFinal", true) ? Opcodes.ACC_FINAL : 0;

        BtField field = (BtField) owner;
        field.getAnnotations().remove(annotation);
        BtClass clazz = (BtClass) field.getParent().getParent();
        clazz.getFields().remove(field);

        BtDeclareFieldForeach fieldForeach = new BtDeclareFieldForeach(iterable, elementName, expression);
        fieldForeach.putValue(BtField.ACCESS, field.get(BtField.ACCESS).getAsInt() | modifyFinal);
        field.forEach(fieldForeach::put);

        clazz.getFields().add(fieldForeach);
    }
}

package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.template.DeclareFieldForeach;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.AnnotationOwner;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtAnnotation;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtClass;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtField;

public class DeclareFieldForeachHandler extends AnnotationHandler {
    @Override
    protected Class<?>[] getAcceptableAnnotations() {
        return new Class[]{DeclareFieldForeach.class};
    }

    @Override
    protected void handle(AnnotationOwner owner, BtAnnotation annotation) {
        String iterable = annotation.getValue("iterable");
        String elementName = annotation.getValue("elementName");
        boolean modifierFinal = annotation.getValue("modifierFinal", true);
        BtField field = (BtField) owner;
//        field.getAnnotations().remove(annotation);
        BtClass clazz = (BtClass) field.getParent().getParent();
        clazz.getFields().remove(field);
        clazz.getFields().add(new BtDeclareFieldForeach(field, iterable, elementName, modifierFinal));
    }
}

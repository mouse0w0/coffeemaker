package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.template.ModifyAnnotation;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.AnnotationOwner;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtAnnotation;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtEnum;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtList;
import org.objectweb.asm.Type;

public class ModifyAnnotationHandler extends AnnotationHandler {

    private static final String MODIFY_ANNOTATION = Type.getDescriptor(ModifyAnnotation.class);

    @Override
    protected Class<?>[] getAcceptableAnnotations() {
        return new Class<?>[]{ModifyAnnotation.class, ModifyAnnotation.Annotations.class};
    }

    @Override
    protected void handle(AnnotationOwner owner, BtAnnotation annotation) {
        String descriptor = annotation.get(BtAnnotation.DESCRIPTOR).getAsString();
        if (MODIFY_ANNOTATION.equals(descriptor)) {
            handleModifyAnnotation(owner, annotation);
        } else {
            BtList<BtAnnotation> values = annotation.getValues().get("value");
            for (BtAnnotation value : values) {
                handleModifyAnnotation(owner, value);
            }
        }
        owner.getAnnotations().remove(annotation);
    }

    protected void handleModifyAnnotation(AnnotationOwner owner, BtAnnotation annotation) {
        String descriptor = annotation.<Type>getValue("type").getDescriptor();
        boolean visible = annotation.getValue("visible", true);
        BtList<BtAnnotation> values = annotation.getValues().get("values");

        BtAnnotation modified = owner.getAnnotation(descriptor);
        if (modified == null) {
            modified = new BtAnnotation(descriptor, visible);
            owner.getAnnotations().add(modified);
        }

        for (BtAnnotation value : values) {
            String type = value.<Type>getValue("type").getDescriptor();
            boolean isEnum = value.getValue("isEnum", false);
            String name = value.getValue("name");
            String statement = value.getValue("expression");
            if (isEnum) {
                BtEnum btEnum = new BtEnum();
                btEnum.putValue(BtEnum.DESCRIPTOR, type);
                btEnum.put(BtEnum.VALUE, new BtComputableValue(statement));
                modified.getValues().put(name, btEnum);
            } else {
                modified.getValues().put(name, new BtComputableValue(statement));
            }
        }
    }
}

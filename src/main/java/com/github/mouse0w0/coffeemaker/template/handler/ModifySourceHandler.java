package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.ModifySource;
import com.github.mouse0w0.coffeemaker.template.tree.BtAnnotation;
import com.github.mouse0w0.coffeemaker.template.tree.BtClass;
import com.github.mouse0w0.coffeemaker.template.tree.BtNode;
import org.objectweb.asm.Type;

public class ModifySourceHandler implements Handler {
    private static final String DESC = Type.getDescriptor(ModifySource.class);

    @Override
    public void handle(BtClass clazz) {
        BtAnnotation annotation = clazz.getAnnotation(DESC);
        if (annotation != null) {
            clazz.getAnnotations().remove(annotation);

            BtNode sourceFile = annotation.getValues().get("sourceFile");
            if (sourceFile != null) clazz.put(BtClass.SOURCE_FILE, sourceFile);
            BtNode sourceDebug = annotation.getValues().get("sourceDebug");
            if (sourceDebug != null) clazz.put(BtClass.SOURCE_DEBUG, sourceDebug);
        }
    }
}

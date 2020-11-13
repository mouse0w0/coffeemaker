package com.github.mouse0w0.coffeemaker.template.impl2;

import com.github.mouse0w0.coffeemaker.template.*;
import com.github.mouse0w0.coffeemaker.template.impl2.handler.*;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtAnnotation;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtClass;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TemplateParserImpl implements TemplateParser {
    private static final String TEMPLATE_ANNOTATION = Type.getDescriptor(TemplateClass.class);

    private final List<Handler> handlers = new ArrayList<>();

    public TemplateParserImpl() {
        handlers.add(new ModifySourceHandler());
        handlers.add(new ModifyAnnotationHandler());
        handlers.add(new DeclareFieldForeachHandler());
        handlers.add(new ConstantsHandler());
        handlers.add(new GetStaticFieldHandler());
        handlers.add(new SetStaticFieldHandler());
        handlers.add(new NewInstanceHandler());
        handlers.add(new ForeachHandler());
    }

    @Override
    public Template parse(InputStream in) throws IOException, TemplateParseException {
        ClassReader cr = new ClassReader(in);
        BtClassVisitor cv = new BtClassVisitor();
        cr.accept(cv, 0);
        BtClass btClass = cv.getBtClass();

        String name = btClass.get(BtClass.NAME).getAsString();

        BtAnnotation annotation = btClass.getAnnotation(TEMPLATE_ANNOTATION);
        if (annotation == null) {
            throw new InvalidTemplateException(name.replace('/', '.'));
        }
        btClass.getAnnotations().remove(annotation);

        for (Handler handler : handlers) {
            handler.handle(btClass);
        }

        return new TemplateImpl(name, btClass);
    }
}

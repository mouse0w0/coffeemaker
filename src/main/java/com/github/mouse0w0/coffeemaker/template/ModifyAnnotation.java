package com.github.mouse0w0.coffeemaker.template;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.CLASS)
@Repeatable(ModifyAnnotation.Annotations.class)
public @interface ModifyAnnotation {
    Class<? extends Annotation> type();

    boolean visible() default true;

    Value[] values() default {};

    @interface Value {
        Class<?> type();

        boolean isEnum() default false;

        String name();

        String expression();
    }

    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
    @Retention(RetentionPolicy.CLASS)
    @interface Annotations {
        ModifyAnnotation[] value();
    }
}

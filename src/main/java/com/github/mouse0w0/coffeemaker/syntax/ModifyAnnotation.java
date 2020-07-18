package com.github.mouse0w0.coffeemaker.syntax;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.CLASS)
@Repeatable(ModifyAnnotations.class)
public @interface ModifyAnnotation {

    Class<? extends Annotation> type();

    String name() default "value";

    String statement();

}

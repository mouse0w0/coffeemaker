package com.github.mouse0w0.coffeemaker.template;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface DeclareFieldForeach {
    /**
     * @return The expression of iterable.
     */
    String iterable();

    /**
     * @return The name of each element.
     */
    String elementName() default "i";

    /**
     * @return The expression of declare field.
     */
    String fieldName();
}

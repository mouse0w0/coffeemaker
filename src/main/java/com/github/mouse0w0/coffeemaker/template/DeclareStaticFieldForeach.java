package com.github.mouse0w0.coffeemaker.template;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface DeclareStaticFieldForeach {
    /**
     * @return 迭代器的查询语句
     */
    String iterator();

    /**
     * @return 每个迭代元素的临时变量名
     */
    String elementName() default "i";

    /**
     * @return 字段名称的查询语句
     */
    String fieldName();

    /**
     * @return 工厂方法的方法名和方法描述
     */
    String factory();

    /**
     * @return 工厂方法参数的查询语句
     */
    String[] factoryArgs() default {};
}

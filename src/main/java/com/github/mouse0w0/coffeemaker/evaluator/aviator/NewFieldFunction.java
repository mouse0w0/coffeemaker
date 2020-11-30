package com.github.mouse0w0.coffeemaker.evaluator.aviator;

import com.github.mouse0w0.coffeemaker.template.Field;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.Map;

public final class NewFieldFunction extends AbstractFunction {
    public static final NewFieldFunction INSTANCE = new NewFieldFunction();

    @Override
    public String getName() {
        return "newField";
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        String owner = FunctionUtils.getStringValue(arg1, env);
        String name = FunctionUtils.getStringValue(arg2, env);
        String descriptor = FunctionUtils.getStringValue(arg3, env);
        return AviatorRuntimeJavaType.valueOf(new Field(owner, name, descriptor));
    }
}

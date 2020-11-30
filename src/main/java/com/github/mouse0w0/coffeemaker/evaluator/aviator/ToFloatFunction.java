package com.github.mouse0w0.coffeemaker.evaluator.aviator;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.Map;

public final class ToFloatFunction extends AbstractFunction {
    public static final ToFloatFunction INSTANCE = new ToFloatFunction();

    @Override
    public String getName() {
        return "toFloat";
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return AviatorRuntimeJavaType.valueOf(FunctionUtils.getNumberValue(arg1, env).floatValue());
    }
}

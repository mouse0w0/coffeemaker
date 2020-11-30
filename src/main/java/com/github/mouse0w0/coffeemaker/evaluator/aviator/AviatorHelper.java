package com.github.mouse0w0.coffeemaker.evaluator.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Feature;

public final class AviatorHelper {
    private static final AviatorEvaluatorInstance INSTANCE = newInstance();

    public static AviatorEvaluatorInstance getInstance() {
        return INSTANCE;
    }

    public static AviatorEvaluatorInstance newInstance() {
        AviatorEvaluatorInstance instance = AviatorEvaluator.newInstance();
        instance.removeModule("io");
        instance.disableFeature(Feature.NewInstance);
        instance.disableFeature(Feature.Module);
        instance.disableFeature(Feature.InternalVars);
        instance.addFunction(ToFloatFunction.INSTANCE);
        instance.addFunction(ToIntFunction.INSTANCE);
        instance.addFunction(NewFieldFunction.INSTANCE);
        return instance;
    }

    private AviatorHelper() {
    }
}

package com.github.mouse0w0.coffeemaker.evaluator.aviator;

import com.github.mouse0w0.coffeemaker.template.Field;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Feature;
import org.objectweb.asm.Type;

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
        instance.addFunction(ToFloatFunction.INSTANCE);
        instance.addFunction(ToIntFunction.INSTANCE);
        instance.addFunction(NewFieldFunction.INSTANCE);
        try {
            instance.addStaticFunctions("Type", Type.class);

            instance.addInstanceFunctions("str", String.class);
            instance.addInstanceFunctions("type", Type.class);
            instance.addInstanceFunctions("field", Field.class);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
        return instance;
    }

    private AviatorHelper() {
    }
}

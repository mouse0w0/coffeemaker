package com.github.mouse0w0.coffeemaker.evaluator;

public class PrefixEvaluator extends SimpleEvaluator {
    private final String prefix;

    public PrefixEvaluator(Object dataModel, String prefix) {
        super(dataModel);
        this.prefix = prefix;
    }

    public PrefixEvaluator(Evaluator parent, Object dataModel, String prefix) {
        super(parent, dataModel);
        this.prefix = prefix;
    }

    @Override
    public <T> T eval(String expression) {
        return expression.startsWith(prefix) ? eval0(expression.substring(prefix.length())) : super.eval(expression);
    }
}

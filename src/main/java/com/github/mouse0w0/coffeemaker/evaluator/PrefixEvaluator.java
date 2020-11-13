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
    @SuppressWarnings("unchecked")
    public <T> T eval(String expression) {
        int dotIndex = expression.indexOf('.');
        String first = dotIndex == -1 ? expression : expression.substring(0, dotIndex);
        if (dotIndex == -1) {
            if (prefix.equals(first)) return (T) dataModel;
            else return super.eval(expression);
        } else {
            if (prefix.equals(first)) return eval0(expression.substring(dotIndex + 1));
            else return super.eval(expression);
        }
    }
}

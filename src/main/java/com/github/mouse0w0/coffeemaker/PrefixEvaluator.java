package com.github.mouse0w0.coffeemaker;

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
    public <T> T eval(String statement) {
        return statement.startsWith(prefix) ? eval0(statement.substring(prefix.length())) : super.eval(statement);
    }
}

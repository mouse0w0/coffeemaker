package com.github.mouse0w0.coffeemaker.template;

public final class Markers {
    // Constants
    public static <T> Class<T> $class(String expression) {
        throw new UnsupportedOperationException("template");
    }

    public static String $string(String expression) {
        throw new UnsupportedOperationException("template");
    }

    public static byte $byte(String expression) {
        throw new UnsupportedOperationException("template");
    }

    public static short $short(String expression) {
        throw new UnsupportedOperationException("template");
    }

    public static int $int(String expression) {
        throw new UnsupportedOperationException("template");
    }

    public static long $long(String expression) {
        throw new UnsupportedOperationException("template");
    }

    public static float $float(String expression) {
        throw new UnsupportedOperationException("template");
    }

    public static double $double(String expression) {
        throw new UnsupportedOperationException("template");
    }

    public static char $char(String expression) {
        throw new UnsupportedOperationException("template");
    }

    public static boolean $bool(String expression) {
        throw new UnsupportedOperationException("template");
    }

    // Static field accesses

    /**
     * @param expression An expression whose result is {@link com.github.mouse0w0.coffeemaker.template.Field}
     * @param <T>
     * @return
     */
    public static <T> T $getStaticField(String expression) {
        throw new UnsupportedOperationException("template");
    }

    /**
     * @param expression An expression whose result is {@link com.github.mouse0w0.coffeemaker.template.Field}
     * @param value
     */
    public static void $setStaticField(String expression, Object value) {
        throw new UnsupportedOperationException("template");
    }

    /**
     * New instance of expression.
     *
     * @param expression An expression whose result is descriptor or {@link org.objectweb.asm.Type}
     * @param <T>
     * @return
     */
    public static <T> T $new(String expression) {
        throw new UnsupportedOperationException("template");
    }

    // For
    public static void $foreach(String iterable, String varName) {
        throw new UnsupportedOperationException("template");
    }

    public static void $endForeach() {
        throw new UnsupportedOperationException("template");
    }

    // If
    public static void $if(String expression) {
        throw new UnsupportedOperationException("template");
    }

    public static void $elseIf(String expression) {
        throw new UnsupportedOperationException("template");
    }

    public static void $else() {
        throw new UnsupportedOperationException("template");
    }

    public static void $endIf() {
        throw new UnsupportedOperationException("template");
    }

    private Markers() {
    }
}

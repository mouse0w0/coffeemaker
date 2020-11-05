package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.template.impl2.tree.util.SmartList;

public class BtMethod extends BtObject {

    public static final String ACCESS = "access";
    public static final String NAME = "name";
    public static final String DESCRIPTOR = "descriptor";
    public static final String SIGNATURE = "signature";
    public static final String EXCEPTIONS = "exceptions";

    public static final String PARAMETERS = "parameters";

    public static final String ANNOTATIONS = "annotations";
    public static final String TYPE_ANNOTATIONS = "typeAnnotations";

    public static final String ATTRIBUTES = "attributes";

    public static final String ANNOTATION_DEFAULT = "annotationDefault";

    public static final String INSTRUCTIONS = "instructions";

    public static final String TRY_CATCH_BLOCKS = "tryCatchBlocks";

    public static final String MAX_STACK = "maxStack";
    public static final String MAX_LOCALS = "maxLocals";

    public static final String LOCAL_VARIABLES = "localVariables";

    public BtMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        putValue(ACCESS, access);
        putValue(NAME, name);
        putValue(DESCRIPTOR, descriptor);
        putValue(SIGNATURE, signature);
        putValue(EXCEPTIONS, new SmartList<>(exceptions));
    }

    public BtMethod() {
    }
}

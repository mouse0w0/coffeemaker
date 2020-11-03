package com.github.mouse0w0.coffeemaker.extree;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

public class TypeAnnotationNodeEx extends AnnotationNodeEx {

    /**
     * A reference to the annotated type. See {@link org.objectweb.asm.TypeReference}.
     */
    public int typeRef;

    /**
     * The path to the annotated type argument, wildcard bound, array element type, or static outer
     * type within the referenced type. May be {@literal null} if the annotation targets 'typeRef' as
     * a whole.
     */
    public TypePath typePath;

    /**
     * Constructs a new {@link AnnotationNodeEx}. <i>Subclasses must not use this constructor</i>.
     * Instead, they must use the {@link TypeAnnotationNodeEx(int, int, TypePath, String, boolean)} version.
     *
     * @param typeRef    a reference to the annotated type. See {@link org.objectweb.asm.TypeReference}.
     * @param typePath   the path to the annotated type argument, wildcard bound, array element type, or
     *                   static inner type within 'typeRef'. May be {@literal null} if the annotation targets
     *                   'typeRef' as a whole.
     * @param descriptor the class descriptor of the annotation class.
     * @param visible    the visibility of the annotation.
     * @throws IllegalStateException If a subclass calls this constructor.
     */
    public TypeAnnotationNodeEx(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        this(/* latest api = */ Opcodes.ASM8, typeRef, typePath, descriptor, visible);
    }

    /**
     * Constructs a new {@link AnnotationNodeEx}.
     *
     * @param api        the ASM API version implemented by this visitor. Must be one of {@link
     *                   Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6}, {@link Opcodes#ASM7} or {@link
     *                   Opcodes#ASM8}.
     * @param typeRef    a reference to the annotated type. See {@link org.objectweb.asm.TypeReference}.
     * @param typePath   the path to the annotated type argument, wildcard bound, array element type, or
     *                   static inner type within 'typeRef'. May be {@literal null} if the annotation targets
     *                   'typeRef' as a whole.
     * @param descriptor the class descriptor of the annotation class.
     * @param visible    the visibility of the annotation.
     */
    public TypeAnnotationNodeEx(
            final int api, final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        super(api, descriptor, visible);
        this.typeRef = typeRef;
        this.typePath = typePath;
    }
}

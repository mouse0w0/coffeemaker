package com.github.mouse0w0.coffeemaker.template.tree;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtLabel;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeAnnotationNode;

import java.util.List;

/**
 * A node that represents a try catch block.
 *
 * @author Eric Bruneton
 * @author Mouse0w0 (modify)
 */
public class BtTryCatchBlock extends BtNode {

    /**
     * The beginning of the exception handler's scope (inclusive).
     */
    public BtLabel start;

    /**
     * The end of the exception handler's scope (exclusive).
     */
    public BtLabel end;

    /**
     * The beginning of the exception handler's code.
     */
    public BtLabel handler;

    /**
     * The internal name of the type of exceptions handled by the handler. May be {@literal null} to
     * catch any exceptions (for "finally" blocks).
     */
    public String type;

    /**
     * The runtime visible type annotations on the exception handler type. May be {@literal null}.
     */
    public List<TypeAnnotationNode> visibleTypeAnnotations;

    /**
     * The runtime invisible type annotations on the exception handler type. May be {@literal null}.
     */
    public List<TypeAnnotationNode> invisibleTypeAnnotations;

    /**
     * Constructs a new {@link TryCatchBlockNode}.
     *
     * @param start   the beginning of the exception handler's scope (inclusive).
     * @param end     the end of the exception handler's scope (exclusive).
     * @param handler the beginning of the exception handler's code.
     * @param type    the internal name of the type of exceptions handled by the handler, or {@literal
     *                null} to catch any exceptions (for "finally" blocks).
     */
    public BtTryCatchBlock(
            final BtLabel start, final BtLabel end, final BtLabel handler, final String type) {
        this.start = start;
        this.end = end;
        this.handler = handler;
        this.type = type;
    }

    /**
     * Updates the index of this try catch block in the method's list of try catch block nodes. This
     * index maybe stored in the 'target' field of the type annotations of this block.
     *
     * @param index the new index of this try catch block in the method's list of try catch block
     *              nodes.
     */
    public void updateIndex(final int index) {
        int newTypeRef = 0x42000000 | (index << 8);
        if (visibleTypeAnnotations != null) {
            for (int i = 0, n = visibleTypeAnnotations.size(); i < n; ++i) {
                visibleTypeAnnotations.get(i).typeRef = newTypeRef;
            }
        }
        if (invisibleTypeAnnotations != null) {
            for (int i = 0, n = invisibleTypeAnnotations.size(); i < n; ++i) {
                invisibleTypeAnnotations.get(i).typeRef = newTypeRef;
            }
        }
    }

    /**
     * Makes the given visitor visit this try catch block.
     *
     * @param methodVisitor a method visitor.
     */
    public void accept(final MethodVisitor methodVisitor, final Evaluator evaluator) {
        methodVisitor.visitTryCatchBlock(
                start.getLabel(), end.getLabel(), handler == null ? null : handler.getLabel(), type);
        if (visibleTypeAnnotations != null) {
            for (int i = 0, n = visibleTypeAnnotations.size(); i < n; ++i) {
                TypeAnnotationNode typeAnnotation = visibleTypeAnnotations.get(i);
                typeAnnotation.accept(
                        methodVisitor.visitTryCatchAnnotation(
                                typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, true));
            }
        }
        if (invisibleTypeAnnotations != null) {
            for (int i = 0, n = invisibleTypeAnnotations.size(); i < n; ++i) {
                TypeAnnotationNode typeAnnotation = invisibleTypeAnnotations.get(i);
                typeAnnotation.accept(
                        methodVisitor.visitTryCatchAnnotation(
                                typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, false));
            }
        }
    }
}

package com.github.mouse0w0.coffeemaker.template.tree.insn;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.tree.BtObject;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.List;
import java.util.Map;

public abstract class BtInsnNode extends BtObject {
    /**
     * The previous instruction in the list to which this instruction belongs.
     */
    BtInsnNode previousInsn;
    /**
     * The next instruction in the list to which this instruction belongs.
     */
    BtInsnNode nextInsn;
    /**
     * The index of this instruction in the list to which it belongs. The value of this field is
     * correct only when {@link BtInsnList#cache} is not null. A value of -1 indicates that this
     * instruction does not belong to any {@link BtInsnList}.
     */
    int index;

    public BtInsnNode() {
        this.index = -1;
    }

    public BtLabel getPreviousLabel() {
        BtInsnNode current = this;
        while (!(current instanceof BtLabel) && current != null) {
            current = current.getPrevious();
        }
        return (BtLabel) current;
    }

    public BtLabel getNextLabel() {
        BtInsnNode current = this.getNext();
        while (!(current instanceof BtLabel) && current != null) {
            current = current.getNext();
        }
        return (BtLabel) current;
    }

    public boolean isConstant() {
        return false;
    }

    public int getAsInt() {
        throw new UnsupportedOperationException("constant");
    }

    public String getAsString() {
        throw new UnsupportedOperationException("constant");
    }

    public Type getAsType() {
        throw new UnsupportedOperationException("constant");
    }

    /**
     * Returns the clone of the given label.
     *
     * @param label        a label.
     * @param clonedLabels a map from LabelNodes to cloned LabelNodes.
     * @return the clone of the given label.
     */
    static BtLabel clone(final BtLabel label, final Map<BtLabel, BtLabel> clonedLabels) {
        return clonedLabels.get(label);
    }

    /**
     * Returns the clones of the given labels.
     *
     * @param labels       a list of labels.
     * @param clonedLabels a map from LabelNodes to cloned LabelNodes.
     * @return the clones of the given labels.
     */
    static BtLabel[] clone(
            final List<BtLabel> labels, final Map<BtLabel, BtLabel> clonedLabels) {
        BtLabel[] clones = new BtLabel[labels.size()];
        for (int i = 0, n = clones.length; i < n; ++i) {
            clones[i] = clonedLabels.get(labels.get(i));
        }
        return clones;
    }

    /**
     * Returns the previous instruction in the list to which this instruction belongs, if any.
     *
     * @return the previous instruction in the list to which this instruction belongs, if any. May be
     * {@literal null}.
     */
    public BtInsnNode getPrevious() {
        return previousInsn;
    }

    /**
     * Returns the next instruction in the list to which this instruction belongs, if any.
     *
     * @return the next instruction in the list to which this instruction belongs, if any. May be
     * {@literal null}.
     */
    public BtInsnNode getNext() {
        return nextInsn;
    }

    /**
     * Makes the given method visitor visit this instruction.
     *
     * @param methodVisitor a method visitor.
     */
    public abstract void accept(MethodVisitor methodVisitor, Evaluator evaluator);

    /**
     * Returns a copy of this instruction.
     *
     * @param clonedLabels a map from LabelNodes to cloned LabelNodes.
     * @return a copy of this instruction. The returned instruction does not belong to any {@link
     * BtInsnList}.
     */
    public abstract BtInsnNode clone(Map<BtLabel, BtLabel> clonedLabels);

}

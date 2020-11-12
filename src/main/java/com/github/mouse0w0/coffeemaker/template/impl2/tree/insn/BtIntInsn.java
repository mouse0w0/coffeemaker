// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
//    contributors may be used to endorse or promote products derived from
//    this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
// THE POSSIBILITY OF SUCH DAMAGE.
package com.github.mouse0w0.coffeemaker.template.impl2.tree.insn;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtNode;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;

/**
 * A node that represents an instruction with a single int operand.
 *
 * @author Eric Bruneton
 * @author Mouse0w0 (modify)
 */
public class BtIntInsn extends BtInsnBase {

    /**
     * The operand of this instruction.
     */
    public static final String OPERAND = "operand";

    /**
     * Constructs a new {@link BtIntInsn}.
     *
     * @param opcode  the opcode of the instruction to be constructed. This opcode must be BIPUSH,
     *                SIPUSH or NEWARRAY.
     * @param operand the operand of the instruction to be constructed.
     */
    public BtIntInsn(final int opcode, final int operand) {
        super(opcode);
        putValue(OPERAND, operand);
    }

    private BtIntInsn(final int opcode, final BtNode operand) {
        super(opcode);
        put(OPERAND, operand);
    }

    /**
     * Sets the opcode of this instruction.
     *
     * @param opcode the new instruction opcode. This opcode must be BIPUSH, SIPUSH or NEWARRAY.
     */
    public void setOpcode(final int opcode) {
        this.opcode = opcode;
    }

    @Override
    public int getType() {
        return INT_INSN;
    }

    @Override
    public void accept(final MethodVisitor methodVisitor, final Evaluator evaluator) {
        methodVisitor.visitIntInsn(opcode, computeInt(OPERAND, evaluator));
        acceptAnnotations(methodVisitor);
    }

    @Override
    public BtInsnBase clone(final Map<BtLabel, BtLabel> clonedLabels) {
        return new BtIntInsn(opcode, get(OPERAND)).cloneAnnotations(this);
    }
}

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

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtNode;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Map;

/**
 * A node that represents an LDC instruction.
 *
 * @author Eric Bruneton
 * @author Mouse0w0 (modify)
 */
public class BtLdcInsn extends BtInsnBase {

    public static final String CST = "cst";

    /**
     * Constructs a new {@link BtLdcInsn}.
     *
     * @param value the constant to be loaded on the stack. This parameter must be a non null {@link
     *              Integer}, a {@link Float}, a {@link Long}, a {@link Double} or a {@link String}.
     */
    public BtLdcInsn(final Object value) {
        super(Opcodes.LDC);
        putValue(CST, value);
    }

    private BtLdcInsn(final BtNode value) {
        super(Opcodes.LDC);
        put(CST, value);
    }

    @Override
    public int getType() {
        return LDC_INSN;
    }

    @Override
    public void accept(final MethodVisitor methodVisitor, final Evaluator evaluator) {
        methodVisitor.visitLdcInsn(compute(CST, evaluator));
        acceptAnnotations(methodVisitor);
    }

    @Override
    public BtInsnBase clone(final Map<BtLabel, BtLabel> clonedLabels) {
        return new BtLdcInsn(get(CST)).cloneAnnotations(this);
    }
}

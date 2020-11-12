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
import com.github.mouse0w0.coffeemaker.template.impl2.tree.util.SmartList;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;
import java.util.Map;

/**
 * A node that represents a TABLESWITCH instruction.
 *
 * @author Eric Bruneton
 * @author Mouse0w0 (modify)
 */
public class BtTableSwitchInsn extends BtInsnBase {

    /**
     * The minimum key value.
     */
    public int min;

    /**
     * The maximum key value.
     */
    public int max;

    /**
     * Beginning of the default handler block.
     */
    public BtLabel dflt;

    /**
     * Beginnings of the handler blocks. This list is a list of {@link BtLabel} objects.
     */
    public List<BtLabel> labels;

    /**
     * Constructs a new {@link BtTableSwitchInsn}.
     *
     * @param min    the minimum key value.
     * @param max    the maximum key value.
     * @param dflt   beginning of the default handler block.
     * @param labels beginnings of the handler blocks. {@code labels[i]} is the beginning of the
     *               handler block for the {@code min + i} key.
     */
    public BtTableSwitchInsn(
            final int min, final int max, final BtLabel dflt, final BtLabel... labels) {
        super(Opcodes.TABLESWITCH);
        this.min = min;
        this.max = max;
        this.dflt = dflt;
        this.labels = new SmartList<>(labels);
    }

    @Override
    public int getType() {
        return TABLESWITCH_INSN;
    }

    @Override
    public void accept(final MethodVisitor methodVisitor, final Evaluator evaluator) {
        Label[] labelsArray = new Label[this.labels.size()];
        for (int i = 0, n = labelsArray.length; i < n; ++i) {
            labelsArray[i] = this.labels.get(i).getLabel();
        }
        methodVisitor.visitTableSwitchInsn(min, max, dflt.getLabel(), labelsArray);
        acceptAnnotations(methodVisitor);
    }

    @Override
    public BtInsnBase clone(final Map<BtLabel, BtLabel> clonedLabels) {
        return new BtTableSwitchInsn(min, max, clone(dflt, clonedLabels), clone(labels, clonedLabels))
                .cloneAnnotations(this);
    }
}

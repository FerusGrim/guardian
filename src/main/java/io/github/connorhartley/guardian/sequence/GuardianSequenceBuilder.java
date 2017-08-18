/*
 * MIT License
 *
 * Copyright (c) 2017 Connor Hartley
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.connorhartley.guardian.sequence;

import com.ichorpowered.guardian.api.detection.check.Check;
import com.ichorpowered.guardian.api.entry.EntityEntry;
import com.ichorpowered.guardian.api.sequence.Sequence;
import com.ichorpowered.guardian.api.sequence.SequenceBlueprint;
import com.ichorpowered.guardian.api.sequence.SequenceBuilder;
import com.ichorpowered.guardian.api.sequence.action.Action;
import com.ichorpowered.guardian.api.sequence.action.ActionBlueprint;
import com.ichorpowered.guardian.api.sequence.action.ActionBuilder;
import io.github.connorhartley.guardian.sequence.action.GuardianAction;
import io.github.connorhartley.guardian.sequence.action.GuardianActionBuilder;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GuardianSequenceBuilder<E, F> implements SequenceBuilder<E, F> {

    private Collection<GuardianAction> actions = Collections.emptyList();

    @Nonnull
    @Override
    public <T> ActionBuilder<E, F, T> action(@Nullable Class<T> aClass) {
        return this.action(new GuardianAction<>(aClass));
    }

    @Nonnull
    @Override
    public <T> ActionBuilder<E, F, T> action(@Nullable ActionBlueprint<T> actionBlueprint) {
        return this.action(actionBlueprint.create());
    }

    @Nonnull
    @Override
    public <T> ActionBuilder<E, F, T> action(@Nullable Action<T> action) {
        this.actions.add(GuardianAction.of(action));

        return new GuardianActionBuilder<>(this, action);
    }

    @Nonnull
    @Override
    public SequenceBlueprint<E, F> build(Check<E, F> check) {
        return new AbstractSequenceBlueprint<E, F>(check) {
            @Nonnull
            @Override
            public Sequence<E, F> create(EntityEntry entry) {
                return new GuardianSequence<>(entry, this, check, GuardianSequenceBuilder.this.actions, null);
            }
        };
    }

}


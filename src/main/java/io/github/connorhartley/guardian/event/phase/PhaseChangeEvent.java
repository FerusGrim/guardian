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
package io.github.connorhartley.guardian.event.phase;

import com.google.common.reflect.TypeToken;
import com.ichorpowered.guardian.api.detection.Detection;
import com.ichorpowered.guardian.api.detection.DetectionConfiguration;
import com.ichorpowered.guardian.api.event.GuardianEvent;
import com.ichorpowered.guardian.api.event.origin.Origin;
import com.ichorpowered.guardian.api.phase.PhaseViewer;
import net.kyori.lunar.reflect.Reified;

import javax.annotation.Nonnull;

public class PhaseChangeEvent<T> implements GuardianEvent, Reified<T> {

    private final Detection detection;
    private final PhaseViewer<T> phaseViewer;
    private final Class<T> phaseClass;
    private final Origin origin;


    public PhaseChangeEvent(Detection detection, PhaseViewer<T> phaseViewer, Class<T> phaseClass, Origin origin) {
        this.detection = detection;
        this.phaseViewer = phaseViewer;
        this.phaseClass = phaseClass;
        this.origin = origin;
    }

    @Nonnull
    @Override
    public TypeToken<T> type() {
        return TypeToken.of(this.phaseClass);
    }

    public <E, F extends DetectionConfiguration> Detection<E, F> getDetection() {
        return this.detection;
    }

    public PhaseViewer<T> getPhaseViewer() {
        return this.phaseViewer;
    }

    public Class<T> getPhaseClass() {
        return this.phaseClass;
    }

    @Override
    public Origin getOrigin() {
        return this.origin;
    }
}

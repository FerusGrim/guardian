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
package com.ichorpowered.guardian.common.detection;

import com.google.inject.Inject;
import com.ichorpowered.guardian.GuardianPlugin;
import com.ichorpowered.guardian.content.AbstractContentContainer;
import com.ichorpowered.guardian.detection.AbstractDetection;
import com.ichorpowered.guardian.detection.AbstractDetectionContentLoader;
import com.ichorpowered.guardian.util.property.Property;
import com.ichorpowered.guardianapi.content.ContentContainer;
import com.ichorpowered.guardianapi.content.transaction.ContentKey;
import com.ichorpowered.guardianapi.detection.DetectionBuilder;
import com.ichorpowered.guardianapi.detection.DetectionContentLoader;
import com.ichorpowered.guardianapi.detection.DetectionManager;
import com.ichorpowered.guardianapi.detection.check.CheckModel;
import com.ichorpowered.guardianapi.detection.heuristic.HeuristicModel;
import com.ichorpowered.guardianapi.detection.penalty.PenaltyModel;
import com.ichorpowered.guardianapi.detection.stage.StageCycle;
import com.me4502.modularframework.module.Module;
import com.me4502.modularframework.module.guice.ModuleContainer;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

@Module(id = "fly",
        name = "Fly Detection",
        authors = { "Connor Hartley (vectrix)" },
        version = "0.2.0",
        onEnable = "onConstruction",
        onDisable = "onDeconstruction"
)
public class FlightDetection extends AbstractDetection {

    private static String MODULE_ID = FlightDetection.class.getAnnotation(Module.class).id();
    private static String MODULE_NAME = FlightDetection.class.getAnnotation(Module.class).name();

    private final GuardianPlugin plugin;

    @Property private String id;
    @Property private String name;
    @Property private StageCycle stageCycle;
    @Property private ContentContainer contentContainer;
    @Property private DetectionContentLoader contentLoader;

    @Inject
    public FlightDetection(@ModuleContainer PluginContainer pluginContainer) {
        super(FlightDetection.MODULE_ID, FlightDetection.MODULE_NAME);

        this.plugin = (GuardianPlugin) pluginContainer.getInstance().orElse(null);
    }

    @Override
    public void onConstruction() {}

    @Override
    public void onDeconstruction() {}

    @Override
    public void onLoad() {
        this.contentLoader.set(this.contentContainer);
    }

    @Override
    public DetectionManager register(final DetectionManager detectionManager) {
        final Optional<DetectionBuilder> detectionBuilder = detectionManager.provider(FlightDetection.class);
        if (!detectionBuilder.isPresent()) return detectionManager;

        return detectionBuilder.get()
                .id(FlightDetection.MODULE_ID)
                .name(FlightDetection.MODULE_NAME)
                .stage(CheckModel.class)
                    .min(1)
                    .max(99)
                    .append()
                .stage(HeuristicModel.class)
                    .min(1)
                    .max(99)
                    .append()
                .stage(PenaltyModel.class)
                    .min(1)
                    .max(99)
                    .append()
                .contentLoader(new FlightContentLoader(this, this.plugin.getConfigDirectory()))
                .content(new FlightContent(this))
                .submit(this.plugin);
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogger();
    }

    @Override
    public StageCycle getStageCycle() {
        return this.stageCycle;
    }

    @Override
    public ContentContainer getContentContainer() {
        return this.contentContainer;
    }

    @Override
    public DetectionContentLoader getContentLoader() {
        return this.contentLoader;
    }

    @Override
    public Object getPlugin() {
        return this.plugin;
    }

    public static class FlightContent extends AbstractContentContainer {

        private final FlightDetection detection;

        public FlightContent(final FlightDetection detection) {
            this.detection = detection;
        }

        @Override
        public Set<ContentKey> getPossibleKeys() {
            return null;
        }

    }

    public static class FlightContentLoader extends AbstractDetectionContentLoader<FlightDetection> {

        public FlightContentLoader(final FlightDetection detection,
                                   final Path configurationDirectory) {
            super(detection, configurationDirectory);
        }

        @Override
        public String getRoot() {
            return "fly.conf";
        }

    }
}

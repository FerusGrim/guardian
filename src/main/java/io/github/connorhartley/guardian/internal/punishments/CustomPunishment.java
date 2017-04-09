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
package io.github.connorhartley.guardian.internal.punishments;

import io.github.connorhartley.guardian.Guardian;
import io.github.connorhartley.guardian.data.DataKeys;
import io.github.connorhartley.guardian.detection.Detection;
import io.github.connorhartley.guardian.punishment.Punishment;
import io.github.connorhartley.guardian.punishment.PunishmentType;
import io.github.connorhartley.guardian.storage.container.StorageValue;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CustomPunishment implements PunishmentType {

    private final Guardian plugin;
    private final Detection detection;

    public CustomPunishment(Guardian plugin, Detection detection) {
        this.plugin = plugin;
        this.detection = detection;
    }

    @Override
    public String getName() {
        return "custom";
    }

    @Override
    public Optional<Detection> getDetection() {
        if (this.detection == null) return Optional.empty();
        return Optional.of(this.detection);
    }

    @Override
    public boolean handle(String[] args, User user, Punishment punishment) {
        List<PunishmentType> punishmentTypes = new ArrayList<>();
        if (user.get(DataKeys.GUARDIAN_PUNISHMENT_TAG).isPresent()) {
            punishmentTypes.addAll(user.get(DataKeys.GUARDIAN_PUNISHMENT_TAG).get());
        }

        punishmentTypes.add(this);

        user.offer(DataKeys.GUARDIAN_PUNISHMENT_TAG, punishmentTypes);

        Optional<StorageValue<String, HashMap<String, List<String>>>> detectionCustomPunishments =
                this.detection.getConfiguration().get("custom-punishments", new HashMap<String, List<String>>());

        List<String> customCommandList;

        if (detectionCustomPunishments.isPresent()) {
            if (!detectionCustomPunishments.get().getValue().isEmpty() && detectionCustomPunishments.get().getValue()
                    .get(args[0]) != null) {
                customCommandList = detectionCustomPunishments.get().getValue().get(args[0]);
            } else {
                return false;
            }
        } else {
            return false;
        }

        for (String command : customCommandList) {
            String commandModified = command.replace("%player%", user.getName())
                    .replace("%probability%", punishment.getProbability().toString())
                    .replace("%detection%", this.detection.getName());

            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), commandModified);
        }

        return true;
    }
}
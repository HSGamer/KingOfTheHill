package me.hsgamer.kingofthehill.feature;

import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.ArenaFeature;
import me.hsgamer.minigamecore.base.Feature;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.System.currentTimeMillis;

public class CooldownFeature extends ArenaFeature<CooldownFeature.ArenaCooldownFeature> {

    @Override
    protected ArenaCooldownFeature createFeature(Arena arena) {
        return new ArenaCooldownFeature();
    }

    public static class ArenaCooldownFeature implements Feature {
        public final AtomicLong cooldown = new AtomicLong(currentTimeMillis());

        public long get(TimeUnit timeUnit) {
            long currentTime = currentTimeMillis();
            long endTime = cooldown.get();
            long duration = Math.max(0, endTime - currentTime);
            return timeUnit.convert(duration, TimeUnit.MILLISECONDS);
        }

        public void start(long duration, TimeUnit timeUnit) {
            long durationMillis = timeUnit.toMillis(duration);
            long currentTime = currentTimeMillis();
            cooldown.lazySet(currentTime + durationMillis);
        }

        @Override
        public void clear() {
            cooldown.lazySet(0);
        }
    }
}

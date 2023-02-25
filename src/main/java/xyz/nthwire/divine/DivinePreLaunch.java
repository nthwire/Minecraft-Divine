package xyz.nthwire.divine;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.state.property.IntProperty;

public class DivinePreLaunch implements PreLaunchEntrypoint {
    public static final ThreadLocal<Integer> VINE_AGE_BATON = ThreadLocal.withInitial(() -> -1);
    public static final int MAX_VINE_AGE = 127;
    public static final IntProperty VINE_AGE = IntProperty.of("age", 0, MAX_VINE_AGE);

    @Override
    public void onPreLaunch() {
        MixinExtrasBootstrap.init();
    }
}

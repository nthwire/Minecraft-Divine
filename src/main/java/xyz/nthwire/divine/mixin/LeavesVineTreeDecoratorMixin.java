package xyz.nthwire.divine.mixin;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.treedecorator.LeavesVineTreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static xyz.nthwire.divine.DivinePreLaunch.MAX_VINE_AGE;
import static xyz.nthwire.divine.DivinePreLaunch.VINE_AGE_BATON;

@Mixin(LeavesVineTreeDecorator.class)
public abstract class LeavesVineTreeDecoratorMixin extends TreeDecorator {
    @Inject(method = "placeVines", at = @At("HEAD"))
    private static void initAgeBaton(BlockPos pos, BooleanProperty faceProperty, Generator generator, CallbackInfo ci) {
        int slack = generator.getRandom().nextInt(4);
        int initialAge = MAX_VINE_AGE - slack - 4;
        VINE_AGE_BATON.set(initialAge);
    }

    @Inject(method = "placeVines", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;down()Lnet/minecraft/util/math/BlockPos;"))
    private static void updateAgeBaton(BlockPos pos, BooleanProperty faceProperty, Generator generator, CallbackInfo ci) {
        VINE_AGE_BATON.set(VINE_AGE_BATON.get() + 1);
    }

    @Inject(method = "placeVines", at = @At("RETURN"))
    private static void clearAgeBaton(BlockPos pos, BooleanProperty faceProperty, Generator generator, CallbackInfo ci) {
        VINE_AGE_BATON.set(-1);
    }
}

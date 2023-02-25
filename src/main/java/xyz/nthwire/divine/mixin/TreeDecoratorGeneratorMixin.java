package xyz.nthwire.divine.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static xyz.nthwire.divine.DivinePreLaunch.*;

@Mixin(TreeDecorator.Generator.class)
public abstract class TreeDecoratorGeneratorMixin {
    @Shadow public abstract Random getRandom();

    @ModifyExpressionValue(method = "replaceWithVine", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDefaultState()Lnet/minecraft/block/BlockState;"))
    private BlockState usePassedAge(BlockState original) {
        int ageBaton = VINE_AGE_BATON.get();
        return original.with(VINE_AGE, ageBaton < 0 ? MAX_VINE_AGE : ageBaton);
    }
}

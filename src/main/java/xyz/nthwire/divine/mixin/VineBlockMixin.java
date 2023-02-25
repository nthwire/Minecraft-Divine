package xyz.nthwire.divine.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.State;
import net.minecraft.state.StateManager;
import net.minecraft.structure.JungleTempleGenerator;
import net.minecraft.structure.RuinedPortalStructurePiece;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.structure.RuinedPortalStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static xyz.nthwire.divine.DivinePreLaunch.MAX_VINE_AGE;
import static xyz.nthwire.divine.DivinePreLaunch.VINE_AGE;

@Mixin(VineBlock.class)
public abstract class VineBlockMixin extends Block {
    public VineBlockMixin(Settings settings) {
        super(settings);

        /* TODO: fix generation *everywhere* */
        RuinedPortalStructurePiece
    }

    @ModifyExpressionValue(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/state/StateManager;getDefaultState()Lnet/minecraft/state/State;"))
    private State ageZeroInDefaultState(State original) {
        return (State) original.with(VINE_AGE, 0);
    }

    @Inject(method = "appendProperties", at = @At("TAIL"))
    private void appendAgeProperty(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(VINE_AGE);
    }

    @ModifyReturnValue(method = "getPlacementState", at = @At("RETURN"))
    private BlockState placeWithRandomAge(BlockState original, ItemPlacementContext ctx) {
        return original.with(VINE_AGE, ctx.getWorld().getRandom().nextInt(MAX_VINE_AGE));
    }

    @WrapOperation(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    private int stopGrowthWhenOld(Random instance, int bound, Operation<Integer> original, BlockState state) {
        return state.get(VINE_AGE) >= MAX_VINE_AGE ? -1 : original.call(instance, bound);
    }

    @WrapOperation(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private boolean ageWhenGrowing(ServerWorld instance, BlockPos blockPos, BlockState newState, int i, Operation<Boolean> original, BlockState currentState) {
        int newAge = Math.min(MAX_VINE_AGE, currentState.get(VINE_AGE) + 1);
        return original.call(instance, blockPos, newState.with(VINE_AGE, newAge), i);
    }
}

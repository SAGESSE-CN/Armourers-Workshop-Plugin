package moe.plushie.armourers_workshop.plugin.mixin;

import moe.plushie.armourers_workshop.plugin.annotation.Available;
import moe.plushie.armourers_workshop.plugin.api.IOriginalLevelChunk;
import moe.plushie.armourers_workshop.plugin.helper.BlockHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Available("[1.19, )")
@Mixin(ClientLevel.class)
public class BlockStateUpdateMixin {

    @ModifyVariable(method = "setServerVerifiedBlockState", at = @At("HEAD"), argsOnly = true)
    private BlockState aw$setServerVerifiedBlockState(BlockState blockState, BlockPos pos, BlockState arg2, int i) {
        ClientLevel level = ClientLevel.class.cast(this);
        ChunkAccess chunk = level.getChunk(pos);
        if (!(chunk instanceof IOriginalLevelChunk)) {
            return blockState;
        }
        IOriginalLevelChunk originalChunk = (IOriginalLevelChunk) chunk;
        BlockState originalState = originalChunk.getOriginalBlockState(pos);
        if (originalState == null) {
            return blockState;
        }
        // because we can't get the real block state from this event,
        // so we will keep current block state when the block type not changes.
        if (BlockHelper.isPlayerHead(blockState) && BlockHelper.isPlayerHead(originalState)) {
            originalChunk.setOriginalBlockState(pos, blockState);
            return level.getBlockState(pos);
        }
        // reset all data
        originalChunk.setOriginalBlockState(pos, null);
        return blockState;
    }

}

package moe.plushie.armourers_workshop.plugin.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import moe.plushie.armourers_workshop.plugin.annotation.Available;
import moe.plushie.armourers_workshop.plugin.api.IOriginalLevelChunk;
import moe.plushie.armourers_workshop.plugin.helper.BlockHelper;
import moe.plushie.armourers_workshop.plugin.helper.GameProfileHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

@Available("[1.18, )")
@Mixin(LevelChunk.class)
public class BlockLoadMixin implements IOriginalLevelChunk {

    private HashMap<BlockPos, BlockState> aw$originalBlockStates;

    @Inject(method = "replaceWithPacketData", at = @At("RETURN"))
    public void aw$loadOwner(FriendlyByteBuf arg, CompoundTag arg2, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> consumer, CallbackInfo ci) {
        LevelChunk chunk = LevelChunk.class.cast(this);
        ArrayList<Runnable> pending = new ArrayList<>();
        chunk.getBlockEntities().forEach((pos, blockEntity) -> {
            if (!(blockEntity instanceof SkullBlockEntity)) {
                return;
            }
            GameProfile profile = ((SkullBlockEntity) blockEntity).getOwnerProfile();
            Pair<BlockState, CompoundTag> pair = BlockHelper.getBlockFromTexture(GameProfileHelper.getTextureFromProfile(profile));
            if (pair == null) {
                return;
            }
            // replace the state.
            pending.add(() -> {
                BlockState newState = pair.getFirst();
                CompoundTag newTag = pair.getSecond();
                setOriginalBlockState(pos, Blocks.PLAYER_HEAD.defaultBlockState());
                chunk.setBlockState(pos, newState, false);
                if (newTag != null && !newState.hasBlockEntity()) {
                    BlockEntity blockEntity1 = chunk.getBlockEntity(pos);
                    if (blockEntity1 != null) {
                        blockEntity1.handleUpdateTag(newTag);
                    }
                }
            });
        });
        try {
            pending.forEach(Runnable::run);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOriginalBlockState(BlockPos pos, BlockState blockState) {
        if (aw$originalBlockStates == null) {
            aw$originalBlockStates = new HashMap<>();
        }
        aw$originalBlockStates.put(pos, blockState);
    }

    @Override
    public BlockState getOriginalBlockState(BlockPos pos) {
        if (aw$originalBlockStates != null) {
            return aw$originalBlockStates.get(pos);
        }
        return null;
    }
}

package moe.plushie.armourers_workshop.plugin.api;

import moe.plushie.armourers_workshop.plugin.api.state.StateDefinition;
import net.querz.nbt.tag.CompoundTag;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Block {

    private ResourceLocation key;

    private final Properties properties;

    protected final StateDefinition<Block, BlockState> stateDefinition;
    private BlockState defaultBlockState;

    public Block(Properties properties) {
        this.properties = properties;
        StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(builder);
        this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        this.registerDefaultState(this.stateDefinition.any());
    }

    public InteractionResult use(BlockState blockState, World world, BlockPos blockPos, Player player, EquipmentSlot hand) {
        return InteractionResult.PASS;
    }

    public InteractionResult attack(BlockState blockState, World world, BlockPos blockPos, Player player) {
        return InteractionResult.PASS;
    }

    public boolean canSurvive(BlockState blockState, World world, BlockPos blockPos) {
        return true;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    }

    @Nullable
    public BlockEntity createBlockEntity(World world, BlockPos pos, BlockState blockState, @Nullable CompoundTag tag) {
        return new BlockEntity(world, pos, blockState, tag);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState();
    }

    public void setKey(ResourceLocation key) {
        this.key = key;
    }

    public ResourceLocation getKey() {
        return key;
    }

    public void registerDefaultState(BlockState defaultBlockState) {
        this.defaultBlockState = defaultBlockState;
    }

    public BlockState defaultBlockState() {
        return defaultBlockState;
    }

    @Nullable
    public Material asMaterial() {
        return properties.material;
    }

    public boolean isInteractable() {
        if (properties.material != null) {
            return properties.material.isInteractable();
        }
        return properties.isInteractable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block block = (Block) o;
        return Objects.equals(key, block.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    public static class Properties {

        Material material;
        boolean isInteractable = false;
        boolean noDrops = false;
        boolean noOcclusion = false;
        boolean noCollission = false;

        public Properties noDrops() {
            this.noDrops = true;
            return this;
        }

        public Properties noOcclusion() {
            this.noOcclusion = true;
            return this;
        }

        public Properties noCollission() {
            this.noCollission = true;
            return this;
        }

        public Properties isInteractable() {
            this.isInteractable = true;
            return this;
        }

        public Properties material(Material material) {
            this.material = material;
            return this;
        }
    }
}

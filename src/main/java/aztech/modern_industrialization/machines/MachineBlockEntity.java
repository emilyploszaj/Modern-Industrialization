/*
 * MIT License
 *
 * Copyright (c) 2020 Azercoco & Technici4n
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
package aztech.modern_industrialization.machines;

import aztech.modern_industrialization.api.FastBlockEntity;
import aztech.modern_industrialization.api.WrenchableBlockEntity;
import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.inventory.MIInventory;
import aztech.modern_industrialization.machines.components.OrientationComponent;
import aztech.modern_industrialization.machines.components.PlacedByComponent;
import aztech.modern_industrialization.machines.gui.GuiComponent;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.gui.MachineMenuServer;
import aztech.modern_industrialization.machines.models.MachineModelClientData;
import aztech.modern_industrialization.util.NbtHelper;
import aztech.modern_industrialization.util.WorldHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.Nullable;

/**
 * The base block entity for the machine system. Contains components, and an
 * inventory.
 */
@SuppressWarnings("rawtypes")
public abstract class MachineBlockEntity extends FastBlockEntity
        implements ExtendedScreenHandlerFactory, RenderAttachmentBlockEntity, WrenchableBlockEntity {
    public final List<GuiComponent.Server> guiComponents = new ArrayList<>();
    private final List<IComponent> icomponents = new ArrayList<>();
    public final MachineGuiParameters guiParams;
    /**
     * Server-side only: true if the next call to sync() will trigger a remesh.
     */
    private boolean syncCausesRemesh = true;

    public final OrientationComponent orientation;
    public final PlacedByComponent placedBy;

    public MachineBlockEntity(BEP bep, MachineGuiParameters guiParams, OrientationComponent.Params orientationParams) {
        super(bep.type(), bep.pos(), bep.state());
        this.guiParams = guiParams;
        this.orientation = new OrientationComponent(orientationParams);
        this.placedBy = new PlacedByComponent();

        registerComponents(orientation, placedBy);
    }

    protected final void registerGuiComponent(GuiComponent.Server component) {
        guiComponents.add(component);
    }

    protected final void registerComponents(IComponent... components) {
        Collections.addAll(icomponents, components);
    }

    /**
     * @return The inventory that will be synced with the client.
     */
    public abstract MIInventory getInventory();

    /**
     * @throws RuntimeException if the component doesn't exist.
     */
    @SuppressWarnings("unchecked")
    public <S extends GuiComponent.Server> S getComponent(ResourceLocation componentId) {
        for (GuiComponent.Server component : guiComponents) {
            if (component.getId().equals(componentId)) {
                return (S) component;
            }
        }
        throw new RuntimeException("Couldn't find component " + componentId);
    }

    @Override
    public final Component getDisplayName() {
        return Component.translatable("block.modern_industrialization." + guiParams.blockId);
    }

    @Override
    public final AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new MachineMenuServer(syncId, inv, this, guiParams);
    }

    @Override
    public final void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        // Write inventory
        MIInventory inv = getInventory();
        CompoundTag tag = new CompoundTag();
        NbtHelper.putList(tag, "items", inv.getItemStacks(), ConfigurableItemStack::toNbt);
        NbtHelper.putList(tag, "fluids", inv.getFluidStacks(), ConfigurableFluidStack::toNbt);
        buf.writeNbt(tag);
        // Write slot positions
        inv.itemPositions.write(buf);
        inv.fluidPositions.write(buf);
        buf.writeInt(guiComponents.size());
        // Write components
        for (GuiComponent.Server component : guiComponents) {
            buf.writeResourceLocation(component.getId());
            component.writeInitialData(buf);
        }
        // Write GUI params
        guiParams.write(buf);
    }

    /**
     * @param face The face that was targeted, taking the overlay into account.
     */
    protected InteractionResult onUse(Player player, InteractionHand hand, Direction face) {
        return InteractionResult.PASS;
    }

    protected abstract MachineModelClientData getModelData();

    @MustBeInvokedByOverriders
    public void onPlaced(LivingEntity placer, ItemStack itemStack) {
        orientation.onPlaced(placer, itemStack);
        placedBy.onPlaced(placer);
    }

    @Override
    public boolean useWrench(Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (orientation.useWrench(player, hand, MachineOverlay.findHitSide(hitResult))) {
            getLevel().blockUpdated(getBlockPos(), Blocks.AIR);
            setChanged();
            if (!getLevel().isClientSide()) {
                sync();
            }
            return true;
        }
        return false;
    }

    @Override
    public final Object getRenderAttachmentData() {
        return getModelData();
    }

    @Override
    public void sync() {
        sync(true);
    }

    public void sync(boolean forceRemesh) {
        syncCausesRemesh = syncCausesRemesh || forceRemesh;
        super.sync();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("remesh", syncCausesRemesh);
        syncCausesRemesh = false;
        for (IComponent component : icomponents) {
            component.writeClientNbt(tag);
        }
        return tag;
    }

    @Override
    public final void saveAdditional(CompoundTag tag) {
        for (IComponent component : icomponents) {
            component.writeNbt(tag);
        }
    }

    @Override
    public final void load(CompoundTag tag) {
        if (!tag.contains("remesh")) {
            for (IComponent component : icomponents) {
                component.readNbt(tag);
            }
        } else {
            boolean forceChunkRemesh = tag.getBoolean("remesh");
            for (IComponent component : icomponents) {
                component.readClientNbt(tag);
            }
            if (forceChunkRemesh) {
                WorldHelper.forceChunkRemesh(level, worldPosition);
            }
        }
    }

    @Override
    protected final boolean shouldSkipComparatorUpdate() {
        return !hasComparatorOutput();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void registerItemApi(BlockEntityType<?> bet) {
        ItemStorage.SIDED.registerForBlockEntities((be, direction) -> ((MachineBlockEntity) be).getInventory().itemStorage, bet);
    }

    public static void registerFluidApi(BlockEntityType<?> bet) {
        FluidStorage.SIDED.registerForBlockEntities((be, direction) -> ((MachineBlockEntity) be).getInventory().fluidStorage, bet);
    }

    public List<ItemStack> dropExtra() {
        return new ArrayList<>();
    }

    public List<Component> getTooltips() {
        return List.of();
    }

    protected boolean hasComparatorOutput() {
        return false;
    }

    protected int getComparatorOutput() {
        return 0;
    }
}

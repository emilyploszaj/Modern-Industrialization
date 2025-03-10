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
package aztech.modern_industrialization.machines.blockentities.multiblocks;

import static aztech.modern_industrialization.machines.multiblocks.HatchType.*;

import aztech.modern_industrialization.MIBlock;
import aztech.modern_industrialization.MIIdentifier;
import aztech.modern_industrialization.MIText;
import aztech.modern_industrialization.compat.megane.holder.EnergyListComponentHolder;
import aztech.modern_industrialization.compat.rei.machines.ReiMachineRecipes;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.components.*;
import aztech.modern_industrialization.machines.guicomponents.ShapeSelection;
import aztech.modern_industrialization.machines.guicomponents.SlotPanel;
import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.models.MachineCasings;
import aztech.modern_industrialization.machines.multiblocks.*;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import aztech.modern_industrialization.util.Simulation;
import java.util.*;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

// TODO: should the common part with ElectricCraftingMultiblockBlockEntity be refactored?
public class ElectricBlastFurnaceBlockEntity extends AbstractCraftingMultiblockBlockEntity implements EnergyListComponentHolder {

    private static final ShapeTemplate[] shapeTemplates;

    public ElectricBlastFurnaceBlockEntity(BEP bep) {
        super(bep, "electric_blast_furnace", new OrientationComponent.Params(false, false, false), shapeTemplates);
        this.upgrades = new UpgradeComponent();
        this.registerComponents(upgrades);
        registerGuiComponent(new SlotPanel.Server(this).withUpgrades(upgrades));

        registerGuiComponent(new ShapeSelection.Server(new ShapeSelection.Behavior() {
            @Override
            public void handleClick(int clickedLine, int delta) {
                activeShape.incrementShape(ElectricBlastFurnaceBlockEntity.this, delta);
            }

            @Override
            public int getCurrentIndex(int line) {
                return activeShape.getActiveShapeIndex();
            }
        }, new ShapeSelection.LineInfo(2, List.of(MIText.ShapeTextCupronickel.text(), MIText.ShapeTextKanthal.text()), true)));
    }

    @Override
    protected CrafterComponent.Behavior getBehavior() {
        return new Behavior();
    }

    private final List<EnergyComponent> energyInputs = new ArrayList<>();
    private final UpgradeComponent upgrades;

    @Override
    public List<EnergyComponent> getEnergyComponents() {
        return energyInputs;
    }

    @Override
    protected void onSuccessfulMatch(ShapeMatcher shapeMatcher) {
        energyInputs.clear();
        for (HatchBlockEntity hatch : shapeMatcher.getMatchedHatches()) {
            hatch.appendEnergyInputs(energyInputs);
        }
    }

    protected InteractionResult onUse(Player player, InteractionHand hand, Direction face) {
        InteractionResult result = super.onUse(player, hand, face);
        if (!result.consumesAction()) {
            result = upgrades.onUse(this, player, hand);
        }
        if (!result.consumesAction()) {
            result = LubricantHelper.onUse(this.crafter, player, hand);
        }
        return result;
    }

    @Override
    public List<ItemStack> dropExtra() {
        List<ItemStack> drops = super.dropExtra();
        drops.add(upgrades.getDrop());
        return drops;
    }

    private class Behavior implements CrafterComponent.Behavior {
        @Override
        public long consumeEu(long max, Simulation simulation) {
            long total = 0;

            for (EnergyComponent energyComponent : energyInputs) {
                total += energyComponent.consumeEu(max - total, simulation);
            }

            return total;
        }

        @Override
        public MachineRecipeType recipeType() {
            return MIMachineRecipeTypes.BLAST_FURNACE;
        }

        public boolean banRecipe(MachineRecipe recipe) {
            int index = activeShape.getActiveShapeIndex();
            return (recipe.eu > getMaxRecipeEu()) || (recipe.eu > coilsMaxBaseEU.get(coils.get(index)));
        }

        @Override
        public long getBaseRecipeEu() {
            return MachineTier.MULTIBLOCK.getBaseEu();
        }

        @Override
        public long getMaxRecipeEu() {
            return MachineTier.MULTIBLOCK.getMaxEu() + upgrades.getAddMaxEUPerTick();
        }

        @Override
        public Level getCrafterWorld() {
            return level;
        }

        @Override
        @Nullable
        public UUID getOwnerUuid() {
            return placedBy.placerId;
        }
    }

    public static void registerReiShapes() {
        for (ShapeTemplate shapeTemplate : shapeTemplates) {
            ReiMachineRecipes.registerMultiblockShape("electric_blast_furnace", shapeTemplate);
        }
    }

    public static final ArrayList<String> coilNames = new ArrayList<>();
    public static final ArrayList<String> coilEnglishNames = new ArrayList<>();
    public static final ArrayList<Block> coils = new ArrayList<>();
    public static final Map<Block, Long> coilsMaxBaseEU = new IdentityHashMap<>();

    static {
        coilNames.add("cupronickel_coil");
        coilNames.add("kanthal_coil");
        for (String coilName : coilNames) {
            coils.add(MIBlock.BLOCKS.get(new MIIdentifier(coilName)).asBlock());
        }
        coilEnglishNames.add(" (Cupronickel Tier)");
        coilEnglishNames.add(" (Kanthal Tier)");
        coilsMaxBaseEU.put(coils.get(0), 32L);
        coilsMaxBaseEU.put(coils.get(1), 128L);

        shapeTemplates = new ShapeTemplate[coils.size()];

        // Build shapes
        for (int i = 0; i < coils.size(); ++i) {
            SimpleMember invarCasings = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("heatproof_machine_casing")).asBlock());
            SimpleMember coilsBlocks = SimpleMember.forBlock(coils.get(i));
            HatchFlags ebfHatches = new HatchFlags.Builder().with(ITEM_INPUT, ITEM_OUTPUT, FLUID_INPUT, FLUID_OUTPUT, ENERGY_INPUT).build();
            ShapeTemplate ebfShape = new ShapeTemplate.Builder(MachineCasings.HEATPROOF).add3by3(0, invarCasings, false, ebfHatches)
                    .add3by3(1, coilsBlocks, true, null).add3by3(2, coilsBlocks, true, null).add3by3(3, invarCasings, false, ebfHatches).build();
            shapeTemplates[i] = ebfShape;
        }
    }
}

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
package aztech.modern_industrialization.machines.init;

import static aztech.modern_industrialization.machines.models.MachineCasings.CLEAN_STAINLESS_STEEL;
import static aztech.modern_industrialization.machines.multiblocks.HatchType.*;

import aztech.modern_industrialization.MIBlock;
import aztech.modern_industrialization.MIFluids;
import aztech.modern_industrialization.MIIdentifier;
import aztech.modern_industrialization.compat.kubejs.KubeJSProxy;
import aztech.modern_industrialization.compat.rei.machines.MachineCategoryParams;
import aztech.modern_industrialization.compat.rei.machines.ReiMachineRecipes;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.blockentities.multiblocks.*;
import aztech.modern_industrialization.machines.components.FluidConsumerComponent;
import aztech.modern_industrialization.machines.guicomponents.CraftingMultiblockGui;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import aztech.modern_industrialization.machines.multiblocks.*;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import aztech.modern_industrialization.util.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;

@SuppressWarnings("rawtypes")
public class MultiblockMachines {
    private static final Rectangle CRAFTING_GUI = new Rectangle(CraftingMultiblockGui.X, CraftingMultiblockGui.Y,
            CraftingMultiblockGui.W, CraftingMultiblockGui.H);
    // @formatter:off
    public static BlockEntityType COKE_OVEN;
    public static BlockEntityType STEAM_BLAST_FURNACE;
    public static BlockEntityType STEAM_QUARRY;
    public static BlockEntityType ELECTRIC_BLAST_FURNACE;
    public static BlockEntityType LARGE_STEAM_BOILER;
    public static BlockEntityType ADVANCED_LARGE_STEAM_BOILER;
    public static BlockEntityType HIGH_PRESSURE_LARGE_STEAM_BOILER;
    public static BlockEntityType HIGH_PRESSURE_ADVANCED_LARGE_STEAM_BOILER;
    public static BlockEntityType ELECTRIC_QUARRY;
    public static BlockEntityType OIL_DRILLING_RIG;
    public static BlockEntityType VACUUM_FREEZER;
    public static BlockEntityType DISTILLATION_TOWER;
    public static BlockEntityType LARGE_DIESEL_GENERATOR;
    public static BlockEntityType LARGE_STEAM_TURBINE;
    public static BlockEntityType HEAT_EXCHANGER;
    public static BlockEntityType PRESSURIZER;
    public static BlockEntityType IMPLOSION_COMPRESSOR;
    public static BlockEntityType NUCLEAR_REACTOR;
    public static BlockEntityType LARGE_TANK;
    public static BlockEntityType FUSION_REACTOR;
    public static BlockEntityType PLASMA_TURBINE;

    private static SimpleMember invarCasings;

    private static SimpleMember bronzePlatedBricks;
    private static SimpleMember bronzePipe;

    private static SimpleMember frostproofMachineCasing;

    private static SimpleMember stainlessSteelClean;
    private static SimpleMember stainlessSteelPipe;

    private static SimpleMember titaniumCasing;
    private static SimpleMember titaniumPipe;


    private static SimpleMember blastProofCasing;

    private static SimpleMember highlyAdvancedHull;
    private static SimpleMember fusionChamber;

    private static SimpleMember plasmaHandlingIridium;
    private static SimpleMember iridiumPipe;

    private static final HatchFlags fluidInputs = new HatchFlags.Builder().with(FLUID_INPUT).build();
    private static final HatchFlags energyOutput = new HatchFlags.Builder().with(ENERGY_OUTPUT).build();
    private static final HatchFlags energyInput = new HatchFlags.Builder().with(ENERGY_INPUT).build();


    private static void cokeOven() {
        SimpleMember bricks = SimpleMember.forBlock(Blocks.BRICKS);
        HatchFlags cokeOvenHatches = new HatchFlags.Builder().with(ITEM_INPUT).with(ITEM_OUTPUT).with(FLUID_INPUT).with(FLUID_OUTPUT).build();
        ShapeTemplate cokeOvenShape = new ShapeTemplate.Builder(MachineCasings.BRICKS).add3by3Levels(-1, 1, bricks, cokeOvenHatches).build();
        COKE_OVEN = MachineRegistrationHelper.registerMachine("Coke Oven", "coke_oven",
                bet -> new SteamCraftingMultiblockBlockEntity(bet, "coke_oven", cokeOvenShape, MIMachineRecipeTypes.COKE_OVEN));
        ReiMachineRecipes.registerMultiblockShape("coke_oven", cokeOvenShape);
    }

    private static void steamBlastFurnace() {
        SimpleMember fireclayBricks = SimpleMember.forBlock(MIBlock.BLOCK_FIRE_CLAY_BRICKS.asBlock());
        HatchFlags sbfHatches = new HatchFlags.Builder().with(ITEM_INPUT, ITEM_OUTPUT, FLUID_INPUT, FLUID_OUTPUT).build();
        ShapeTemplate sbfShape = new ShapeTemplate.Builder(MachineCasings.FIREBRICKS).add3by3Levels(-1, 2, fireclayBricks, sbfHatches).build();
        STEAM_BLAST_FURNACE = MachineRegistrationHelper.registerMachine("Steam Blast Furnace", "steam_blast_furnace",
                bet -> new SteamCraftingMultiblockBlockEntity(bet, "steam_blast_furnace", sbfShape, MIMachineRecipeTypes.BLAST_FURNACE));
        ReiMachineRecipes.registerMultiblockShape("steam_blast_furnace", sbfShape);
    }

    private static void electricBlastFurnace() {
        ELECTRIC_BLAST_FURNACE = MachineRegistrationHelper.registerMachine("Electric Blast Furnace", "electric_blast_furnace",
                ElectricBlastFurnaceBlockEntity::new);
        ElectricBlastFurnaceBlockEntity.registerReiShapes();
    }

    private static void steamBoilers() {

        HatchFlags slbHatchFlags = new HatchFlags.Builder().with(ITEM_INPUT, FLUID_INPUT, FLUID_OUTPUT).build();
        ShapeTemplate largeSteamBoilerShape = new ShapeTemplate.Builder(MachineCasings.HEATPROOF).add3by3(-1, invarCasings, false, slbHatchFlags)
                .add3by3(0, bronzePlatedBricks, true, null).add3by3(1, bronzePlatedBricks, true, null).add3by3(2, bronzePlatedBricks, false, null)
                .add(0, 0, 1, bronzePipe, null).add(0, 1, 1, bronzePipe, null).build();


        LARGE_STEAM_BOILER = MachineRegistrationHelper.registerMachine("Large Steam Boiler", "large_steam_boiler",
                bet -> new SteamBoilerMultiblockBlockEntity(bet, largeSteamBoilerShape, "large_steam_boiler",
                        256, false));
        ReiMachineRecipes.registerMultiblockShape("large_steam_boiler", largeSteamBoilerShape);

        ShapeTemplate advancedLargeSteamBoilerShape = new ShapeTemplate.Builder(MachineCasings.HEATPROOF)
                .add3by3(-2, invarCasings, false, slbHatchFlags)
                .add3by3(-1, bronzePlatedBricks, true, null)
                .add3by3(0, bronzePlatedBricks, true, null)
                .add3by3(1, bronzePlatedBricks, true, null)
                .add3by3(2, bronzePlatedBricks, false, null)
                .add(0, -1, 1, bronzePipe, null)
                .add(0, 0, 1, bronzePipe, null)
                .add(0, 1, 1, bronzePipe, null).build();

        ADVANCED_LARGE_STEAM_BOILER = MachineRegistrationHelper.registerMachine("Advanced Large Steam Boiler", "advanced_large_steam_boiler",
                bet -> new SteamBoilerMultiblockBlockEntity(bet, advancedLargeSteamBoilerShape, "advanced_large_steam_boiler",
                        1024, false));
        ReiMachineRecipes.registerMultiblockShape("advanced_large_steam_boiler", advancedLargeSteamBoilerShape);


        ShapeTemplate highPressureLargeSteamBoilerShape = new ShapeTemplate.Builder(MachineCasings.HEATPROOF)
                .add3by3(-1, invarCasings, false, slbHatchFlags)
                .add3by3(0, stainlessSteelClean, true, null)
                .add3by3(1, stainlessSteelClean, true, null)
                .add3by3(2, stainlessSteelClean, false, null)
                .add(0, 0, 1, stainlessSteelPipe, null)
                .add(0, 1, 1, stainlessSteelPipe, null).build();

        HIGH_PRESSURE_LARGE_STEAM_BOILER = MachineRegistrationHelper.registerMachine(
                "High Pressure Large Steam Boiler",
                "high_pressure_large_steam_boiler",
                bet -> new SteamBoilerMultiblockBlockEntity(bet, highPressureLargeSteamBoilerShape, "high_pressure_large_steam_boiler",
                        2048, true));
        ReiMachineRecipes.registerMultiblockShape("high_pressure_large_steam_boiler", highPressureLargeSteamBoilerShape);

        ShapeTemplate highPressureAdvancedLargeSteamBoilerShape = new ShapeTemplate.Builder(MachineCasings.HEATPROOF)
                .add3by3(-2, invarCasings, false, slbHatchFlags)
                .add3by3(-1, stainlessSteelClean, true, null)
                .add3by3(0, stainlessSteelClean, true, null)
                .add3by3(1, stainlessSteelClean, true, null)
                .add3by3(2, stainlessSteelClean, false, null)
                .add(0, -1, 1, stainlessSteelPipe, null)
                .add(0, 0, 1, stainlessSteelPipe, null)
                .add(0, 1, 1, stainlessSteelPipe, null).build();

        HIGH_PRESSURE_ADVANCED_LARGE_STEAM_BOILER = MachineRegistrationHelper.registerMachine(
                "High Pressure Advanced Large Steam Boiler",
                "high_pressure_advanced_large_steam_boiler",
                bet -> new SteamBoilerMultiblockBlockEntity(bet, highPressureAdvancedLargeSteamBoilerShape, "high_pressure_advanced_large_steam_boiler",
                        8192, true));
        ReiMachineRecipes.registerMultiblockShape("high_pressure_advanced_large_steam_boiler", highPressureAdvancedLargeSteamBoilerShape);
    }

    private static void quarries() {
        SimpleMember steelCasing = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("steel_machine_casing")).asBlock());
        SimpleMember steelPipe = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("steel_machine_casing_pipe")).asBlock());
        HatchFlags quarryHatchFlags = new HatchFlags.Builder().with(ITEM_INPUT, FLUID_INPUT, ITEM_OUTPUT).build();
        HatchFlags quarryElectricHatchFlags = new HatchFlags.Builder().with(ITEM_INPUT, ITEM_OUTPUT, ENERGY_INPUT).build();

        ShapeTemplate.Builder quarryShapeBuilder = new ShapeTemplate.Builder(MachineCasings.STEEL).add3by3(0, steelCasing, true, quarryHatchFlags)
                .add3by3(1, steelCasing, true, quarryHatchFlags);

        ShapeTemplate.Builder quarryElectricShapeBuilder = new ShapeTemplate.Builder(MachineCasings.STEEL)
                .add3by3(0, steelCasing, true, quarryElectricHatchFlags).add3by3(1, steelCasing, true, quarryElectricHatchFlags);

        for (int y = 2; y <= 4; y++) {
            quarryShapeBuilder.add(-1, y, 1, steelPipe, null);
            quarryShapeBuilder.add(1, y, 1, steelPipe, null);
            quarryElectricShapeBuilder.add(-1, y, 1, steelPipe, null);
            quarryElectricShapeBuilder.add(1, y, 1, steelPipe, null);
        }
        quarryShapeBuilder.add(0, 4, 1, steelCasing, null);
        quarryElectricShapeBuilder.add(0, 4, 1, steelCasing, null);

        SimpleMember chain = SimpleMember.verticalChain();

        for (int y = 0; y <= 3; y++) {
            quarryShapeBuilder.add(0, y, 1, chain, null);
            quarryElectricShapeBuilder.add(0, y, 1, chain, null);
        }

        ShapeTemplate quarryShape = quarryShapeBuilder.build();
        ShapeTemplate quarryElectricShape = quarryElectricShapeBuilder.build();

        STEAM_QUARRY = MachineRegistrationHelper.registerMachine(
                "Steam Quarry",
                "steam_quarry",
                bet -> new SteamCraftingMultiblockBlockEntity(bet, "steam_quarry", quarryShape, MIMachineRecipeTypes.QUARRY));
        ReiMachineRecipes.registerMultiblockShape("steam_quarry", quarryShape);
        ELECTRIC_QUARRY = MachineRegistrationHelper.registerMachine(
                "Electric Quarry",
                "electric_quarry",
                bet -> new ElectricCraftingMultiblockBlockEntity(bet, "electric_quarry", quarryElectricShape, MIMachineRecipeTypes.QUARRY));
        ReiMachineRecipes.registerMultiblockShape("electric_quarry", quarryElectricShape);
    }

    private static void oilDrillingRig() {
        ShapeTemplate.Builder oilDrillingRigShapeBuilder = new ShapeTemplate.Builder(MachineCasings.STEEL);
        SimpleMember steelCasing = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("steel_machine_casing")).asBlock());
        SimpleMember steelPipe = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("steel_machine_casing_pipe")).asBlock());
        SimpleMember chain = SimpleMember.verticalChain();
        HatchFlags hatchFlags = new HatchFlags.Builder().with(ITEM_INPUT).with(FLUID_OUTPUT).with(ENERGY_INPUT).build();
        // pillars
        for (int y = -4; y <= -2; ++y) {
            oilDrillingRigShapeBuilder.add(-2, y, -1, steelCasing, null);
            oilDrillingRigShapeBuilder.add(2, y, -1, steelCasing, null);
            oilDrillingRigShapeBuilder.add(-2, y, 3, steelCasing, null);
            oilDrillingRigShapeBuilder.add(2, y, 3, steelCasing, null);
        }
        // platform
        for (int x = -2; x <= 2; ++x) {
            for (int z = -1; z <= 3; ++z) {
                if (x == 2 || x == -2 || z == -1 || z == 3) {
                    oilDrillingRigShapeBuilder.add(x, -1, z, steelCasing, null);
                }
            }
        }
        // chains and pipe casings
        for (int y = -4; y <= 4; ++y) {
            oilDrillingRigShapeBuilder.add(-1, y, 1, chain, null);
            oilDrillingRigShapeBuilder.add(1, y, 1, chain, null);
            if (y >= -1) {
                oilDrillingRigShapeBuilder.add(0, y, 1, steelPipe, null);
            }
        }
        // top
        for (int x = -2; x <= 2; ++x) {
            oilDrillingRigShapeBuilder.add(x, 5, 1, steelCasing, null);
        }
        // hatches
        oilDrillingRigShapeBuilder.add(-1, 0, 0, steelCasing, hatchFlags);
        oilDrillingRigShapeBuilder.add(1, 0, 0, steelCasing, hatchFlags);
        oilDrillingRigShapeBuilder.add(-1, 0, 2, steelCasing, hatchFlags);
        oilDrillingRigShapeBuilder.add(0, 0, 2, steelCasing, hatchFlags);
        oilDrillingRigShapeBuilder.add(1, 0, 2, steelCasing, hatchFlags);

        ShapeTemplate oilDrillingRigShape = oilDrillingRigShapeBuilder.build();

        OIL_DRILLING_RIG = MachineRegistrationHelper.registerMachine(
                "Oil Drilling Rig",
                "oil_drilling_rig", bet -> new ElectricCraftingMultiblockBlockEntity(bet,
                        "oil_drilling_rig", oilDrillingRigShape, MIMachineRecipeTypes.OIL_DRILLING_RIG));
        ReiMachineRecipes.registerMultiblockShape("oil_drilling_rig", oilDrillingRigShape);
    }

    private static void vacuumFreezer() {
        HatchFlags vacuumFreezerHatches = new HatchFlags.Builder().with(ITEM_INPUT).with(ITEM_OUTPUT).with(FLUID_INPUT).with(FLUID_OUTPUT)
                .with(ENERGY_INPUT).build();
        ShapeTemplate vacuumFreezerShape = new ShapeTemplate.Builder(MachineCasings.FROSTPROOF)
                .add3by3LevelsRoofed(-1, 2, frostproofMachineCasing, vacuumFreezerHatches).build();
        VACUUM_FREEZER = MachineRegistrationHelper.registerMachine(
                "Vacuum Freezer",
                "vacuum_freezer",
                bet -> new ElectricCraftingMultiblockBlockEntity(bet, "vacuum_freezer", vacuumFreezerShape, MIMachineRecipeTypes.VACUUM_FREEZER));
        ReiMachineRecipes.registerMultiblockShape("vacuum_freezer", vacuumFreezerShape);
    }

    private static void distillationTower() {
        DISTILLATION_TOWER = MachineRegistrationHelper.registerMachine(
                "Distillation Tower",
                "distillation_tower", DistillationTowerBlockEntity::new);
        DistillationTowerBlockEntity.registerReiShapes();
    }

    private static void largeDieselGenerator() {
        ShapeTemplate.Builder largeDieselGeneratorShapeBuilder = new ShapeTemplate.Builder(MachineCasings.SOLID_TITANIUM);
        for (int z = 1; z < 4; z++) {
            largeDieselGeneratorShapeBuilder.add(0, 0, z, z < 3 ? titaniumPipe : titaniumCasing, z == 3 ? energyOutput : null);
            for (int x = -1; x < 2; x++) {
                largeDieselGeneratorShapeBuilder.add(x, 1, z, titaniumCasing, null);
                largeDieselGeneratorShapeBuilder.add(x, -1, z, titaniumCasing, null);
                if (x != 0) {
                    largeDieselGeneratorShapeBuilder.add(x, 0, z, titaniumCasing, z < 3 ? fluidInputs : null);
                }
            }
        }
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (x != 0 || y != 0) {
                    largeDieselGeneratorShapeBuilder.add(x, y, 0, titaniumPipe, null);
                }
            }
        }
        ShapeTemplate largeDieselGeneratorShape = largeDieselGeneratorShapeBuilder.build();
        LARGE_DIESEL_GENERATOR = MachineRegistrationHelper.registerMachine(
                "Large Diesel Generator",
                "large_diesel_generator", bet ->
                        new EnergyFromFluidMultiblockBlockEntity(bet, "large_diesel_generator",
                                largeDieselGeneratorShape,
                                FluidConsumerComponent.ofFluidFuels(16384)));
        ReiMachineRecipes.registerMultiblockShape("large_diesel_generator", largeDieselGeneratorShape);
    }

    private static ShapeTemplate largeTurbineShape(MachineCasing mainCasing, SimpleMember casing, SimpleMember pipe) {
        ShapeTemplate.Builder largeTurbineBuilder = new ShapeTemplate.Builder(mainCasing);
        for (int z = 0; z < 4; z++) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (z == 0) {
                        if (x != 0 || y != 0) {
                            largeTurbineBuilder.add(x, y, z, casing, fluidInputs);
                        }
                    } else if (z == 3) {
                        largeTurbineBuilder.add(x, y, z, casing, (x == 0 && y == 0) ? energyOutput : null);
                    } else {
                        largeTurbineBuilder.add(x, y, z, pipe, null);
                    }

                }
            }
        }
        return largeTurbineBuilder.build();

    }

    private static void largeSteamTurbine() {
        ShapeTemplate largeSteamTurbineShape = largeTurbineShape(MachineCasings.CLEAN_STAINLESS_STEEL,
                stainlessSteelClean, stainlessSteelPipe);

        LARGE_STEAM_TURBINE = MachineRegistrationHelper.registerMachine(
                "Large Steam Turbine",
                "large_steam_turbine", bet ->
                        new EnergyFromFluidMultiblockBlockEntity(bet, "large_steam_turbine", largeSteamTurbineShape,
                                FluidConsumerComponent.of(16384,
                                        (Fluid f) -> (f == MIFluids.STEAM.asFluid() || f == MIFluids.HIGH_PRESSURE_STEAM.asFluid()
                                                || f == MIFluids.HIGH_PRESSURE_HEAVY_WATER_STEAM.asFluid()
                                                || f == MIFluids.HEAVY_WATER_STEAM.asFluid()),
                                        (Fluid f) -> ((f == MIFluids.STEAM.asFluid() || f == MIFluids.HEAVY_WATER_STEAM.asFluid()) ? 1 : 8)
                                )));
        ReiMachineRecipes.registerMultiblockShape("large_steam_turbine", largeSteamTurbineShape);
    }

    private static void heathExchanger() {
        ShapeTemplate.Builder heatExchangerShapeBuilder = new ShapeTemplate.Builder(MachineCasings.STAINLESS_STEEL_PIPE);
        for (int z = 0; z < 5; z++) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (z > 0 && z < 4) {

                        heatExchangerShapeBuilder.add(x, y, z, x == -1 ? invarCasings : x == 0 ? stainlessSteelPipe : frostproofMachineCasing,
                                ((y == 1 || y == -1) && x == 0) ? energyInput : null);
                    } else {
                        if (z != 0 || x != 0 || y != 0) {
                            HatchFlags flag = null;
                            if (x == 0) {
                                flag = new HatchFlags.Builder().with(z == 0 ? ITEM_INPUT : ITEM_OUTPUT).with(ENERGY_INPUT).build();
                            } else {
                                boolean fluidOutput = (x == -1) ^ (z == 0);
                                flag = new HatchFlags.Builder().with(fluidOutput ? FLUID_OUTPUT : FLUID_INPUT).build();
                            }

                            heatExchangerShapeBuilder.add(x, y, z, stainlessSteelPipe, flag);
                        }
                    }
                }
            }
        }
        ShapeTemplate heatExchangerShape = heatExchangerShapeBuilder.build();
        HEAT_EXCHANGER = MachineRegistrationHelper.registerMachine(
                "Heat Exchanger",
                "heat_exchanger",
                bet -> new ElectricCraftingMultiblockBlockEntity(bet, "heat_exchanger", heatExchangerShape, MIMachineRecipeTypes.HEAT_EXCHANGER));
        ReiMachineRecipes.registerMultiblockShape("heat_exchanger", heatExchangerShape);

    }

    private static void pressurizer() {
        ShapeTemplate.Builder pressurizeShapeBuilder = new ShapeTemplate.Builder(MachineCasings.TITANIUM);
        for (int y = -1; y < 3; y++) {
            SimpleMember member = (y == -1 || y == 2) ? titaniumCasing : titaniumPipe;
            HatchFlags flag = null;
            if (y == -1) {
                flag = new HatchFlags.Builder().with(ENERGY_INPUT, FLUID_OUTPUT).build();
            } else if (y == 2) {
                flag = new HatchFlags.Builder().with(FLUID_INPUT, ITEM_INPUT).build();
            }
            pressurizeShapeBuilder.add(-1, y, 1, member, flag);
            pressurizeShapeBuilder.add(0, y, 1, member, flag);
            pressurizeShapeBuilder.add(1, y, 1, member, flag);
            pressurizeShapeBuilder.add(0, y, 2, member, flag);
            if (y != 0) {
                pressurizeShapeBuilder.add(0, y, 0, member, flag);
            }
        }
        ShapeTemplate pressurizerShape = pressurizeShapeBuilder.build();
        PRESSURIZER = MachineRegistrationHelper.registerMachine(
                "Pressurizer",
                "pressurizer",
                bet -> new ElectricCraftingMultiblockBlockEntity(bet, "pressurizer", pressurizerShape, MIMachineRecipeTypes.PRESSURIZER));
        ReiMachineRecipes.registerMultiblockShape("pressurizer", pressurizerShape);
    }

    private static void implosionCompressor() {
        ShapeTemplate.Builder implosionCompressorShapeBuilder = new ShapeTemplate.Builder(MachineCasings.TITANIUM);
        HatchFlags hatchs = new HatchFlags.Builder().with(ITEM_OUTPUT, ITEM_INPUT, ENERGY_INPUT).build();
        implosionCompressorShapeBuilder.add3by3(0, titaniumCasing, false, hatchs);
        implosionCompressorShapeBuilder.add3by3(1, blastProofCasing, true, null);
        implosionCompressorShapeBuilder.add3by3(2, blastProofCasing, true, null);
        implosionCompressorShapeBuilder.add3by3(3, titaniumCasing, false, null);

        ShapeTemplate implosionCompressorShape = implosionCompressorShapeBuilder.build();
        IMPLOSION_COMPRESSOR = MachineRegistrationHelper.registerMachine(
                "Implosion Compressor",
                "implosion_compressor",
                bet -> new ElectricCraftingMultiblockBlockEntity(bet, "implosion_compressor", implosionCompressorShape, MIMachineRecipeTypes.IMPLOSION_COMPRESSOR));
        ReiMachineRecipes.registerMultiblockShape("implosion_compressor", implosionCompressorShape);
    }

    private static void nuclearReactor() {
        NUCLEAR_REACTOR = MachineRegistrationHelper.registerMachine(
                "Nuclear Reactor",
                "nuclear_reactor", NuclearReactorMultiblockBlockEntity::new);
        NuclearReactorMultiblockBlockEntity.registerReiShapes();
    }

    private static void largeTank() {
        LARGE_TANK = MachineRegistrationHelper.registerMachine(
                "Large Tank",
                "large_tank", LargeTankMultiblockBlockEntity::new);
        LargeTankMultiblockBlockEntity.registerFluidAPI(LARGE_TANK);
    }

    private static void fusionReactor() {
        ShapeTemplate.Builder fusionReactorShapeBuilder = new ShapeTemplate.Builder(MachineCasings.EV);
        int[][] shapeEdge = new int[][]{
                {6, 1, 0, 0},
                {4, 3, 0, 0},
                {3, 3, 0, 0},
                {2, 2, 0, 0},
                {1, 2, 0, 0},
                {1, 2, 0, 0},
                {0, 2, 0, 0},
        };


        int[][] shapeCenter = new int[][]{
                {5, 2, 0, 0},
                {3, 2, 2, 0},
                {2, 1, 2, 2},
                {1, 1, 2, 1},
                {1, 1, 1, 1},
                {0, 1, 1, 1},
                {0, 1, 1, 1}
        };

        for (int y = -1; y <= 1; y++) {

            int[][] shape = (y == 0) ? shapeCenter : shapeEdge;

            for (int i = 0; i < 7; i++) {
                int x = i + 1;

                for (int k = 0; k < 4; k++) {
                    int[] placement = shape[6 - i];
                    int z0 = placement[0];
                    int z1 = z0 + placement[1];
                    int z2 = z1 + placement[2];
                    int z3 = z2 + placement[3];
                    for (int z = z0; z < z3; z++) {
                        if (z < z1 || z >= z2) {
                            fusionReactorShapeBuilder.add(x, y, z, highlyAdvancedHull);
                            fusionReactorShapeBuilder.add(-x, y, z, highlyAdvancedHull);
                            fusionReactorShapeBuilder.add(x, y, 14 - z, highlyAdvancedHull);
                            fusionReactorShapeBuilder.add(-x, y, 14 - z, highlyAdvancedHull);
                        } else if (z >= z1) {
                            fusionReactorShapeBuilder.add(x, y, z, fusionChamber);
                            fusionReactorShapeBuilder.add(-x, y, z, fusionChamber);
                            fusionReactorShapeBuilder.add(x, y, 14 - z, fusionChamber);
                            fusionReactorShapeBuilder.add(-x, y, 14 - z, fusionChamber);
                        }
                    }
                }

            }

            HatchFlags flags = new HatchFlags.Builder().with(FLUID_INPUT, FLUID_OUTPUT, ENERGY_INPUT).build();

            for (int l = 0; l < ((y == 0) ? 3 : 2); l++) {
                if (!(y == 0 && l == 1)) {

                    HatchFlags currentFlag = l == 0 ? flags : null;

                    if (l != 0 || y != 0) {
                        fusionReactorShapeBuilder.add(0, y, l, highlyAdvancedHull, currentFlag);
                    }
                    fusionReactorShapeBuilder.add(0, y, 14 - l, highlyAdvancedHull, currentFlag);
                    fusionReactorShapeBuilder.add(-7 + l, y, 7, highlyAdvancedHull, currentFlag);
                    fusionReactorShapeBuilder.add(7 - l, y, 7, highlyAdvancedHull, currentFlag);
                } else {
                    fusionReactorShapeBuilder.add(0, y, l, fusionChamber);
                    fusionReactorShapeBuilder.add(0, y, 14 - l, fusionChamber);
                    fusionReactorShapeBuilder.add(-7 + l, y, 7, fusionChamber);
                    fusionReactorShapeBuilder.add(7 - l, y, 7, fusionChamber);
                }
            }
        }

        ShapeTemplate fusionReactorShape = fusionReactorShapeBuilder.build();
        FUSION_REACTOR = MachineRegistrationHelper.registerMachine(
                "Fusion Reactor",
                "fusion_reactor",
                bet -> new FusionReactorBlockEntity(bet, "fusion_reactor",
                        fusionReactorShape));
        ReiMachineRecipes.registerMultiblockShape("fusion_reactor", fusionReactorShape);

    }

    private static void plasmaTurbine() {
        ShapeTemplate plasmaTurbineShape = largeTurbineShape(MachineCasings.PLASMA_HANDLING_IRIDIUM,
                plasmaHandlingIridium, iridiumPipe);

        PLASMA_TURBINE = MachineRegistrationHelper.registerMachine(
                "Plasma Turbine",
                "plasma_turbine", bet ->
                        new EnergyFromFluidMultiblockBlockEntity(bet, "plasma_turbine", plasmaTurbineShape,
                                FluidConsumerComponent.of(
                                        1 << 20,
                                        MIFluids.HELIUM_PLASMA.asFluid(),
                                        100000
                                )));
        ReiMachineRecipes.registerMultiblockShape("plasma_turbine", plasmaTurbineShape);
    }

    public static void init() {

        invarCasings = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("heatproof_machine_casing")).asBlock());

        bronzePlatedBricks = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("bronze_plated_bricks")).asBlock());
        bronzePipe = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("bronze_machine_casing_pipe")).asBlock());

        frostproofMachineCasing = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("frostproof_machine_casing")).asBlock());

        stainlessSteelClean = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("clean_stainless_steel_machine_casing")).asBlock());
        stainlessSteelPipe = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("stainless_steel_machine_casing_pipe")).asBlock());

        titaniumCasing = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("solid_titanium_machine_casing")).asBlock());
        titaniumPipe = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("titanium_machine_casing_pipe")).asBlock());

        blastProofCasing = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("blastproof_casing")).asBlock());


        highlyAdvancedHull = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("highly_advanced_machine_hull")).asBlock());
        fusionChamber = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("fusion_chamber")).asBlock());

        plasmaHandlingIridium = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("plasma_handling_iridium_machine_casing")).asBlock());
        iridiumPipe = SimpleMember.forBlock(MIBlock.BLOCKS.get(new MIIdentifier("iridium_machine_casing_pipe")).asBlock());

        cokeOven();
        steamBlastFurnace();
        electricBlastFurnace();
        steamBoilers();
        quarries();
        oilDrillingRig();
        vacuumFreezer();
        distillationTower();
        largeDieselGenerator();
        largeSteamTurbine();
        heathExchanger();
        pressurizer();
        implosionCompressor();
        nuclearReactor();
        largeTank();
        fusionReactor();
        plasmaTurbine();
    }

    public static void clientInit() {
        MachineRegistrationHelper.addMachineModel("coke_oven", "coke_oven", MachineCasings.BRICKS, true, false, false);
        new Rei("Coke Oven", "coke_oven", MIMachineRecipeTypes.COKE_OVEN, new ProgressBar.Parameters(77, 33, "arrow"))
                .items(inputs -> inputs.addSlot(56, 35), outputs -> outputs.addSlot(102, 35))
                .fluids(inputs -> {
                }, outputs -> outputs.addSlot(102, 53))
                .steam(true)
                .register();

        MachineRegistrationHelper.addMachineModel("steam_blast_furnace", "steam_blast_furnace", MachineCasings.FIREBRICKS, true, false, false);
        new Rei("Steam Blast Furnace", "steam_blast_furnace", MIMachineRecipeTypes.BLAST_FURNACE, new ProgressBar.Parameters(77, 33, "arrow"))
                .items(inputs -> inputs.addSlots(56, 35, 1, 2), outputs -> outputs.addSlots(102, 35, 1, 1))
                .fluids(fluids -> fluids.addSlots(36, 35, 1, 1), outputs -> outputs.addSlots(122, 35, 1, 1))
                .workstations("steam_blast_furnace", "electric_blast_furnace").extraTest(recipe -> recipe.eu <= 4)
                .steam(false)
                .register();

        MachineRegistrationHelper.addMachineModel("electric_blast_furnace", "electric_blast_furnace", MachineCasings.HEATPROOF, true, false, false);

        MachineRegistrationHelper.addMachineModel("large_steam_boiler", "large_boiler", MachineCasings.BRONZE_PLATED_BRICKS, true, false, false);

        MachineRegistrationHelper.addMachineModel("advanced_large_steam_boiler", "large_boiler", MachineCasings.BRONZE_PLATED_BRICKS, true, false,
                false);

        MachineRegistrationHelper.addMachineModel("high_pressure_large_steam_boiler", "large_boiler", CLEAN_STAINLESS_STEEL, true, false, false);

        MachineRegistrationHelper.addMachineModel("high_pressure_advanced_large_steam_boiler", "large_boiler", CLEAN_STAINLESS_STEEL, true, false,
                false);

        MachineRegistrationHelper.addMachineModel("steam_quarry", "quarry", MachineCasings.STEEL, true, false, false);
        new Rei("Steam Quarry", "steam_quarry", MIMachineRecipeTypes.QUARRY, new ProgressBar.Parameters(77, 33, "arrow"))
                .items(inputs -> inputs.addSlot(56, 35), outputs -> outputs.addSlots(102, 35, 4, 4))
                .workstations("steam_quarry", "electric_quarry").extraTest(recipe -> recipe.eu <= 4)
                .steam(false)
                .register();
        new Rei("Electric Quarry", "electric_quarry", MIMachineRecipeTypes.QUARRY, new ProgressBar.Parameters(77, 33, "arrow"))
                .items(inputs -> inputs.addSlot(56, 35), outputs -> outputs.addSlots(102, 35, 4, 4))
                .workstations("electric_quarry").extraTest(recipe -> recipe.eu > 4)
                .register();

        MachineRegistrationHelper.addMachineModel("electric_quarry", "quarry", MachineCasings.STEEL, true, false, false);

        MachineRegistrationHelper.addMachineModel("vacuum_freezer", "vacuum_freezer", MachineCasings.FROSTPROOF, true, false, false);
        new Rei("Vacuum Freezer", "vacuum_freezer", MIMachineRecipeTypes.VACUUM_FREEZER, new ProgressBar.Parameters(77, 33, "arrow"))
                .items(inputs -> inputs.addSlots(56, 35, 1, 2), outputs -> outputs.addSlot(102, 35))
                .fluids(inputs -> inputs.addSlots(36, 35, 1, 2), outputs -> outputs.addSlot(122, 35))
                .register();

        MachineRegistrationHelper.addMachineModel("oil_drilling_rig", "oil_drilling_rig", MachineCasings.STEEL, true, false, false);
        new Rei("Oil Drilling Rig", "oil_drilling_rig", MIMachineRecipeTypes.OIL_DRILLING_RIG, new ProgressBar.Parameters(77, 33, "arrow"))
                .items(inputs -> inputs.addSlot(36, 35), outputs -> {
                })
                .fluids(inputs -> {
                }, outputs -> outputs.addSlot(122, 35))
                .register();

        MachineRegistrationHelper.addMachineModel("distillation_tower", "distillation_tower", CLEAN_STAINLESS_STEEL, true, false, false);
        new Rei("Distillation Tower", "distillation_tower", MIMachineRecipeTypes.DISTILLATION_TOWER, new ProgressBar.Parameters(77, 33, "arrow"))
                .fluids(inputs -> inputs.addSlot(56, 35), outputs -> outputs.addSlots(102, 35, 8, 1))
                .register();

        MachineRegistrationHelper.addMachineModel("large_diesel_generator", "diesel_generator", MachineCasings.SOLID_TITANIUM, true, false, false);

        MachineRegistrationHelper.addMachineModel("large_steam_turbine", "steam_turbine", CLEAN_STAINLESS_STEEL, true, false, false);

        MachineRegistrationHelper.addMachineModel("heat_exchanger", "heat_exchanger", MachineCasings.STAINLESS_STEEL_PIPE, true, false, false);
        new Rei("Heat Exchanger", "heat_exchanger", MIMachineRecipeTypes.HEAT_EXCHANGER, new ProgressBar.Parameters(77, 42, "arrow"))
                .items(inputs -> inputs.addSlot(36, 35), outputs -> outputs.addSlot(122, 35))
                .fluids(inputs -> inputs.addSlots(56, 35, 1, 2), outputs -> outputs.addSlots(102, 35, 1, 2))
                .register();

        MachineRegistrationHelper.addMachineModel("pressurizer", "pressurizer", MachineCasings.TITANIUM_PIPE, true, false, false);
        new Rei("Pressurizer", "pressurizer", MIMachineRecipeTypes.PRESSURIZER, new ProgressBar.Parameters(77, 33, "arrow"))
                .items(inputs -> inputs.addSlot(38, 35), outputs -> {
                })
                .fluids(inputs -> inputs.addSlot(56, 35), outputs -> outputs.addSlot(102, 35))
                .register();

        MachineRegistrationHelper.addMachineModel("implosion_compressor", "compressor", MachineCasings.SOLID_TITANIUM, true, false, false);
        new Rei("Implosion Compressor", "implosion_compressor", MIMachineRecipeTypes.IMPLOSION_COMPRESSOR,
                new ProgressBar.Parameters(77, 42, "compress"))
                        .items(inputs -> inputs.addSlots(36, 35, 2, 2), outputs -> outputs.addSlot(102, 42))
                        .register();

        MachineRegistrationHelper.addMachineModel("nuclear_reactor", "nuclear_reactor", MachineCasings.NUCLEAR, true, false, false, true);

        MachineRegistrationHelper.addMachineModel("large_tank",
                "large_tank", MachineCasings.STEEL, true, false, false, false);

        MachineRegistrationHelper.addMachineModel("fusion_reactor",
                "fusion_reactor", MachineCasings.EV, true, false, false, true);
        new Rei("Fusion Reactor", "fusion_reactor", MIMachineRecipeTypes.FUSION_REACTOR, new ProgressBar.Parameters(66, 33, "arrow"))
                .fluids(inputs -> inputs.addSlots(26, 35, 2, 1), outputs -> outputs.addSlots(92, 35, 3, 1))
                .register();

        MachineRegistrationHelper.addMachineModel("plasma_turbine", "steam_turbine",
                MachineCasings.PLASMA_HANDLING_IRIDIUM, true, false, false);

        registerEbfReiCategories();
    }

    private static void registerEbfReiCategories() {
        // Register REI categories
        for (int i = 0; i < ElectricBlastFurnaceBlockEntity.coils.size(); ++i) {
            long previousMax = i == 0 ? 4 : ElectricBlastFurnaceBlockEntity.coilsMaxBaseEU.get(ElectricBlastFurnaceBlockEntity.coils.get(i - 1));
            long currentMax = ElectricBlastFurnaceBlockEntity.coilsMaxBaseEU.get(ElectricBlastFurnaceBlockEntity.coils.get(i));
            List<String> workstations = new ArrayList<>();
            workstations.add("electric_blast_furnace");
            for (int j = i; j < ElectricBlastFurnaceBlockEntity.coils.size(); ++j) {
                workstations.add(ElectricBlastFurnaceBlockEntity.coilNames.get(j));
            }

            new Rei("EBF" + ElectricBlastFurnaceBlockEntity.coilEnglishNames.get(i), "electric_blast_furnace_" + i,
                    MIMachineRecipeTypes.BLAST_FURNACE,
                    new ProgressBar.Parameters(77, 33, "arrow"))
                            .items(inputs -> inputs.addSlots(56, 35, 1, 2), outputs -> outputs.addSlot(102, 35))
                            .fluids(fluids -> fluids.addSlot(36, 35), outputs -> outputs.addSlot(122, 35))
                            .extraTest(recipe -> previousMax < recipe.eu && recipe.eu <= currentMax)
                            .workstations(workstations.toArray(new String[0])).register();
        }
    }

    public static class Rei {
        private final String englishName;
        private final String category;
        private final MachineRecipeType recipeType;
        private final ProgressBar.Parameters progressBarParams;
        private final List<String> workstations;
        private Predicate<MachineRecipe> extraTest = recipe -> true;
        private SlotPositions.Builder itemInputs = new SlotPositions.Builder();
        private SlotPositions.Builder itemOutputs = new SlotPositions.Builder();
        private SlotPositions.Builder fluidInputs = new SlotPositions.Builder();
        private SlotPositions.Builder fluidOutputs = new SlotPositions.Builder();
        private SteamMode steamMode = SteamMode.ELECTRIC_ONLY;

        public Rei(String englishName, String category, MachineRecipeType recipeType, ProgressBar.Parameters progressBarParams) {
            this.englishName = englishName;
            this.category = category;
            this.recipeType = recipeType;
            this.progressBarParams = progressBarParams;
            this.workstations = new ArrayList<>();
            workstations.add(category);
        }

        public Rei items(Consumer<SlotPositions.Builder> inputs, Consumer<SlotPositions.Builder> outputs) {
            inputs.accept(itemInputs);
            outputs.accept(itemOutputs);
            return this;
        }

        public Rei fluids(Consumer<SlotPositions.Builder> inputs, Consumer<SlotPositions.Builder> outputs) {
            inputs.accept(fluidInputs);
            outputs.accept(fluidOutputs);
            return this;
        }

        public Rei extraTest(Predicate<MachineRecipe> extraTest) {
            this.extraTest = extraTest;
            return this;
        }

        public Rei workstations(String... workstations) {
            this.workstations.clear();
            this.workstations.addAll(Arrays.asList(workstations));
            return this;
        }

        public Rei steam(boolean steamOnly) {
            this.steamMode = steamOnly ? SteamMode.STEAM_ONLY : SteamMode.BOTH;
            return this;
        }

        public final void register() {
            // Allow KJS scripts to add slots
            KubeJSProxy.instance.fireAddMultiblockSlotsEvent(category, itemInputs, itemOutputs, fluidInputs, fluidOutputs);

            ReiMachineRecipes.registerCategory(category, new MachineCategoryParams(englishName, category,
                    itemInputs.build(), itemOutputs.build(), fluidInputs.build(), fluidOutputs.build(),
                    progressBarParams, recipe -> recipe.getType() == recipeType && extraTest.test(recipe), true, steamMode));
            for (String workstation : workstations) {
                ReiMachineRecipes.registerWorkstation(category, workstation);
                ReiMachineRecipes.registerRecipeCategoryForMachine(workstation, category, ReiMachineRecipes.MachineScreenPredicate.MULTIBLOCK);
                ReiMachineRecipes.registerMachineClickArea(workstation, CRAFTING_GUI);
            }
        }
    }
}
